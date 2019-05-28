
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


public class Client implements ClientWork{
    
    private final DatagramSocket socket;
    private final InetAddress address;
    private byte[] buf;
 
    
    
    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }
 
    
    /**
     * Description: This method is used for sending the message to the serve
     * @param msg the message (line) to be sent
     * @param sequence  the sequence number of the packet
     * @throws IOException in case if the IO exception occur
     * @throws InterruptedException  in case if the Interrupt occur during option
     */
    @Override
    public void sendPacket(String msg, byte sequence) throws IOException, InterruptedException {
        // 4 bytes should be added for head purpose
        buf = new byte[msg.getBytes().length + 4];
        
        // first byte for sequence
        buf[0] = sequence;
        
        // second byte for length
        buf[1] = (byte) msg.length();
        
        // 3th and 4th bytes for checksum purpose
        byte[] checksum = checkSumCalculation(msg);
        buf[2] = checksum[0];
        buf[3] = checksum[1];
        
        // copying the main content into the buffer container to be sent
        System.arraycopy(msg.getBytes(), 0, buf, 4, buf.length - 4);
        
        // packet read to be sent
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
        
        // recieve the acknowledgment
        packet = new DatagramPacket(buf, 3);
        socket.receive(packet);
        
        // if the acknowledgement is 1 means the error has occured so resend the message
        if(buf[0] == 1){
           sendPacket(msg, sequence);
        }
        
    }
 
    /**
     * Description: to close the socket of the client side
     */
    public void close() {
        socket.close();
    }
    
    
    /**
     * Description: This method is used for finding the checksum of the line or message
     * @param message the main message (line)
     * @return two bytes of checksum
     */
    public static byte[] checkSumCalculation(String message){
       byte[] bytes = new byte[2];
       short totalValue = (short) message.charAt(0);
       for(int x = 0; x < message.length(); x++){
           totalValue =  (short) ((totalValue + message.charAt(x)) % 16);
       }
       totalValue = (short) ~totalValue;
       bytes[0] = (byte)(totalValue & 0xff);
       bytes[1] = (byte)((totalValue >> 8) & 0xff);
       return bytes;
    }
}
