
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class clientUDP {

    private DatagramSocket socket;
    private InetAddress address;

    public clientUDP() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void send(ArrayList<String> file) throws IOException {
        for (String s : file) {
            byte[] line = s.getBytes();
            System.out.println(s);
            DatagramPacket packet = new DatagramPacket(line, line.length, address, 9000);
            socket.send(packet);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {

        /*------------------- File choosing -------------- */
        Scanner input = new Scanner(System.in);
        System.out.print("Type the name of file example(test1, test2): ");
        String fileName = input.next();

        FileScanner scan = new FileScanner(fileName);

        scan.getLines().add("end");
        new clientUDP().send(scan.getLines());

    }
}
