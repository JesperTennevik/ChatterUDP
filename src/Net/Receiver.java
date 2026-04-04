package Net;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class Receiver {
    MulticastSocket socket;

    public Receiver(String ip, int port) throws IOException {
        socket = new MulticastSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        socket.joinGroup(new InetSocketAddress(ip, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public void startReceive(JTextArea textArea){
        Thread.startVirtualThread(() -> {
            int packetSize = 512;
            DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);
            while(true){
                try{
                    socket.receive(packet);
                    String sender = packet.getAddress().getHostAddress();
                    String message = new String(packet.getData(), 0, packet.getLength());
                    SwingUtilities.invokeLater(() -> {
                        textArea.append(sender + ": " + message + "\n");
                    });
                }
                catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Error receiving message.");
                    break;
                }
            }
        });
    }
}
