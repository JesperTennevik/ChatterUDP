package Net;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Receiver {
    MulticastSocket socket;
    ArrayList<String> activeUsers = new ArrayList<>();

    public Receiver(String ip, int port) throws IOException {
        socket = new MulticastSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        socket.joinGroup(new InetSocketAddress(ip, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public void startReceive(JTextArea textArea, JTextArea usersArea, String name, Sender sender){
        Thread.startVirtualThread(() -> {
            int packetSize = 512;
            DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);
            while(true){
                try{
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if(message.startsWith("UserJoined:") || message.startsWith("UserPresent:")){
                        String[] split = message.split(":");
                        activeUsers.add(split[1]);
                        updateUsers(usersArea);
                    }
                    else if(message.startsWith("UserLeft:")){
                        String[] split = message.split(":");
                        activeUsers.remove(split[1]);
                        updateUsers(usersArea);
                    }
                    else if(message.startsWith("RequestSync")){
                        activeUsers.clear();
                        sender.sendMsg("UserPresent:"+name);
                    }
                    else {
                        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
                    }
                }
                catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Error receiving message.");
                    break;
                }
            }
        });
    }

    private void updateUsers(JTextArea usersArea){
        SwingUtilities.invokeLater(() -> {
            usersArea.setText("");
            for(String user : activeUsers){
                usersArea.append(user+"\n");
            }
        });
    }
}
