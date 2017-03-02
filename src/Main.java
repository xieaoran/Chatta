import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Robert on 2015/5/25.
 */
public class Main {
    public static String UserName;
    public static InetAddress IPAddress;
    public static chatta MainChatta;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chatta");
        MainChatta = new chatta();
        frame.setContentPane(MainChatta.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(new Rectangle(300, 200, 600, 400));
        frame.setResizable(false);
        UserName = JOptionPane.showInputDialog("请输入用户名：");
        String ipAddressStr = JOptionPane.showInputDialog("请输入对方的IP地址：");
        try {
            IPAddress = InetAddress.getByName(ipAddressStr);
            MainChatta.console.append("[信息] 发送端加载成功。\n");
        }
        catch (UnknownHostException e) {
            MainChatta.console.append("[错误] 不存在的IP地址。\n");
        }
        new Thread(new Daemon()).start();
    }
}
