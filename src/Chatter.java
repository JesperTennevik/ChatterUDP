import javax.swing.*;

import Components.InputField;
import Components.MessageArea;
import Components.UsersArea;
import Components.disconnectButton;

import Net.Receiver;
import Net.ReceiverEventListener;
import Net.Sender;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Chatter extends JFrame {
    Receiver receiver;
    Sender sender;
    String ip;
    int port;
    String name;
    ArrayList<String> activeUsers = new ArrayList<>();

    JPanel panel;
    JTextArea usersArea;
    JTextField inputField;
    JScrollPane messageArea;
    JButton disconnectButton;

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

        panel = new JPanel(new BorderLayout());
        usersArea = new UsersArea();
        inputField = new InputField(sender, name);
        messageArea = new MessageArea();
        disconnectButton = new disconnectButton(sender, name);

        setTitle("Chatter");
        add(panel);
        panel.add(messageArea, BorderLayout.CENTER);
        panel.add(usersArea, BorderLayout.EAST);
        panel.add(inputField, BorderLayout.SOUTH);
        panel.add(disconnectButton, BorderLayout.NORTH);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        receiver.startReceive();
        receiver.registerReceiverListener(new ReceiverEventListener() {
            @Override
            public void OnUserJoined(String user) {
                activeUsers.add(user);
                updateUsers();
            }

            @Override
            public void OnUserLeft(String user) {
                activeUsers.remove(user);
                updateUsers();
            }

            @Override
            public void OnRequestSync() {
                activeUsers.clear();
                sender.sendMsg("UserPresent:"+name);
            }

            @Override
            public void OnMessageReceived(String message){
                SwingUtilities.invokeLater(() -> ((JTextArea)messageArea.getViewport().getComponent(0))
                        .append(message + "\n"));
            }
        });

        sender.sendMsg("UserJoined:"+name);
        sender.sendMsg("RequestSync");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sender.sendMsg("UserLeft:"+name);
                super.windowClosing(e);
            }
        });
    }

    private void updateUsers(){
        SwingUtilities.invokeLater(() -> {
            usersArea.setText("");
            for(String user : activeUsers){
                usersArea.append(user+"\n");
            }
        });
    }

    private void validateChatRoomInput(String chatRoom) throws InvalidInputIpException {
        if (!chatRoom.contains(":")) { throw new InvalidInputIpException("Requires format: Ip:Port"); }

        String[] split = chatRoom.split(":");
        if (split.length != 2) { throw new InvalidInputIpException("Requires format: Ip:Port"); }

        String ip = split[0];
        String port = split[1];

        if (!isValidIp(ip)) { throw new InvalidInputIpException("Invalid Ip address"); }

        if (!isValidPort(port)) { throw new InvalidInputIpException("Invalid port"); }
    }

    private boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) { return false; }
    }

    private boolean isValidIp(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) { return false; }


        for (int i = 0; i < 4; i++) {
            try {
                int value = Integer.parseInt(parts[i]);
                if(i == 0){
                    if(value < 224 || value > 239) { return false; }
                } else {
                    if(value < 0 || value > 255) { return false; }
                }
            } catch (NumberFormatException e) { return false; }
        }
        return true;
    }

    static void main(String[] args){ SwingUtilities.invokeLater(Chatter::new); }
}

class InvalidInputIpException extends Exception{
    public InvalidInputIpException(String m){
        super(m);
    }
}