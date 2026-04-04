package Net;

import java.io.IOException;
import java.net.*;

public class Sender {
    MulticastSocket socket;
    int port;
    InetAddress iAdr;
    public Sender(String ip, int port) throws IOException{
        iAdr = InetAddress.getByName(ip);
        this.port = port;
        socket = new MulticastSocket(null);
    }

    public void sendMsg(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, iAdr, port);
        socket.send(packet);
    }
}