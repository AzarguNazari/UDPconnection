
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP extends Thread {

    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    public ServerUDP(int port) throws IOException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(50000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                String message = new String(packet.getData(), "UTF-8");
                if(message.equals("end")) break;
                
                System.out.println(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        socket.close();
    }

    public static void main(String[] args) {
        int port = 9000;
        try {
            Thread t = new ServerUDP(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
