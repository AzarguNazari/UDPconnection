
import java.io.IOException;


public interface ClientWork {
    
    public void close();
    
    public void sendPacket(String message, byte sequence) throws IOException, InterruptedException;
    
}
