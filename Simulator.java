import java.io.File;
import java.util.ArrayList;

// Main entrance to the program
/* Stores instructions, data to memory
 * Interprets the instructions
 * Executes and records the process
 */
public class Simulator {
	
	private Memory m;
	FileParser filep;
	private MachineCodeParser codep;
	private CUtils ut;
	
	private File input_file;
	private ArrayList<String> instrContent, dataContent;
	private String startAddr, instrAddr, dataAddr;
	
	public Simulator(File file)
	{
		// Initializations
		m = new Memory();
		ut = new CUtils();
		input_file = file;
		
		
		// ---------------------------------------------- START
		// Break file sections into different parts
		parseFile();
		
		// Store content into the memory
		fileToMemory();
		printMemory();
		
		// Make sense of the instructions
		// codep = new MachineCodeParser(instrContent);
	}
	
	private void parseFile()
	{
		filep = new FileParser(input_file);
		startAddr = filep.getStartAddr();
		instrAddr = filep.getInstrAddr();
		dataAddr = filep.getDataAddr();
		instrContent = filep.getInstrContent();
		dataContent = filep.getDataContent();
	}
	
	private void fileToMemory()
	{
		Integer text_addr = Integer.parseInt(instrAddr, 16);
		Integer stat_data_addr = Integer.parseInt(dataAddr, 16);
		
		Long data = (long)0;
		
		int i = 0;
		for(long pc = text_addr; pc < (text_addr + instrContent.size()*4); pc += 4)
		{
			data = Long.parseLong(instrContent.get(i), 16);
			//ut.println(pc + " " + data);
			m.mem.put(pc, data);
			i++;
		}
		
		i = 0;
		for(long gp = stat_data_addr; gp < (stat_data_addr + dataContent.size()*2); gp += 2)
		{
			data = Long.parseLong(dataContent.get(i), 16);
			//ut.println(gp + " " + data);
			m.mem.put(gp, data);
			i++;
		}
	}
	
	// Debugging
	private void printMemory()
	{
		Long text_addr = Long.parseLong(instrAddr, 16);
		Long stat_data_addr = Long.parseLong(dataAddr, 16);
		
		ut.println("Instr Addr = " + instrAddr);
		
		ut.println("Data Addr  = " + dataAddr);
		
		ut.println("Text: ");
		
		for(long pc = text_addr; pc < (text_addr + instrContent.size()*4); pc += 4)
		{
			ut.println(String.format("%8s", Long.toHexString(pc)).replace(' ', '0') + " " + String.format("%8s", Long.toHexString(m.mem.get(pc))).replace(' ', '0'));
		}
		
		//binary = String.format("%32s", binary).replace(' ', '0');
		
		ut.println("Static Data: ");
		for(long gp = stat_data_addr; gp < (stat_data_addr + dataContent.size()*2); gp += 2)
		{
			ut.println(String.format("%8s", Long.toHexString(gp)).replace(' ', '0') + " " + String.format("%2s", Long.toHexString(m.mem.get(gp))).replace(' ', '0'));
		}
		
	}
}
