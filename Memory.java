
import java.util.LinkedHashMap;

public class Memory {
	
	private int space_low = 1048576; // 1MB on top
	private int space_hi = 1048576;  // 1MB on bottom
	private int cache_size = 16384;  // 16K cache
	
	protected LinkedHashMap<Long, Byte> mem = new LinkedHashMap<Long, Byte>();
	protected LinkedHashMap<Long, Byte> cache = new LinkedHashMap<Long, Byte>();
	
	public Memory()
	{
		init();
	}
	
	private void init()
	{
		// allocating lower addresses
		for(long addr = 0; addr < space_low; addr++)
		{
			mem.put(addr, (byte)0);
		}
		
		// allocating higher addresses
		long highest_addr = Long.parseLong("FFFFFFFF", 16);
		
		for(long addr = (highest_addr-space_hi); addr <= highest_addr; addr++ )
		{
			mem.put(addr, (byte)0);
		}
		
		// allocating cache
		for(long addr = 0; addr < cache_size; addr++)
		{
			cache.put(addr, (byte)0);
		}
		
	}
	
}
