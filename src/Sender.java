import java.io.IOException;
import java.net.*;

public class Sender implements Runnable {
    private String UserName;
    private String Message;
    private byte Buffer[];
    private DatagramSocket Socket;
    private DatagramPacket Packet;
    private InetAddress IPAddress;
    private final int Port = 23333;

    public Sender(String userName, InetAddress ipAddress, String message){
        UserName = userName;
        IPAddress = ipAddress;
        Message = UserName + "：" + message;
    }

    public void run() {
        try {
            Socket = new DatagramSocket();
        }
        catch (SocketException e) {
            Main.MainChatta.console.append("[错误] " + e.getLocalizedMessage() + "\n");
        }
        Buffer = RSAHelper.Encrypt(Message.getBytes());
        Packet = new DatagramPacket(Buffer, Buffer.length , IPAddress , Port);
        try {
            Socket.send(Packet);
            Main.MainChatta.console.append("[发送] " + Message + "\n");
        }
        catch (IOException e) {
            Main.MainChatta.console.append("[错误] 信息发送失败：" + e.getLocalizedMessage() + "\n");
        }
        Socket.close();
    }
}
