
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Azargul Nazari
 * CPCS-371 (Computer Networking) Project
 * Date: 2/4/2018
 * 
 * 
 * 
 * The following instruction should be implemented:
 *  --> User will provide the name of the file, IP address and Port number of the server to the client program.
 *  --> Program will read the file line by line and each line will be transferred separately using UDP datagram.
 *  --> For each line, it will calculate the checksum and assign a sequence number. Sequence number will increase linearly. For example, for the first line, the sequence number will be 1, for the second line, it will be 2 and so on. 
 *  --> Program will create an application layer packet according to the following format 
 * 
 *      -----------------------------------------------------------------------
 *      |    1 Byte   |   1 Byte  |  - Variable - byte  |  2 bytes checksum   |
 *      -----------------------------------------------------------------------
 *      |   Seq No    |   Length  |   Length content    |      Checksum       |
 *      -----------------------------------------------------------------------
 * 
 *  --> This packet will be forwarded to the server using UDP/IP protocol.
 *  --> Client will now wait for the application layer acknowledgment. If it received a positive acknowledgment, it will take next line from the fill and forward it to the server according to the abovementioned
        procedure. If the client received a negative acknowledgment, then it will retransmit the same packet again. In short, you need to implement stop-and-wait scenario. 
 *  
 * 
 * 
 * 
 */





public class Client {
    
    //------------------------ properties --------
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    private static byte counter;
    //---------------------------------------------
 
    
    
    
    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        counter = 0;
    }
 
    
    
    
    public void sendEcho(String msg) {
        buf = new byte[msg.length() + 4];
        buf[0] = counter;
        
        buf = msg.getBytes();
        try{
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
 
    public void close() {
        socket.close();
    }
    
    public static void main(String[] args) throws SocketException, UnknownHostException, Exception {
        Client client = new Client();
        FileScanner scan = new FileScanner("test");
        ArrayList<String> lines = scan.getLines();
        for(String l : lines){
            client.sendEcho(l);
        }
    }
    
    
    public static byte[] checksum(byte[] list){
        return null;
    }
    
    public static int getCounter(){
    
    }
}
