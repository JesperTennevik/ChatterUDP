import javax.swing.*;

import GUI.Chatter;

public class Main {
    static void main(String[] args){
        try {
            SwingUtilities.invokeLater(Chatter::new);
        } catch(Exception e){
            IO.println("Något gick fel.");
        }
    }
}
