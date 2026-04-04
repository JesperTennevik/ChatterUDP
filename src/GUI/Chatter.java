package GUI;

import javax.swing.*;

import Net.Receiver;

import java.io.IOException;

public class Chatter {
    Receiver receiver;
    String ip;
    int port;
    String name;

    public Chatter(){
        boolean validInput = false;
        while(!validInput) {
            String chatRoom = JOptionPane.showInputDialog(null, "Ip:Port");
            if (chatRoom == null) { return; }
            else {
                try { validateChatRoomInput(chatRoom); }
                catch(InvalidInputIpException e){
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    continue;
                }
            }

            String[] split = chatRoom.split(":");
            ip = split[0];
            port = Integer.parseInt(split[1]);

            try{
                receiver = new Receiver(ip, port);
                validInput = true;
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(null, "Unable to Connect to: " + ip + ":" + port);
            }
        }

        while(name == null){
            name = JOptionPane.showInputDialog(null, "Name: ");
            if(name == null){
                JOptionPane.showMessageDialog(null, "Invalid name");
            }
        }

        Thread rec = Thread.startVirtualThread(receiver);
    }

    private void validateChatRoomInput(String chatRoom) throws InvalidInputIpException {
        if(!chatRoom.contains(":")) { throw new InvalidInputIpException("Requires format: Ip:Port"); }

        String[] split = chatRoom.split(":");
        if (split.length != 2) { throw new InvalidInputIpException("Requires format: Ip:Port."); }
        else if (split[0].length() != 15) { throw new InvalidInputIpException("Invalid ip address."); }
        else if (split[1].length() != 4) { throw new InvalidInputIpException("Invalid port."); }

        try { Integer.parseInt(split[1]); }
        catch (Exception e) { throw new InvalidInputIpException("Invalid Port"); }

        // can/should add validations for ip och port ranges.
    }
}

class InvalidInputIpException extends Exception{
    public InvalidInputIpException(String m){
        super(m);
    }
}

