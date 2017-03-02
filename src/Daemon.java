import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Daemon implements Runnable{

    private byte Buffer[] = new byte[10240];
    private DatagramSocket Socket;
    private DatagramPacket Packet;
    private final int Port = 23333;

    public void run() {
        Packet = new DatagramPacket(Buffer, 0, 10240);
        try {
            Socket = new DatagramSocket(Port);
        }
        catch (SocketException e) {
            Main.MainChatta.console.append("[错误] 23333端口已被占用。\n");
        }
        Main.MainChatta.console.append("[信息] 接收端加载成功，等待消息传入。\n");
        while (true) {
            try {
                Socket.receive(Packet);
            }
            catch (IOException e) {
                Socket.close();
                Main.MainChatta.console.append("[错误] " + e.getLocalizedMessage() + "\n");
            }
            byte[] realData = new byte[Packet.getLength()];
            System.arraycopy(Packet.getData(), 0,realData , 0, Packet.getLength());
            byte[] decodedData = RSAHelper.Decrypt(realData);
            String receivedMessage = new String(decodedData, 0, decodedData.length);
            Main.MainChatta.console.append("[接收] " + receivedMessage + "\n");
        }
    }
}
