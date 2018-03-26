
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileScanner {
    
    private final File file;
    private final ArrayList<String> lines;
    
    public FileScanner(String fname) throws FileNotFoundException, IOException, Exception{
        
        file = new File(fname.concat(".txt"));
        if(!file.exists()){
            throw new Exception("Sorry, your file name is not correct");
        }
        
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        lines = new ArrayList<>();
        
        /// reading the file lines
        String s;
        while((s = reader.readLine()) != null){
            lines.add(s);
        }
    }
    
    public ArrayList getLines(){
        return lines;
    }
    
    
}
