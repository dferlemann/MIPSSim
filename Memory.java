
import java.util.LinkedHashMap;

public class Memory {
	
	private int space_low = 1048576; // 4MB
	private int space_hi = 1048576; // 4MB
	private CUtils ut = new CUtils();
	
	protected LinkedHashMap<Long, Byte> mem = new LinkedHashMap<Long, Byte>();
	
	public Memory()
	{
		init();
	}
	
	private void init()
	{
		for(long addr = 0; addr < space_low; addr++)
		{
			mem.put(addr, (byte)0);
		}
		
		
		long highest_addr = Long.parseLong("FFFFFFFF", 16);
		
		for(long addr = (highest_addr-space_hi); addr <= highest_addr; addr++ )
		{
			mem.put(addr, (byte)0);
		}
	}
	
}
