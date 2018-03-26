
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Test implements Runnable {
    
    
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        (new Thread(new Test())).start();
    }

    @Override
    public void run() {
        while(true){
            System.out.println("Hello world");
        }
        
    }

}
