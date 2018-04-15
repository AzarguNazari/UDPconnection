/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication2;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    
    //------------- properties ---------------------
    private DatagramSocket socket;
    private boolean running;
    private final byte[] buf = new byte[256];
    private static int type;
    private static File recievedFile;
    private static Scanner input = new Scanner(System.in);
    private static PrintWriter output;
    //----------------------------------------------

    
    //---------------- constructor --------------------- 
    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    
    
    
    @Override
    public void run() {
        running = true;
        try {
            
            // for printing the recieved file
            output = new PrintWriter(new File("server.txt"));
            
            
            while (running) {
                
                //  initialize all the buffer to zero at first
                Arrays.fill(buf, (byte) 0);
                
                // recieved the packet from the client in the buffer array
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                // finding the actual data of the received
                String actualData = new String(messagePart(packet.getData()),  "UTF-8");
                
                // find the checksum of the recieved file
                byte[] checksum = checkSumCalculation(actualData);
                
                // to check whether the check of the reived file is same as the caclculated one
                if(!(buf[2] == checksum[0] && buf[3] == checksum[1])){
                    
                    // send back the negative ackowledgment to the sender 
                    byte[] acknowledgement = new byte[]{1, checksum[0], checksum[1]};
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(acknowledgement, acknowledgement.length, address, port);
                    socket.send(packet);
                }
                
                // if yes then add the line into the file name 
                else{
                    if (actualData.equals("end")) {
                    output.close();
                    output = new PrintWriter(new File(getRandomName()));
                    System.out.println("----------------- File Ended--------------------------");
                    }
                    else{
                        output.println(actualData);
                    }
                    
                    // send back the positive ackowledment to the sender to make sure to send the next line
                    byte[] acknowledgement = new byte[]{0, checksum[0], checksum[1]};  
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(acknowledgement, acknowledgement.length, address, port);
                    socket.send(packet);
                }
                
                

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // close the socket connection
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
    
    /**
     * 
     * @param message the total received packet from the client
     * @return only the content part of the packet which is the original message
     */
    public byte[] messagePart(byte[] message){
        byte[] part = new byte[message.length - 4];
        System.arraycopy(message, 4, part, 0, part.length);
        return part;
    }
    
    /**
     * 
     * @return random file names
     */
    public String getRandomName(){
        return "sever" + (int)(Math.random() * 10000);
    }
    
    
    
    
    public static void main(String[] args) throws SocketException {
        new Server().start();
    }
}
