
// Common Utilities
public class CUtils {

	public static void println(Object line) 
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
}
