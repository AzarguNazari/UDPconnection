/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication2;


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
    
    //------------------ Properties --------------------
    private final DatagramSocket socket;
    private final InetAddress address;
    private static byte seqNum;
    private byte[] packet;
    //-------------------------------------------------
 
    
    //-------------- Constructor --------------------------
    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        seqNum = 0;
    }
 
    /**
     * This method is to send the UDP to the server
     * @param msg The line of file to be sent
     * @param sequenceNumber The sequence number to be sent to the server
     */
    //------------- Sending data to the server
    public void sendEcho(String msg, int sequenceNumber) {
        
        // convert the message to bytes
        byte[] buf = msg.getBytes();
        
        // intializing the size of packet
        packet = new byte[msg.length() + 4];
        
        // the first byte is reserved to the 
        packet[0] = (byte) sequenceNumber;
        
        // the second byte is reseved to the message length
        packet[1] = (byte) buf.length;
        
        // the 3th and 4th bytes are reserved to the checksum value
        byte[] checksum = checkSumCalculation(msg);
        packet[2] = checksum[0];
        packet[3] = checksum[1];
        
        // copy the message content into a new array byte
        System.arraycopy(buf, 0, packet, 4, buf.length);
        
        
        try{
            
            // send the packet
            DatagramPacket UDPpacket = new DatagramPacket(packet, packet.length, address, 4445);
            socket.send(UDPpacket);
            
            // recieve the acknowledgment
            byte[] acknowledgement = new byte[3];
            UDPpacket = new DatagramPacket(acknowledgement, acknowledgement.length);
            socket.receive(UDPpacket);
            
            if(acknowledgement[0] == 1){
                sendEcho(msg, sequenceNumber);
            }
            else{
                System.out.println(("Line " + sequenceNumber) + " is sent successfully");
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
 
    /**
     * This method is to close the socket connection
     */
    public void close() {
        socket.close();
    }
    
    
    
    
    /**
     * This method is to calculate the checksum of a specific line of the file
     * @param message the line of file to check it's checksum
     * @return two bytes of checksum
     */
    public static byte[] checkSumCalculation(String message){
       byte[] bytes = new byte[2];
       short first = (short) message.charAt(0);
       for(int x = 0; x < message.length(); x++){
           first = (short) ((first + message.charAt(x)) % 16);
       }
       bytes[0] = (byte)(first & 0xff);
       bytes[1] = (byte)((first >> 8) & 0xff);
       return bytes;
    }
    
    
    public static void main(String[] args) throws SocketException, UnknownHostException, Exception {
        
        
        Client client;
        FileScanner scan;       
        ArrayList<String> lines;
        Scanner input = new Scanner(System.in);
        
        
        
        outerloop:
        while(true){
            
            // This display message for the user
            System.out.println("------------------------------");
            System.out.println("1 > For Sending File");
            System.out.println("2 > For Exit");
            System.out.println("------------------------------");
            System.out.print("Please Insert an Option >> ");
            
            
            int option = input.nextInt();
            
            // entered options from the user
            switch(option){
                
                /* Sending message option */
                case 1:
                    
                    // Insert the file name without extension
                    System.out.print("Insert Your File Name, Example (test1, test2): ");
                    String fileName = input.next();
                    
                    // Initializing client object
                    client = new Client();
                    
                    // converting file into array of lines
                    lines = new FileScanner(fileName).getLines();    
                    
                    client.sendEcho(fileName, seqNum++);
                    for(String line : lines){
                        client.sendEcho(line, seqNum++);
                    }
                    client.close();
                    break;
                    
                case 2:
                    break outerloop;
                    
                default:
                    System.out.println("Sorry! You have to enter either 1 or 2");
            }
            
        }
        
        
       
    }
}
