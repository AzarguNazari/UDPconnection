
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;


    /**
     * 
     * @author Azargul Nazari
     * CPCS-371 (Computer Networking) Project
     * Date: 2/4/2018
     * 
     * 
     * 
     * 
     *     --------------------  Explanation ------------------------------------------
     * 
     * --> Server node will receive the packet. 
     * --> Extract the line contents from the packet and then it will recompute the checksum for a line present in the packet
     * --> It will compare the recomputed checksum and the checksum present in the packet. 
     * --> If both are same, then it will send positive acknowledgment otherwise it will send negative acknowledgmen according to the given format.
     * 
     *  ----------------------------------
     *  |     1 byte    |    2 bytes     |
     *  ----------------------------------
     *  |     Type      |   Checksum     |
     *  ----------------------------------
     * 
     * --> Set Type = 0 for positive acknowledgement and Set Type = 1 for negative acknowledgement.
     * --> When all packets are received correctly, the server will arrange the lines in sequence and save it in the file.
     * 
     * 
     * 
     * 
     * 
     */





public class Server extends Thread {

    
    //------------- properties ---------------------
    private DatagramSocket socket;
    private boolean running;
    private final byte[] buf = new byte[256];
    private static int type;
    private static File recievedFile;
    private static Scanner input = new Scanner(System.in);
    private static PrintWriter output;
    //----------------------------------------------

    public Server() throws SocketException {
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

    public static boolean checkSum(byte[] data){
        return false;
    }
    
    public static void main(String[] args) throws SocketException {
        new Server().start();
    }
}
