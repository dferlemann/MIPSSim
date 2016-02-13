
import java.util.LinkedHashMap;

public class Memory {
	
	private int space_low = 1048576; // 4MB
	private int space_hi = 1048576; // 4MB
	
	protected LinkedHashMap<Long, Long> mem = new LinkedHashMap<Long, Long>();
	
	public Memory()
	{
		init();
	}
	
	private void init()
	{
		for(long addr = 0; addr < space_low; addr++)
		{
			mem.put(addr, (long)0);
		}
		
		
		long highest_addr = Long.parseLong("FFFFFFFF", 16);
		
		for(long addr = (highest_addr-space_hi); addr <= highest_addr; addr++ )
		{
			mem.put(addr, (long)0);
		}
	}
}
