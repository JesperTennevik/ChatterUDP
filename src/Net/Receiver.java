package Net;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class Receiver {
    MulticastSocket socket;
    ReceiverEventListener listener;

    public Receiver(String ip, int port) throws IOException {
        socket = new MulticastSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        socket.joinGroup(new InetSocketAddress(ip, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public void startReceive(){
        Thread.startVirtualThread(() -> {
            int packetSize = 512;
            DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);
            while(true){
                try{
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());

                    if(message.startsWith("UserJoined:") || message.startsWith("UserPresent:")){
                        String[] split = message.split(":");
                        listener.OnUserJoined(split[1]);
                    }
                    else if(message.startsWith("UserLeft:")){
                        String[] split = message.split(":");
                        listener.OnUserLeft(split[1]);
                    }
                    else if(message.startsWith("RequestSync")){
                        listener.OnRequestSync();
                    }
                    else {
                        listener.OnMessageReceived(message);
                    }
                }
                catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Error receiving message.");
                    break;
                }
            }
        });
    }
    public void registerReceiverListener(ReceiverEventListener listener){
        this.listener = listener;
    }
}
