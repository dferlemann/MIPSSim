import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// Main entrance to the program
/* Stores instructions, data to memory
 * Interprets the instructions
 * Executes and records the process
 */
public class Simulator {
	public static boolean DEV_MODE = true;
	
	FileParser filep;
	private Memory m;
	private Register reg;
	Decode decoder;
	private CUtils ut;
	
	// Program 
	private File input_file;
	private ArrayList<String> instrContent, dataContent;
	private String startAddr, instrAddr, dataAddr;

	
	HashMap<String,InstructionFormat> instrsFmt;
	HashMap<String, String> instrsAsm, // assembly names
							currentInstrFields,
							instrsType; 
	
	// Execution parameters
	int instr_num;
	private Long pc; // contain address of next instruction
	private String nextInstruction; // contain 8 bytes hex representation of the instruction from memory
	
	String currentInstr_bin;
	String currentInstrAsmName;
	
	// metrics
	private int numInstrExecuted;
	private int numClkCyclesUsed;
	private int numInstructions_load;
	private int numInstructions_store;
	private int numInstructions_logical;
	private int numInstructions_arithmetic;
	private int numInstructions_control;
	
	// ================================================================================== Constructor
	public Simulator(File file)
	{
		// Initializations
		m = new Memory();
		reg = new Register();
		decoder = new Decode();
		
		ut = new CUtils();
		input_file = file;
		
		numInstrExecuted = 0;
		numClkCyclesUsed = 0;
		numInstructions_load = 0;
		numInstructions_store = 0;
		numInstructions_logical = 0;
		numInstructions_arithmetic = 0;
		numInstructions_control = 0;
		
		// ---------------------------------------------------------------------------------- START
		// Interpret the input file and store instruction and data in different array lists
		parseFile();
		
		// Store content into different segments of the memory
		fileToMemory();
		
		// Initialize SP, GP
		reg.setRegValByReg("$29", "11111111111111111111111111111111"); // Stack pointer
		// reg.setRegValByReg("$28", ""); // GP - no idea
		
		
		// Have decoding information ready
		instrsAsm = decoder.getInstrs();
		instrsFmt = decoder.GetInstrsFormatMasks();
		instrsType = decoder.GetInstrsTypes();
		currentInstrFields = new HashMap<String, String>();

		// Start the loop from the first instruction
		instr_num = 0;
		pc = Long.parseLong(startAddr, 16);
		while(instr_num < instrContent.size())
		{
			// ----------------------------------------------------------------------------------------------- FETCH
			nextInstruction = fetchInstruction(pc);
			// -------------- Decode Instruction
			// -- step 1: convert it to 32-bit binary string
			currentInstr_bin = hex2binaryInstruction(nextInstruction);
			
			// -- step 2: get opcode
			String opcode = currentInstr_bin.substring(0, 6);
			
			// -- step 3: get ISR format of current instruction
			currentInstrAsmName = ut.getKeyByValue(instrsAsm, opcode);
			InstructionFormat currentFmt = instrsFmt.get(currentInstrAsmName);
			currentInstrFields = currentFmt.format(currentInstr_bin);
			
			
			// -------------------------------------------------------------------------------------------- EXECUTE
			try
			{
				// DEF: updateMetrics(.......)
//				numInstrExecuted += a;
//				numClkCyclesUsed += b;
//				numInstructions_load += c;
//				numInstructions_store += d;
//				numInstructions_logical += e;
//				numInstructions_arithmetic += f;
//				numInstructions_control += g;
				
				if (DEV_MODE=true) printEverything();
				
				switch(instrsType.get(currentInstrAsmName))
				{
					case "load": 		udpateMetrics(0, 0, 1, 0, 0, 0, 0); break;
					case "store": 		udpateMetrics(0, 0, 0, 1, 0, 0, 0); break;
					case "logical": 	udpateMetrics(0, 0, 0, 0, 1, 0, 0); break;
					case "arithmetic": 	udpateMetrics(0, 0, 0, 0, 0, 1, 0); break;
					case "control":		udpateMetrics(0, 0, 0, 0, 0, 0, 1); break;
					case "syscall":		udpateMetrics(0, 0, 0, 0, 0, 0, 0); break;
					default: throw new DebugException("Error - Instruction Type not found.");
				}
				
				executeInstruction();
			} 
			catch (DebugException ex)
			{
				ut.println(ex);
				ut.println("Instruction: " + nextInstruction);
				ex.printStackTrace();
				
			}
			
			instr_num ++;

				
		}
		
		printMetrics();
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
		
		String datastr;
		byte[] databytes = new byte[4];
		
		// We want it to be byte addressable
		int i = 0;
		long pc = text_addr;
		while(i < instrContent.size()) //pc < (text_addr + instrContent.size()*4)
		{
			datastr = instrContent.get(i); // this is instruction 32-bit, 4 bytes in hex form
			// break it down to 1 byte each element in databytes
			// ut.println("\n========" + datastr + "\n");
			
			databytes[0] = (byte)Integer.parseInt(datastr.substring(0, 2), 16);
			databytes[1] = (byte)Integer.parseInt(datastr.substring(2, 4), 16);
			databytes[2] = (byte)Integer.parseInt(datastr.substring(4, 6), 16);
			databytes[3] = (byte)Integer.parseInt(datastr.substring(6, 8), 16);
			
			for(byte databyte : databytes) // each address will address each byte
			{
				m.mem.put(pc, databyte);
				//ut.println(String.format("%8s", Long.toHexString(pc)).replace(' ', '0') + " " + String.format("%2s", Integer.toHexString( m.mem.get(pc)& 0xFF) ).replace(' ', '0'));
				pc++;
			}
			i++;
		}
		
		// For data segment
		i = 0;
		long gp = stat_data_addr;
		while(i < dataContent.size())
		{
			datastr = dataContent.get(i); // this is data 8-bit, 1 bytes in hex form
			// break it down to 1 byte each element in databytes
			byte databyte = (byte)Integer.parseInt(datastr, 16);
			m.mem.put(gp, databyte);
			//ut.println(String.format("%8s", Long.toHexString(gp)).replace(' ', '0') + " " + String.format("%2s", Integer.toHexString( m.mem.get(gp)& 0xFF) ).replace(' ', '0'));
			gp++;
			i++;
		}
	}
	
	private String hex2binaryInstruction(String instr_hex)
	{
		String binary;
		binary = ut.hex2binary(instr_hex);
		if (binary.length() != 32)
			binary = String.format("%32s", binary).replace(' ', '0');
		return binary;
	}
	
	private String fetchInstruction(Long addr)
	{
		//ut.println("==========================");
		String instruction = "";
		Long currentAddr = addr;
		for(int i = 0; i < 4; i++)
		{
			//ut.println(String.format("%8s", Long.toHexString(currentAddr)).replace(' ', '0')  + " " + String.format("%2s", Integer.toHexString( m.mem.get(currentAddr)& 0xFF)).replace(' ', '0') );
			instruction += String.format("%2s", Integer.toHexString( m.mem.get(currentAddr)& 0xFF) ).replace(' ', '0');
			currentAddr += 1;
		}
		
		return instruction;
	}
	
	// ------------------------------------------------------------- execution
	private void executeInstruction() throws DebugException
	{
		numInstrExecuted += 1;
		switch(currentInstrAsmName)
		{
			case "addi": addi(); numClkCyclesUsed += 1; break;
			case "sll":   sll(); numClkCyclesUsed += 1; break;
			case "bne":   bne(); numClkCyclesUsed += 1; break;
			
			default: throw new DebugException("Error! Instruction not listed.");
			
		}
	}
	

	// -------------------------------------------------------------  metrics
	private void udpateMetrics(int a, int b, int c, int d, int e, int f, int g)
	{
		numInstrExecuted += a;
		numClkCyclesUsed += b;
		numInstructions_load += c;
		numInstructions_store += d;
		numInstructions_logical += e;
		numInstructions_arithmetic += f;
		numInstructions_control += g;
	}
	
	private void printMetrics()
	{
		ut.println("===================== Execution Complete ============================");
		
		ut.println("Number of instructions executed:   " + numInstrExecuted);
		ut.println("Number of clock cycles used:       " + numClkCyclesUsed);
		ut.println("Number of Load instructions:       " + numInstructions_load);
		ut.println("Number of Store instructions:      " + numInstructions_store);
		ut.println("Number of Logical instructions:    " + numInstructions_logical);
		ut.println("Number of Arithmetic instructions: " + numInstructions_arithmetic);
		ut.println("Number of Control instructions:    " + numInstructions_control);
		
		ut.println("Register Content: ---------------------------------------------------");
		reg.printAllRegisters();
		ut.println("=====================================================================");
	}
	// =============================================================================================== Instruction Functions
	private String rtStr, rsStr, rdStr, saStr;
	private String constStr;

	private int rt, rs, rd;
	private int cons, sa;
	
	private void addi() 
	{
		loadIType();
		rt = rs + cons;
		reg.setRegValByBin(rtStr, intToBinStr32Len(rt));
		pc += 4;
	}
	private void sll()
	{
		loadRType();
		rd = rt << sa;
		reg.setRegValByBin(rdStr, intToBinStr32Len(rd));
		pc += 4;
	}
	private void bne()
	{
		loadIType();
		if (rt != rs)
			pc = pc << cons;
		
	}
	
	// ========================================================== Support 	
	private void loadIType()
	{
		// opcode 6, rs 5, rt 5, immediate(const) 16
		
		rtStr = currentInstrFields.get("rt"); // reg binary definition
		rsStr = currentInstrFields.get("rs"); // reg binary definition
		constStr = currentInstrFields.get("const"); // value
		
		rt = Integer.parseInt(reg.getRegValByBin(rtStr), 2);
		rs = Integer.parseInt(reg.getRegValByBin(rsStr), 2);
		cons = Integer.parseInt(constStr, 2);
		
		if (DEV_MODE = true)
		{
		ut.println("Opcode=" + currentInstrAsmName + 
				", Rs->" + reg.getRegByBin(rsStr) + 
				", Rt->" + reg.getRegByBin(rtStr) + 
				", const->" + Integer.parseInt(constStr, 2));
		}
	}
	private void loadRType()
	{
		rtStr = currentInstrFields.get("rt"); // reg binary definition
		rsStr = currentInstrFields.get("rs"); // reg binary definition
		rdStr = currentInstrFields.get("rd"); // reg binary definition
		saStr = currentInstrFields.get("sa"); // Shift amount value (6-bit)
		
		rt = Integer.parseInt(reg.getRegValByBin(rtStr), 2); // 32-bit value i
		rs = Integer.parseInt(reg.getRegValByBin(rsStr), 2);
		rd = Integer.parseInt(reg.getRegValByBin(rdStr), 2);
		sa = Integer.parseInt(constStr, 2);
		
		if (DEV_MODE = true)
		{
			ut.println("Opcode=000000" +
					", Rs->" + reg.getRegByBin(rsStr) + 
					", Rt->" + reg.getRegByBin(rtStr) + 
					", Rd->" + reg.getRegByBin(rdStr) + 
					", sa->" + reg.getRegByBin(saStr) +
					", funct->" + currentInstrAsmName );
		}
	}
	
	private String intToBinStr32Len(Integer bin)
	{
		return String.format("%32s", Integer.toBinaryString(bin)).replace(' ', '0');
	}
	

	private void printEverything()
	{
		ut.println("============ INSTR #" + instr_num + " =============================");
		ut.println("PC=" + String.format("%8s", Long.toHexString(pc)).replace(' ', '0'));
		ut.println("Instruction=" + nextInstruction);
		
		reg.printAllRegisters();
	}
	

}
