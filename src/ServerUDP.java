
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoServer extends Thread {

    private final DatagramSocket socket;
    private boolean running;
    private final byte[] buf = new byte[256];

    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    @Override
    public void run() {
        running = true;
        try {
            
            PrintWriter output = new PrintWriter(new File("server.txt"));
            while (running) {

                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String recieved = new String(packet.getData(), "UTF-8").trim();
                
                if (recieved.equals("end")) {
                    output.close();
                    output = new PrintWriter(new File(getRandomName()));
                    System.out.println("----------------- File Ended--------------------------");
                }
                else{
                    output.println(recieved);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        socket.close();
    }
    
    public String getRandomName(){
        return "sever" + (int)(Math.random() * 10000);
    }

    public static void main(String[] args) throws SocketException {
        new EchoServer().start();
    }
}
