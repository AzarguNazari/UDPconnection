
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerUDP extends Thread {

    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    public ServerUDP(int port) throws IOException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(5000);
    }

    @Override
    public void run() {
        PrintWriter fileOutput = null;
        try {
            fileOutput = new PrintWriter(new File("server.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        DatagramPacket packet;
        while (true) {
            try{
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                String message = new String(buf, "UTF-8");
                if(message.equalsIgnoreCase("end")){
                    break;
                }
                System.out.println(message);
                fileOutput.println(message);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        fileOutput.close();
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
