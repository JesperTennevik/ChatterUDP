package Components;

import Net.Sender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputField extends JTextField implements ActionListener {
    Sender sender;
    String name;

    public InputField(Sender sender, String name){
        this.sender = sender;
        this.name = name;
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(border);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        sender.sendMsg(name + ": " + getText());
        SwingUtilities.invokeLater(() -> setText(""));
    }
}
