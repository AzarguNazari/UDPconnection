/**
 * @author Azargul Nazari
 * CPCS-371 (Computer Networking) Project
 * Date: 2/4/2018
 * Description: This object is a helper class for storing the received message's content
 */
public class ReceivedData{
    
    byte sequence;
    String data;
    byte check1, check2;
    
    
    ReceivedData(byte seq, String data, byte check1, byte check2){
        sequence = seq;
        this.data = data;
        this.check1 = check1;
        this.check2 = check2;
    }
    
    /**
     * Description: To check whether the received checksum is equal to the calculated checksum of the content
     * @return if the check is equal then return true otherwise false
     */
    public boolean isRightChecksum(){
        byte[] checksum = checkSumCalculation(data);
        return checksum[0] == check1 && checksum[1] == check2;
    }
    
    @Override
    public String toString(){
        return sequence + "  " + data + "  " + check1 + " " + check2;
    }
    
    /**
     * Description: This method is used for finding the checksum of the line or message
     * @param message the main message (line)
     * @return two bytes of checksum
     */
    private static byte[] checkSumCalculation(String message){
       byte[] bytes = new byte[2];
       short totalValue = (short) message.charAt(0);
       for(int x = 0; x < message.length(); x++){
           totalValue =  (short) ((totalValue + message.charAt(x)) % 16);
       }
       totalValue = (short) ~totalValue;
       bytes[0] = (byte)(totalValue & 0xff);
       bytes[1] = (byte)((totalValue >> 8) & 0xff);
       return bytes;
    }
   
}