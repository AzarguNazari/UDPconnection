
import java.net.SocketException;


/**
 *
 * @author Hazar Gul Nazari
 */
public class ServerMain {

    public static void main(String[] args) throws SocketException {

        Server server = new Server();
        server.start();
        
    }

}
