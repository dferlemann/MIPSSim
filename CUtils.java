
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
	
	public String hex2binary(String hexStr)
	{
		int decimal = Integer.parseInt(hexStr, 16);
		return Integer.toString(decimal, 2);
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
