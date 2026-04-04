package Net;

import java.io.IOException;
import java.net.*;

public class Receiver implements Runnable{
    MulticastSocket socket;

    public Receiver(String ip, int port) throws IOException {
        socket = new MulticastSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        socket.joinGroup(new InetSocketAddress(ip, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    synchronized String receive(){
        int packetSize = 512;
        DatagramPacket packet = new DatagramPacket(new byte[packetSize], packetSize);
        try{
            socket.receive(packet);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return new String(packet.getData(), 0, packet.getLength());
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            IO.println("Received: " + receive());
        }
    }
}
