
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Hazar Gul Nazari
 */
public class ClientMain {

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, Exception {

        Scanner input = new Scanner(System.in);

        // asking from the user the file name
        System.out.print("Enter File Name: ");
        String fileName = input.next();

        // initiating the client side connection
        Client client = new Client();

        // array list of file's lines
        ArrayList<String> lines = new FileScanner(fileName).getLines();

        byte squence = 0;

        // the first packet is the file name
        client.sendPacket("server1", squence++);

        // sending all the lines
        for (String s : lines) {
            client.sendPacket(s, squence++);
        }

        // sending the signal to the server for ending the connection
        client.sendPacket("end", squence);

        // closing the client side socket
        client.close();
    }

}
