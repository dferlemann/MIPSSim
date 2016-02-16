import java.util.HashMap;


public class Decode {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> instrsAsmOpcode = new HashMap<String, String>();
	private HashMap<String, String> instrsAsmFunct = new HashMap<String, String>();
	private HashMap<String, String> instrsType = new HashMap<String, String>();
	private HashMap<String, InstructionFormat> instrsFormatMasks = new HashMap<String, InstructionFormat>();
	
	// ============================================================================== Properties
	public HashMap<String, String> getInstrsAsmOpcode()
	{
		return instrsAsmOpcode;
	}
	public HashMap<String, String> getInstrsAsmFunct()
	{
		return instrsAsmFunct;
	}
	public HashMap<String, InstructionFormat> GetInstrsFormatMasks()
	{
		return instrsFormatMasks;
	}
	public HashMap<String, String> GetInstrsTypes()
	{
		return instrsType;
	}
	// ============================================================================== Constructor
	public Decode()
	{
		init();
	}
	
	private void init()
	{
		// -------------------------------------------- Initialize instructions 
		// R-type (functs) - R-type use opcode 000000
		instrsAsmFunct.put("add",  "100000"); // Addition
		instrsAsmFunct.put("addu", "100001"); // Add unsigned
		instrsAsmFunct.put("and",  "100100"); // Logical and
		instrsAsmFunct.put("jr",   "001000"); // Jump Register
		instrsAsmFunct.put("nor",  "100111"); // Logical nor
		instrsAsmFunct.put("or",   "100101"); // logical or
		instrsAsmFunct.put("slt",  "101010"); // Set less than
		instrsAsmFunct.put("sltu", "101011"); // Set less than unsigned
		instrsAsmFunct.put("sll",  "000000"); // Shift left logical
		instrsAsmFunct.put("srl",  "000010"); // Shift right logical
		instrsAsmFunct.put("sub",  "100010"); // Substract
		instrsAsmFunct.put("subu", "100011"); // Substract unsigned
		
		instrsAsmFunct.put("sra",  "000011");
		instrsAsmFunct.put("srav", "000111");
		instrsAsmFunct.put("div",  "011010");
		instrsAsmFunct.put("divu", "011011");
		instrsAsmFunct.put("mult", "011000");
		instrsAsmFunct.put("multu","011001");		
		instrsAsmFunct.put("mfhi", "010000");
		instrsAsmFunct.put("mflo", "010010");
		instrsAsmFunct.put("mthi", "010001");
		instrsAsmFunct.put("mtlo", "010011");
		
		instrsAsmFunct.put("xor",  "100110");
		instrsAsmFunct.put("jalr", "001001");
		
		
		instrsAsmFunct.put("syscall", "001100");
		
        // I-type (opcodes)
		instrsAsmOpcode.put("addi", "001000"); // Add immediate
		instrsAsmOpcode.put("addiu","001001"); // Add immediate unsigned
		instrsAsmOpcode.put("andi", "001100"); 
		instrsAsmOpcode.put("xori", "001110");
		instrsAsmOpcode.put("beq",  "000100"); // Branch if equal
		instrsAsmOpcode.put("bne",  "000101"); // branch if not equal
		instrsAsmOpcode.put("lbu",  "100100"); // Load byte unsigned
		instrsAsmOpcode.put("lhu",  "100101"); // Load half word unsigned
		instrsAsmOpcode.put("ll",   "110000");
		instrsAsmOpcode.put("lui",  "001111"); // Load upper immediate
		instrsAsmOpcode.put("lw",   "100011"); // Load word
		instrsAsmOpcode.put("ori",  "001101");
		instrsAsmOpcode.put("slti", "001010"); // Set less than immediate
		instrsAsmOpcode.put("sltiu","001011"); // Set less than immediate unsigned
		instrsAsmOpcode.put("sb",   "101000"); // Store byte
		instrsAsmOpcode.put("sc",   "111000"); // Store conditional
		instrsAsmOpcode.put("sh",   "101001"); // Store halfword
		instrsAsmOpcode.put("sw",   "101011"); // Store word

		instrsAsmOpcode.put("lh",   "100001");
		instrsAsmOpcode.put("lb",   "100000");
		
        // J-type (opcodes)
		instrsAsmOpcode.put("j",    "000010"); // Jump
		instrsAsmOpcode.put("jal",  "000011"); // Jump and Link
		
        
        // ----------------------------------------------------------------- Associate instruction Type
        // Loads
        instrsType.put("lw",   "load"); // Load word
		instrsType.put("lhu",  "load"); // Load half word unsigned
		instrsType.put("lbu",  "load"); // Load byte unsigned
		instrsType.put("lui",  "load"); // Load upper immediate
		instrsType.put("ll",   "load");
		
		instrsType.put("lh",   "load");
		instrsType.put("lb",   "load");
        
        // Stores
		instrsType.put("sb",   "store"); // Store byte
		instrsType.put("sc",   "store"); // Store conditional
		instrsType.put("sh",   "store"); // Store halfword
		instrsType.put("sw",   "store"); // Store word
		
        // Logical
		instrsType.put("and",  "logical"); // Logical and
		instrsType.put("andi", "logical"); 
		instrsType.put("nor",  "logical"); // Logical nor
		instrsType.put("or",   "logical"); // logical or
		instrsType.put("ori",  "logical"); 
		instrsType.put("xor",  "logical");
		instrsType.put("xori", "logical");
		instrsType.put("slt",  "logical"); // Set less than
		instrsType.put("sltu", "logical"); // Set less than unsigned
		instrsType.put("slti", "logical"); // Set less than immediate
		instrsType.put("sltiu","logical"); // Set less than immediate unsigned
		instrsType.put("sll",  "logical"); // Shift left logical
		instrsType.put("srl",  "logical"); // Shift right logical
		
        // Arithmetic

		instrsType.put("add",  "arithmetic"); // Addition
		instrsType.put("addi", "arithmetic"); // Add immediate
		instrsType.put("addiu","arithmetic"); // Add immediate unsigned
		instrsType.put("addu", "arithmetic"); // Add unsigned
		instrsType.put("sub",  "arithmetic"); // Substract
		instrsType.put("subu", "arithmetic"); // Substract unsigned
		
		instrsType.put("sra",  "arithmetic");
		instrsType.put("srav", "arithmetic");
		instrsType.put("div",  "arithmetic");
		instrsType.put("divu", "arithmetic");
		instrsType.put("mult", "arithmetic");
		instrsType.put("multu","arithmetic");		
		instrsType.put("mfhi", "arithmetic");
		instrsType.put("mflo", "arithmetic");
		instrsType.put("mthi", "arithmetic");
		instrsType.put("mtlo", "arithmetic");
		
        
		// Control
		instrsType.put("j",    "control"); // Jump
		instrsType.put("jal",  "control"); // Jump and Link
		instrsType.put("jalr", "control"); // Jump and Link Register
		instrsType.put("jr",   "control"); // Jump Register
		instrsType.put("beq",  "control"); // Branch if equal
		instrsType.put("bne",  "control"); // branch if not equal

		instrsType.put("syscall", "interrupt");
        
        // ------------------------------------------------------------------- Associate instruction Format
        
		instrsFormatMasks.put("add",  instr_r); // Addition
		instrsFormatMasks.put("addu", instr_r); // Add unsigned
        instrsFormatMasks.put("and",  instr_r); // Logical and
        instrsFormatMasks.put("jr",   instr_r); // Jump Register
        instrsFormatMasks.put("nor",  instr_r); // Logical nor
        instrsFormatMasks.put("or",   instr_r); // logical or
        instrsFormatMasks.put("slt",  instr_r); // Set less than
        instrsFormatMasks.put("sltu", instr_r); // Set less than unsigned
        instrsFormatMasks.put("sll",  instr_r); // Shift left logical
        instrsFormatMasks.put("srl",  instr_r); // Shift right logical
        instrsFormatMasks.put("sub",  instr_r); // Substract
        instrsFormatMasks.put("subu", instr_r); // Substract unsigned

        
        instrsFormatMasks.put("sra",  instr_r);
        instrsFormatMasks.put("srav", instr_r);
        instrsFormatMasks.put("div",  instr_r);
        instrsFormatMasks.put("divu", instr_r);
        instrsFormatMasks.put("mult", instr_r);
        instrsFormatMasks.put("multu",instr_r);		
        instrsFormatMasks.put("mfhi", instr_r);
        instrsFormatMasks.put("mflo", instr_r);
        instrsFormatMasks.put("mthi", instr_r);
        instrsFormatMasks.put("mtlo", instr_r);
		
        instrsFormatMasks.put("xor",  instr_r);
        instrsFormatMasks.put("jalr",  instr_r);
		
        // I-type (opcodes)
        instrsFormatMasks.put("addi", instr_i); // Add immediate
        instrsFormatMasks.put("addiu",instr_i); // Add immediate unsigned
        instrsFormatMasks.put("andi", instr_i); 
        instrsFormatMasks.put("xori", instr_i); 
        instrsFormatMasks.put("beq",  instr_i); // Branch if equal
        instrsFormatMasks.put("bne",  instr_i); // branch if not equal
        instrsFormatMasks.put("lbu",  instr_i); // Load byte unsigned
        instrsFormatMasks.put("lhu",  instr_i); // Load half word unsigned
        instrsFormatMasks.put("ll",   instr_i);
        instrsFormatMasks.put("lui",  instr_i); // Load upper immediate
        instrsFormatMasks.put("lw",   instr_i); // Load word
        instrsFormatMasks.put("ori",  instr_i);
        instrsFormatMasks.put("slti", instr_i); // Set less than immediate
        instrsFormatMasks.put("sltiu",instr_i); // Set less than immediate unsigned
        instrsFormatMasks.put("sb",   instr_i); // Store byte
        instrsFormatMasks.put("sc",   instr_i); // Store conditional
        instrsFormatMasks.put("sh",   instr_i); // Store halfword
        instrsFormatMasks.put("sw",   instr_i); // Store word
        
        instrsFormatMasks.put("lh",   instr_i);
        instrsFormatMasks.put("lb",   instr_i);

        // J-type (opcodes)
        instrsFormatMasks.put("j",    instr_j); // Jump
        instrsFormatMasks.put("jal",  instr_j); // Jump and Link
		
        instrsFormatMasks.put("syscall", instr_syscall);
        
	}
	
	// ======================================================================= Instruction Format Masks

	
	private InstructionFormat instr_r = new InstructionFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", "000000");
			fields.put("rs", parts.substring(6, 11));
			fields.put("rt", parts.substring(11, 16));
			fields.put("rd", parts.substring(16, 21));
			fields.put("sa", parts.substring(21, 26));
			fields.put("funct", parts.substring(26, 32));
			
			return fields;
		}
	};
	
	private InstructionFormat instr_i = new InstructionFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", parts.substring(0, 6));
			fields.put("rs", parts.substring(6, 11));
			fields.put("rt", parts.substring(11, 16));
			fields.put("const", parts.substring(16, 32));
			
			return fields;
		}
	};
	
	private InstructionFormat instr_j = new InstructionFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", parts.substring(0, 6));
			fields.put("target", parts.substring(6, 32));
			
			return fields;
		}
	};
	

	
	private InstructionFormat instr_syscall = new InstructionFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", "000000");
			fields.put("code", "00000000000000000000");
			fields.put("funct", parts.substring(6, 32));
			
			return fields;
		}
	};
	
	
	
}
