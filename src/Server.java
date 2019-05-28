
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

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
     * --> If both are same, then it will send positive acknowledgment otherwise it will send negative acknowledgment according to the given format.
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
 
    private final DatagramSocket socket;
    private boolean running;
    private final byte[] buf = new byte[256];
 
    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }
 
    @Override
    public void run() {
        running = true;
        ArrayList<ReceivedData> recLines = new ArrayList<>();
        try{
            while (running) {
                
                // every time the buffer should be empty
                Arrays.fill(buf, (byte)0);
                
                // received the UDP packet
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                // store the received UDP packet into an object
                ReceivedData recieved = new ReceivedData(buf[0], new String(buf, 4, buf[1]), buf[2], buf[3]);
                
                
                // if the check is correct (no error has occured) 
                if(recieved.isRightChecksum()){
                    String fileName = recieved.data;
                    
                    // check if the message is the ending signal from the client side
                    if (fileName.equals("end")) {
                        running = false;
                    }
                    else{
                        // store the message into an array list
                       recLines.add(recieved); 
                    }
                    
                    // 0 for positive ackonwledgement
                    buf[0] = 0;
                }
                else{
                    // 1 for negative acknowledgment
                    buf[0] = 1;
                }
                
                // 2 bytes for checksum
                buf[1] = recieved.check1;
                buf[2] = recieved.check2;
                
                // sending back the acknowledgment to the client
                packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            }
            
            socket.close();
            
            try ( // printing out the received file
                    PrintWriter output = new PrintWriter(new File(recLines.get(0).data + ".txt"))) {
                for(int x = 1; x < recLines.size(); x++){
                    output.println(recLines.get(x).data);
                }
            }
            
        }
        catch(IOException ex){
            ex.printStackTrace();;
        }
        
    }
}

