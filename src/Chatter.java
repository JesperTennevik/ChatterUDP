import javax.swing.*;

import Components.InputField;
import Components.MessageArea;
import Components.UsersArea;

import Net.Receiver;
import Net.Sender;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class Chatter extends JFrame {
    Receiver receiver;
    Sender sender;
    String ip;
    int port;
    String name;
    String clientID;

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
                sender = new Sender(ip, port);
                validInput = true;
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(null, "Unable to Connect to: " + ip + ":" + port);
            }
        }

        while(name == null){
            name = JOptionPane.showInputDialog(null, "Name: ");
            if(name == null){
                JOptionPane.showMessageDialog(null, "Invalid name.");
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea usersArea = new UsersArea();
        JTextField inputField = new InputField(sender, name);
        JScrollPane messageArea = new MessageArea();

        setTitle("Chatter");
        add(panel);
        panel.add(messageArea, BorderLayout.CENTER);
        panel.add(usersArea, BorderLayout.EAST);
        panel.add(inputField, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        clientID = UUID.randomUUID().toString();

        receiver.startReceive((JTextArea)messageArea.getViewport().getComponent(0), usersArea, name, sender);
        try{
            sender.sendMsg("UserJoined:"+name);
            sender.sendMsg("RequestSync");
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error Sending Message.");
        }
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

    static void main(String[] args){ SwingUtilities.invokeLater(Chatter::new); }
}

class InvalidInputIpException extends Exception{
    public InvalidInputIpException(String m){
        super(m);
    }
}

