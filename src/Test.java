
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String name = "Azargul";
        byte[] bname = name.getBytes();
        
        System.out.println(new String(bname, "UTF-8"));
        
        
    }

}
