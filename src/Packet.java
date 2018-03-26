public class Packet {
    
    private final byte seqNum;
    private final byte length; 
    private final String line;
    private final byte checksum;
    
    
    public Packet(byte seqNum, byte length, String line, byte checksum){
        this.seqNum = seqNum;
        this.length = length;
        this.line = line;
        this.checksum = checksum;
    }

    public byte getSeqNum() {
        return seqNum;
    }

    public byte getLength() {
        return length;
    }

    public String getLine() {
        return line;
    }

    public byte getChecksum() {
        return checksum;
    }
}
