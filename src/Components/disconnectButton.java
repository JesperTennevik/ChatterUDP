package Components;

import Net.Sender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class disconnectButton extends JButton implements ActionListener {
    String name;
    Sender sender;

    public disconnectButton(Sender sender, String name){
        this.sender = sender;
        this.name = name;
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(border);
        setText("Disconnect");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        sender.sendMsg("UserLeft:"+name);
        System.exit(1);
    }
}
