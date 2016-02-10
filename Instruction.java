import java.util.HashMap;


public class Instruction {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> instrs = new HashMap<String, String>();
	private HashMap<String, String> instrsFormat = new HashMap<String, String>();
	
	
	// ============================================================================== Constructor
	public Instruction()
	{
		
	}
	
	private void init()
	{
		// -------------------------------------------- Initialize instructions 
		// R-type (functs) - R-type use opcode 000000
		instrs.put("add",  "100000"); // Addition
		instrs.put("addu", "100001"); // Add unsigned
        instrs.put("and",  "100100"); // Logical and
        instrs.put("jr",   "001000"); // Jump Register
        instrs.put("nor",  "100111"); // Logical nor
        instrs.put("or",   "100101"); // logical or
        instrs.put("slt",  "101010"); // Set less than
        instrs.put("sltu", "101011"); // Set less than unsigned
        instrs.put("sll",  "000000"); // Shift left logical
        instrs.put("srl",  "000010"); // Shift right logical
        instrs.put("sub",  "100010"); // Substract
        instrs.put("subu", "100011"); // Substract unsigned
        
        // I-type (opcodes)
        instrs.put("addi", "001000"); // Add immediate
        instrs.put("addiu","001001"); // Add immediate unsigned
        instrs.put("andi", "001100"); 
        instrs.put("beq",  "000100"); // Branch if equal
        instrs.put("bne",  "000101"); // branch if not equal
        instrs.put("lbu",  "100100"); // Load byte unsigned
        instrs.put("lhu",  "100101"); // Load half word unsigned
        instrs.put("ll",   "110000");
        instrs.put("lui",  "001111"); // Load upper immediate
        instrs.put("lw",   "100011"); // Load word
        instrs.put("ori",  "001101");
        instrs.put("slti", "001010"); // Set less than immediate
        instrs.put("sltiu","001011"); // Set less than immediate unsigned
        instrs.put("sb",   "101000"); // Store byte
        instrs.put("sc",   "111000"); // Store conditional
        instrs.put("sh",   "101001"); // Store halfword
        instrs.put("sw",   "101011"); // Store word

        // J-type (opcodes)
        instrs.put("j",    "000010"); // Jump
        instrs.put("jal",  "000011"); // Jump and Link
		
        instrs.put("syscall", "001100");
        
        // -------------------------------------------- Associate instruction Format
        
	}
	
	
	private interface instrFormat
	{
        String format(String[] parts);
    }
	
	
}
