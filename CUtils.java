
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;


// Common Utilities
public class CUtils {

	public void println(Object line) 
	{
	    System.out.println(line);
	}
	
	public String binary2hex(String binaryStr)
	{
		int decimal = Integer.parseInt(binaryStr, 2);
		return Integer.toString(decimal, 16);
	}
	
	public String hex2binStr32(String hexStr)
	{
		long decimal = Long.parseLong(hexStr, 16);
		String binStr = String.format("%32s", Long.toBinaryString(decimal)).replace(' ', '0');
		//println("Binary String: " + binStr);
		
		return binStr;//String.format("%1$-32s", Integer.toString(decimal, 2));
	}
	
	public String binStr32ToHexString(String bin)
	{
		Long temp = Long.parseLong(bin, 2); //bin.substring(bin.length()-32, bin.length())
		return String.format("%8s", Long.toHexString(temp)).replace(' ', '0');
	}
	
    // Returns signed 16-bit binary string from int
    public String intToSigned16Bin(int dec) 
    {
        String bin = Integer.toBinaryString(dec);

        int l = bin.length();
        if (l < 16 && dec >= 0) {
            for (int i = 0; i < (16 - l); i++) {
                bin = "0" + bin;
            }
        } else if (dec < 0) {
            bin = bin.substring(l - 16);
        }

        return bin;
    }
    
    public int binStr16toSignedInt(String binStr16)
    {
    	return (short)Integer.parseInt(binStr16, 2);
    }
    
    public int binStr32toSignedInt(String binStr32)
    {
    	return (int)Long.parseLong(binStr32, 2);
    }
	
	public <T, E> T getKeyByValue(Map<T, E> map, E value) 
	{
	    for (Entry<T, E> entry : map.entrySet()) 
	    {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
