import javax.swing.*;
import java.awt.event.*;

public class chatta {
    public JButton send;
    public JButton exit;
    public JTextArea console;
    public JTextField message;
    public JPanel controlPanel;
    public JPanel rootPanel;
    private JScrollPane consolePanel;

    public chatta() {
        send.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String messageStr = message.getText();
                if(!messageStr.equals("")) {
                    new Thread(new Sender(Main.UserName, Main.IPAddress,  messageStr)).start();
                    message.setText("");
                    send.setEnabled(false);
                }
            }
        });

        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
            }
        });

        message.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String messageStr = message.getText();
                if (messageStr.equals("")){
                    send.setEnabled(false);
                }
                else{
                    send.setEnabled(true);
                }
            }
        });
    }
}
