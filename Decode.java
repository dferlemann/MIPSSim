import java.util.HashMap;


public class Decode {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> instrsAssembly = new HashMap<String, String>();
	private HashMap<String, String> instrsType = new HashMap<String, String>();
	private HashMap<String, InstructionFormat> instrsFormatMasks = new HashMap<String, InstructionFormat>();
	
	// ============================================================================== Properties
	public HashMap<String, String> getInstrs()
	{
		return instrsAssembly;
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
		instrsAssembly.put("add",  "100000"); // Addition
		instrsAssembly.put("addu", "100001"); // Add unsigned
		instrsAssembly.put("and",  "100100"); // Logical and
		instrsAssembly.put("jr",   "001000"); // Jump Register
		instrsAssembly.put("nor",  "100111"); // Logical nor
		instrsAssembly.put("or",   "100101"); // logical or
		instrsAssembly.put("slt",  "101010"); // Set less than
		instrsAssembly.put("sltu", "101011"); // Set less than unsigned
		instrsAssembly.put("sll",  "000000"); // Shift left logical
		instrsAssembly.put("srl",  "000010"); // Shift right logical
		instrsAssembly.put("sub",  "100010"); // Substract
		instrsAssembly.put("subu", "100011"); // Substract unsigned
		
		instrsAssembly.put("sra",  "000011");
		instrsAssembly.put("srav", "arithmetic");
		instrsAssembly.put("div",  "arithmetic");
		instrsAssembly.put("divu", "arithmetic");
		instrsAssembly.put("mult", "arithmetic");
		instrsAssembly.put("multu","arithmetic");		
		instrsAssembly.put("mfhi", "010000");
		instrsAssembly.put("mflo", "010010");
		instrsAssembly.put("mthi", "arithmetic");
		instrsAssembly.put("mtlo", "arithmetic");
		
        
        // I-type (opcodes)
		instrsAssembly.put("addi", "001000"); // Add immediate
		instrsAssembly.put("addiu","001001"); // Add immediate unsigned
		instrsAssembly.put("andi", "001100"); 
        instrsAssembly.put("beq",  "000100"); // Branch if equal
        instrsAssembly.put("bne",  "000101"); // branch if not equal
        instrsAssembly.put("lbu",  "100100"); // Load byte unsigned
        instrsAssembly.put("lhu",  "100101"); // Load half word unsigned
        instrsAssembly.put("ll",   "110000");
        instrsAssembly.put("lui",  "001111"); // Load upper immediate
        instrsAssembly.put("lw",   "100011"); // Load word
        instrsAssembly.put("ori",  "001101");
        instrsAssembly.put("slti", "001010"); // Set less than immediate
        instrsAssembly.put("sltiu","001011"); // Set less than immediate unsigned
        instrsAssembly.put("sb",   "101000"); // Store byte
        instrsAssembly.put("sc",   "111000"); // Store conditional
        instrsAssembly.put("sh",   "101001"); // Store halfword
        instrsAssembly.put("sw",   "101011"); // Store word

        // J-type (opcodes)
        instrsAssembly.put("j",    "000010"); // Jump
        instrsAssembly.put("jal",  "000011"); // Jump and Link
		
        instrsAssembly.put("syscall", "001100");
        
        // -------------------------------------------- Associate instruction Type
        // Loads
        instrsType.put("lw",   "load"); // Load word
		instrsType.put("lhu",  "load"); // Load half word unsigned
		instrsType.put("lbu",  "load"); // Load byte unsigned
		instrsType.put("lui",  "load"); // Load upper immediate
		instrsType.put("ll",   "load");
        
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
		instrsType.put("jr",   "control"); // Jump Register
		instrsType.put("beq",  "control"); // Branch if equal
		instrsType.put("bne",  "control"); // branch if not equal

		instrsType.put("syscall", "syscall");
        
        // -------------------------------------------- Associate instruction Format
        
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
        
        // I-type (opcodes)
        instrsFormatMasks.put("addi", instr_i); // Add immediate
        instrsFormatMasks.put("addiu",instr_i); // Add immediate unsigned
        instrsFormatMasks.put("andi", instr_i); 
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
	
	private InstructionFormat instr_fi = new InstructionFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", parts.substring(0, 6));
			fields.put("fmt", parts.substring(6, 11));
			fields.put("ft", parts.substring(11, 16));
			fields.put("const", parts.substring(16, 32));
			
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
