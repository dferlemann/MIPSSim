

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
//import java.util.List;
import java.util.regex.Pattern;


// FileParser takes in the machine code file and produces:
// 	- 2 ArrayList for instructions and data
//  - 3 memory addresses for start, instr, and data
public class FileParser {

	private File asm_file;
	private ArrayList<String> fileContent, instrContent, dataContent;
	private String startAddr, instrAddr, dataAddr;
	
	
	// --------------------------------------------------------------- Properties
	public void setFile(File file)
	{
		asm_file = file;
	}
	public File getFile()
	{
		return asm_file;
	}
	
	public String getFileName()
	{
		return asm_file.getName();
	}
	public ArrayList<String> getFileContent()
	{
		return fileContent;
	}
	public ArrayList<String> getInstrContent()
	{
		return instrContent;
	}
	public ArrayList<String> getDataContent()
	{
		return dataContent;
	}
	
	public String getStartAddr()
	{
		return startAddr;
	}
	public String getInstrAddr()
	{
		return instrAddr;
	}
	public String getDataAddr()
	{
		return dataAddr;
	}
	// ----------------------------------------------------------- Constructor
	
	public FileParser(File file)
	{
		fileContent = new ArrayList<String>(); // initialize ArrayList
		instrContent = new ArrayList<String>();
		dataContent = new ArrayList<String>();
		
		this.setFile(file);
		linesToArrayList(); // convert each line in the file to a String and store in a list
		
		try
		{
			linesToInstrData();
		}
		catch (DebugException ex)
		{
			println(ex);
			ex.printStackTrace();
		}
		
	}
	
	
	// ----------------------------------------------------------- Local Methods
	
	
	// This method stores all lines into an Arraylist fileContent
	private void linesToArrayList()
	{
		Scanner s;
		try 
		{
			
			File file = getFile();
			s = new Scanner(file);
			
			while (s.hasNextLine())
			{
				String nextLine = s.nextLine();
				nextLine.trim(); // remove leading and trailing whitespaces
				
				if (!(nextLine.length() == 0))
				{
					fileContent.add(nextLine);
				} 
			}
				
			s.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// This method identifies and stores instrs and data to their appropriate ArrayList
	// It also identifies starting address, instruction address, and data address in memory.
	private void linesToInstrData() throws DebugException
	{
		// for validating lines in instrContent, dataContent
		long numInstr = 0, dataLength = 0; 
		// This decides next String in the array belongs to which ArrayList
		int currentState = -1; // start = 0, instr = 1, data = 2
		
		for (String line : fileContent)
		{
			
			// make all lower case
			line = line.toLowerCase();
			// remove the "," after the instructions
			if (line.endsWith(","))
				line = line.substring(0, line.length()-1);
			
			
			if (line.startsWith("start"))
			{
				String addr = line.substring(6, 14);
				if (!addr.matches("^[0-9A-F]+$")) // address should be in hex format
				{
					throw new DebugException("Error - Parsed start address format is incorrect!");
				}
				startAddr = addr;
				currentState = 0;
			} 
			else if (line.startsWith("data"))
			{
				// GET THE ADDRESS
				String addr = line.substring(5, 13);
				if (!addr.matches("^[0-9A-F]+$")) // address should be in hex format
				{
					throw new DebugException("Error - Parsed data address format is incorrect!");
				}
				dataAddr = addr;
				
				// GET THE DATA LENGTH
				Pattern pattern = Pattern.compile("(\\d+)(?!.*\\d)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.find())
					dataLength = Long.parseLong(matcher.group(0));
				
				currentState = 2;
				//println(dataLength);
			}
			else if (line.startsWith("instr"))
			{
				// GET THE ADDRESS
				String addr = line.substring(6, 14);
				if (!addr.matches("^[0-9A-F]+$")) // address should be in hex format
				{
					throw new DebugException("Error - Parsed instr address format is incorrect!");
				}
				instrAddr = addr;
				
				// GET THE DATA LENGTH
				Pattern pattern = Pattern.compile("(\\d+)(?!.*\\d)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.find())
					numInstr = Long.parseLong(matcher.group(0));
				
				currentState = 1;
			}
			else
			{
				if (currentState == 1)
				{
					if (line.length() != 8 && !line.matches("[0-9a-fA-F]+"))
					{
						throw new DebugException("Error - instruction format is not correct!");
					} 
					else 
					{
						instrContent.add(line);
					}
				}
					
				else if (currentState == 2)
				{
					String[] data_lines = line.split(",");
					for(String data_line : data_lines)
					{
						if (data_line.length() != 2 && !data_line.matches("[0-9a-fA-F]+"))
						{
							throw new DebugException("Error - data format is not correct!");
						} 
						else 
						{
							dataContent.add(data_line);
						}
					}
					
				}
			}
		}
		
		// Validate the number of lines
		if (numInstr != instrContent.size())
		{
			throw new DebugException("Error - number of instructions does not match description!");
		}
		if (dataLength != dataContent.size())
		{
			throw new DebugException("Error - data length does not match description!");
		}
	}
	
	// ------------------------------------------------------------ Supporting Methods
	public void PrintContent()
	{
		
		println("start addr = " + startAddr);
		println("instr addr = " + instrAddr);
		println("data addr = " + dataAddr);
		
		println("\nFile Content: ");
		for (String line : fileContent)
		{
			println(line);
		}
		println("\nInstruction Content: ");
		for (String line : instrContent)
		{
			println(line);
		}
		println("\nData Content: ");
		for (String line : dataContent)
		{
			println(line);
		}
	}
	
	public static void println(Object line) 
	{
	    System.out.println(line);
	}
}
