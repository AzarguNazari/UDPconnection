
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class EchoClient {
    
    private final DatagramSocket socket;
    private final InetAddress address;
    private byte[] buf;
 
    public EchoClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }
 
    public void sendEcho(String msg) {
        
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
        EchoClient client = new EchoClient();
        FileScanner scan = new FileScanner("test");
        ArrayList<String> lines = scan.getLines();
        for(String l : lines){
            client.sendEcho(l);
        }
    }
}
