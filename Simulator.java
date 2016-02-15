import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

// Main entrance to the program
/* Stores instructions, data to memory
 * Interprets the instructions
 * Executes and records the process
 */
public class Simulator 
{
	public boolean DEV_MODE = true;
	
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
	HashMap<String, String> instrsAsmOpcode, // assembly names
							instrsAsmFunct,
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
		instrsAsmOpcode = decoder.getInstrsAsmOpcode();
		instrsAsmFunct = decoder.getInstrsAsmFunct();
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
			
			if(DEV_MODE) 
			{
				ut.println("============ INSTR #" + instr_num + " =============================");
				ut.println("PC=" + String.format("%8s", Long.toHexString(pc)).replace(' ', '0'));
				ut.println("INSTR_HEX=" + nextInstruction);
				ut.println("INSTR_BIN=" + currentInstr_bin);
			}
			
			
			// -- step 3: get ISR format of current instruction
			if (Integer.parseInt(opcode) == 0)
			{
				String funct = currentInstr_bin.substring(26, 32);
				currentInstrAsmName = ut.getKeyByValue(instrsAsmFunct, funct);
				if(DEV_MODE) 
				{
					ut.println("Opcode: 000000    Funct: " + currentInstr_bin + "    ASM: " + currentInstrAsmName);
				}
			} 
			else
			{
				currentInstrAsmName = ut.getKeyByValue(instrsAsmOpcode, opcode);
				if(DEV_MODE) 
				{
					ut.println("Opcode: " + currentInstr_bin.substring(0, 6) + "    ASM: " + currentInstrAsmName);
				}
			}
			//ut.println(currentInstr_bin);
			InstructionFormat currentFmt = instrsFmt.get(currentInstrAsmName);
			currentInstrFields = currentFmt.format(currentInstr_bin);
			
			
			// -------------------------------------------------------------------------------------------- EXECUTE
			try
			{
				// DEF: updateMetrics(.......)
				// numInstrExecuted       numClkCyclesUsed          numInstructions_load  
				// numInstructions_store  numInstructions_logical   numInstructions_arithmetic   numInstructions_control
				
				
				
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
				
				if (DEV_MODE == true) 
				{
					ut.println("\nRegister Content: -------------------------------\n ");
					reg.printAllRegisters();
				}
					
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
		binary = ut.hex2binStr32(instr_hex);
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
		numClkCyclesUsed += 3;
		return instruction;
	}
	
	// ------------------------------------------------------------- execution
	private void executeInstruction() throws DebugException
	{
		switch(currentInstrAsmName)
		{
			case "add": 	add(); numClkCyclesUsed += 1; break;
			case "addu":   addu(); numClkCyclesUsed += 1; break;
			case "and":   	and(); numClkCyclesUsed += 1; break;
			case "jr": 		 jr(); numClkCyclesUsed += 1; break;
			case "nor":   	nor(); numClkCyclesUsed += 1; break;
			case "or":   	 or(); numClkCyclesUsed += 1; break;
			case "slt": 	slt(); numClkCyclesUsed += 1; break;
			case "sltu":   sltu(); numClkCyclesUsed += 1; break;
			case "sll":   	sll(); numClkCyclesUsed += 1; break;
			case "srl": 	srl(); numClkCyclesUsed += 1; break;
			case "sub":    	sub(); numClkCyclesUsed += 1; break;
			case "subu":   subu(); numClkCyclesUsed += 1; break;
			
			case "addi":   addi(); numClkCyclesUsed += 1; break;
			case "addiu": addiu(); numClkCyclesUsed += 1; break;
			case "andi":   andi(); numClkCyclesUsed += 1; break;
			case "beq": 	beq(); numClkCyclesUsed += 1; break;
			case "bne":   	bne(); numClkCyclesUsed += 1; break;
			case "lbu":   	lbu(); numClkCyclesUsed += 1; break;
			case "lb":   	 lb(); numClkCyclesUsed += 1; break;
			case "lhu": 	lhu(); numClkCyclesUsed += 1; break;
			case "lh":		 lh(); numClkCyclesUsed += 1; break;
			case "ll":   	 ll(); numClkCyclesUsed += 1; break;
			case "lui":   	lui(); numClkCyclesUsed += 3; break;
			case "lw": 		 lw(); numClkCyclesUsed += 1; break;
			case "ori":   	ori(); numClkCyclesUsed += 1; break;
			case "slti":   slti(); numClkCyclesUsed += 3; break;
			case "sltiu": sltiu(); numClkCyclesUsed += 3; break;
			case "sb":   	 sb(); numClkCyclesUsed += 3; break;
			case "sc":   	 sc(); numClkCyclesUsed += 3; break;
			case "sh":   	 sh(); numClkCyclesUsed += 3; break;
			case "sw":   	 sw(); numClkCyclesUsed += 3; break;
			
			case "j":   	  j(); numClkCyclesUsed += 1; break;
			case "jal":   	jal(); numClkCyclesUsed += 1; break;
			case "syscall": syscall(); numClkCyclesUsed += 1; break;
			
			default: throw new DebugException("Error! Instruction not listed.");
		}
		
		numInstrExecuted += 1;
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
		ut.println("=====================================================================");
		ut.println("===================== Execution Complete ============================");
		ut.println("=====================================================================");
		
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
		ut.println("=====================================================================");
		ut.println("=====================================================================");
	}
	
	// ========================================================== Support 	
	private String rtStr, rsStr, rdStr, saStr;
	private String constStr, targetStr;

	private long rt, rs, rd;
	private long target;
	private short immedi_unsigned, immedi_signed, sa;
	
	private void loadRType()
	{
		rtStr = currentInstrFields.get("rt"); // reg binary definition
		rsStr = currentInstrFields.get("rs"); // reg binary definition
		rdStr = currentInstrFields.get("rd"); // reg binary definition
		saStr = currentInstrFields.get("sa"); // Shift amount value (6-bit)
		
		rt = Long.parseLong(reg.getRegValByBin(rtStr), 2) & 0xffffffff; // 32-bit value i
		rs = Long.parseLong(reg.getRegValByBin(rsStr), 2) & 0xffffffff;
		rd = Long.parseLong(reg.getRegValByBin(rdStr), 2) & 0xffffffff;
		sa = (short)Integer.parseInt(saStr, 2);
		
		if (DEV_MODE == true)
		{
			ut.println("Opcode=000000" +
					", Rs->" + reg.getRegByBin(rsStr) + 
					", Rt->" + reg.getRegByBin(rtStr) + 
					", Rd->" + reg.getRegByBin(rdStr) + 
					", sa->" + reg.getRegByBin(saStr) +
					", funct->" + currentInstrAsmName );
		}
	}
	private void loadIType()
	{
		// opcode 6, rs 5, rt 5, immediate(const) 16
		
		rtStr = currentInstrFields.get("rt"); // reg binary definition
		rsStr = currentInstrFields.get("rs"); // reg binary definition
		constStr = currentInstrFields.get("const"); // value
		
		rt = Long.parseLong(reg.getRegValByBin(rtStr), 2) & 0xffffffff;
		rs = Long.parseLong(reg.getRegValByBin(rsStr), 2) & 0xffffffff;
		immedi_unsigned = (short)Integer.parseInt(constStr, 2);
		immedi_signed = (short)ut.binStr16toSignedInt(constStr);
		
		if (DEV_MODE == true)
		{
			ut.println("Opcode=" + currentInstrAsmName + 
					", Rs->" + reg.getRegByBin(rsStr) + 
					", Rt->" + reg.getRegByBin(rtStr) + 
					", const->0b" + constStr);
		}
	}
	private void loadJType()
	{
		targetStr = currentInstrFields.get("target"); 
		target = Long.parseLong(reg.getRegValByBin(targetStr), 2) & 0xffffffff; 
		
		if (DEV_MODE == true)
		{
			ut.println("Opcode=" + currentInstrAsmName +
					", target->" + reg.getRegByBin(targetStr));
		}
	}
	
	private String longToBinStr32Len(Long bin)
	{
		return String.format("%32s", Integer.toBinaryString((int) (bin & 0xffffffff))).replace(' ', '0');
	}
	

	

	// =============================================================================================== Instruction Functions

	// ---------------------------------------------------------------------------------------- Loads
	private void ll() 
	{
		loadIType();
		rt = rs + immedi_unsigned;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void lw() 
	{
		loadIType();
		long effectiveMemoryAddress = rs + immedi_signed;
		rt = ((int)m.mem.get(effectiveMemoryAddress) << 24)+
			 ((int)m.mem.get(effectiveMemoryAddress+1) << 16)+
			 ((int)m.mem.get(effectiveMemoryAddress+2) << 8)+
			 ((int)m.mem.get(effectiveMemoryAddress+3));
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}	
	private void lhu() 
	{
		loadIType();
		long effectiveMemoryAddress = rs + immedi_signed;
		rt = ((int)m.mem.get(effectiveMemoryAddress) << 8)+
			 ((int)m.mem.get(effectiveMemoryAddress+1));
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt & 0x0000ffff));
		pc += 4;
	}	
	private void lh() 
	{
		loadIType();
		long effectiveMemoryAddress = rs + immedi_signed;
		if (m.mem.get(effectiveMemoryAddress) > 127)
			rt = ((int)m.mem.get(effectiveMemoryAddress) << 8) + ((int)m.mem.get(effectiveMemoryAddress+1)) | 0xffff0000l;
		else 
			rt = ((int)m.mem.get(effectiveMemoryAddress) << 8) + ((int)m.mem.get(effectiveMemoryAddress+1));
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void lbu() 
	{
		loadIType();
		long effectiveMemoryAddress = rs + immedi_signed;
		rt = m.mem.get(effectiveMemoryAddress);
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt & 0x000000ffl));
		pc += 4;
	}	
	private void lb() 
	{
		loadIType();
		long effectiveMemoryAddress = rs + immedi_signed;
		if (m.mem.get(effectiveMemoryAddress) > 127)
			rt = ((int)m.mem.get(effectiveMemoryAddress)) | 0xffff0000l;
		else 
			rt = ((int)m.mem.get(effectiveMemoryAddress));
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void lui() 
	{
		// Load the lower halfword of the immediate imm into 
		// the upper halfword of register Rd. The lower bits 
		// of the register are set to 0.
		loadIType();
		long lower_hw_imm = (immedi_signed & 0x0000ffff) << 16;
		ut.println("lower half word of imm = " + lower_hw_imm);
		rt = lower_hw_imm & 0xffff0000;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}	
	
	// ---------------------------------------------------------------------------------------- Stores
	private void sb() 
	{
		// Store the least significant byte of register Rt into memory address Rs + imm.
		loadIType();
		byte ls_rt = (byte)(rt & 0xffl);
		long store = rs + immedi_unsigned;
		m.mem.put(store, ls_rt);
		pc += 4;
	}
	private void sh() 
	{
		//Store the lower 16 bits (halfword) of register Rt into memory address Rs + imm.
		loadIType();
		byte ls_rt1 = (byte)(rt & 0xff000000l);
		byte ls_rt2 = (byte)(rt & 0x00ff0000l);
		byte ls_rt3 = (byte)(rt & 0x0000ff00l);
		byte ls_rt4 = (byte)(rt & 0x000000ffl);
		long store = rs + immedi_unsigned;
		m.mem.put(store,   ls_rt1);
		m.mem.put(store+1, ls_rt2);
		m.mem.put(store+2, ls_rt3);
		m.mem.put(store+3, ls_rt4);
		
		pc += 4;
	}	
	private void sw() 
	{
		// Store the word in register rt into memory address rs + imm.
		loadIType();
		byte ls_rt1 = (byte)(rt & 0xff00000000000000l);
		byte ls_rt2 = (byte)(rt & 0x00ff000000000000l);
		byte ls_rt3 = (byte)(rt & 0x0000ff0000000000l);
		byte ls_rt4 = (byte)(rt & 0x000000ff00000000l);
		byte ls_rt5 = (byte)(rt & 0x00000000ff000000l);
		byte ls_rt6 = (byte)(rt & 0x0000000000ff0000l);
		byte ls_rt7 = (byte)(rt & 0x000000000000ff00l);
		byte ls_rt8 = (byte)(rt & 0x00000000000000ffl);
		long store = rs + immedi_unsigned;
		m.mem.put(store,   ls_rt1);
		m.mem.put(store+1, ls_rt2);
		m.mem.put(store+2, ls_rt3);
		m.mem.put(store+3, ls_rt4);
		m.mem.put(store+4, ls_rt5);
		m.mem.put(store+5, ls_rt6);
		m.mem.put(store+6, ls_rt7);
		m.mem.put(store+7, ls_rt8);
		pc += 4;
	}	
	private void sc() 
	{
		// same like sw, but conditional upon a atomic flag. LL & SC used together.
		// TODO: implement the flag later
		loadIType();
		byte ls_rt1 = (byte)(rt & 0xff00000000000000l);
		byte ls_rt2 = (byte)(rt & 0x00ff000000000000l);
		byte ls_rt3 = (byte)(rt & 0x0000ff0000000000l);
		byte ls_rt4 = (byte)(rt & 0x000000ff00000000l);
		byte ls_rt5 = (byte)(rt & 0x00000000ff000000l);
		byte ls_rt6 = (byte)(rt & 0x0000000000ff0000l);
		byte ls_rt7 = (byte)(rt & 0x000000000000ff00l);
		byte ls_rt8 = (byte)(rt & 0x00000000000000ffl);
		long store = rs + immedi_unsigned;
		m.mem.put(store,   ls_rt1);
		m.mem.put(store+1, ls_rt2);
		m.mem.put(store+2, ls_rt3);
		m.mem.put(store+3, ls_rt4);
		m.mem.put(store+4, ls_rt5);
		m.mem.put(store+5, ls_rt6);
		m.mem.put(store+6, ls_rt7);
		m.mem.put(store+7, ls_rt8);
		pc += 4;
	}	
	// ---------------------------------------------------------------------------------------- Logical
	private void and() 
	{
		loadRType();
		rd = rt & sa;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void andi() 
	{
		loadIType();
		rt = rs & immedi_unsigned;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void nor() 
	{
		loadRType();
		rd = ~(rs | rt);
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void or() 
	{
		loadRType();
		rd = rs | rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void ori()
	{
		loadIType();
		rt = rs | immedi_unsigned;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void xor() 
	{
		loadRType();
		
		pc += 4;
	}
	private void xori() 
	{
		loadRType();
		
		pc += 4;
	}
	private void slt() 
	{
		loadRType();
		rd = rs < rt ? 1 : 0;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void sltu() 
	{
		loadRType();
		rd = rs < rt ? 1 : 0;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void slti() 
	{
		loadIType();
		rd = rs < immedi_unsigned ? 1 : 0;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void sltiu() 
	{
		loadIType();
		rd = rs < immedi_unsigned ? 1 : 0;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void sll()
	{
		loadRType();
		rd = rt << sa;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void srl()
	{
		loadRType();
		rd = rt >> sa;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	
	// ---------------------------------------------------------------------------------------- Arithmetic
	private void sra() 
	{
		loadRType();
		
		pc += 4;
	}
	private void srav() 
	{
		loadRType();
		
		pc += 4;
	}
	private void add() 
	{
		loadRType();
		rd = rs + rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void addi() 
	{
		loadIType();
		rt = rs + immedi_signed;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void addiu() 
	{
		loadIType();
		rt = rs + immedi_signed;
		reg.setRegValByBin(rtStr, longToBinStr32Len(rt));
		pc += 4;
	}
	private void addu() 
	{
		loadRType();
		rd = rs + rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void sub() 
	{
		loadRType();
		rd = rs - rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void subu() 
	{
		loadRType();
		rd = rs - rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void div() 
	{
		loadRType();
		reg.setLo(longToBinStr32Len(rs / rt));
		reg.setHi(longToBinStr32Len(rs % rt));
		pc += 4;
	}
	private void divu() 
	{
		loadRType();
		reg.setLo(longToBinStr32Len(rs / rt));
		reg.setHi(longToBinStr32Len(rs % rt));
		pc += 4;
	}
	private void mult() 
	{
		loadRType();
		rd = rs * rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void multu() 
	{
		loadRType();
		rd = rs * rt;
		reg.setRegValByBin(rdStr, longToBinStr32Len(rd));
		pc += 4;
	}
	private void mfhi() 
	{
		loadRType();
		
		pc += 4;
	}
	private void mflo() 
	{
		loadRType();
		
		pc += 4;
	}
	private void mthi() 
	{
		loadRType();
		
		pc += 4;
	}
	private void mtlo() 
	{
		loadRType();
		
		pc += 4;
	}

	// ---------------------------------------------------------------------------------------- Control
	private void j() 
	{
		loadJType();
		pc = target;
	}
	private void jal() 
	{
		loadJType();
		reg.setRegValByReg("$31", longToBinStr32Len(pc+4));
		pc = target;
	}
	private void jr() 
	{
		loadRType();
		pc = (long)rs;
	}
	private void jalr() 
	{
		loadRType();
		rd = pc + 4;
		pc = rs;
	}
	private void beq() 
	{
		loadIType();
		if (rt == rs)
			pc = pc + 4 + immedi_signed;
		else 
			pc += 4;
	}
	private void bne()
	{
		//ut.println("-------------------------- >BEFORE PC = " + pc);
		loadIType();
		if (rt != rs)
			pc = pc + 4 + immedi_signed;
		else
			pc += 4;
		//ut.println("-------------------------- >AFTER PC = " + pc);
	}	
	private void blt() 
	{
		loadRType();
		
		pc += 4;
	}
	private void bgt() 
	{
		loadRType();
		
		pc += 4;
	}
	private void ble() 
	{
		loadRType();
		
		pc += 4;
	}
	private void bge() 
	{
		loadRType();
		
		pc += 4;
	}
	private void syscall() 
	{
		loadRType();
		
		pc += 4;
	}
	
	
}
