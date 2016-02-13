import java.util.HashMap;


public class Instruction {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> instrsAssembly = new HashMap<String, String>();
	private HashMap<String, instrFormat> instrsFormatMasks = new HashMap<String, instrFormat>();
	
	// ============================================================================== Properties
	public HashMap<String, String> getInstrs()
	{
		return instrsAssembly;
	}
	
	// ============================================================================== Constructor
	public Instruction()
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
	
	private interface instrFormat
	{
		HashMap<String, String> format(String parts);
    }
	
	private instrFormat instr_r = new instrFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", "000000");
			fields.put("rs", parts.substring(6, 10));
			fields.put("rt", parts.substring(11, 15));
			fields.put("rd", parts.substring(16, 20));
			fields.put("sa", parts.substring(21, 25));
			fields.put("funct", parts.substring(26, 31));
			
			return fields;
		}
	};
	
	private instrFormat instr_i = new instrFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", parts.substring(0, 5));
			fields.put("rs", parts.substring(6, 10));
			fields.put("rt", parts.substring(11, 15));
			fields.put("const", parts.substring(16, 31));
			
			return fields;
		}
	};
	
	private instrFormat instr_j = new instrFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", parts.substring(0, 5));
			fields.put("target", parts.substring(6, 31));
			
			return fields;
		}
	};
	
	private instrFormat instr_syscall = new instrFormat() 
	{
		public HashMap<String, String> format(String parts)
		{
			HashMap<String, String> fields = new HashMap<String, String>();
			
			fields.put("opcode", "000000");
			fields.put("code", "00000000000000000000");
			fields.put("funct", parts.substring(6, 31));
			
			return fields;
		}
	};
	
}
