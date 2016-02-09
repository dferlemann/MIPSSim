import java.util.HashMap;


public class Instruction {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> opcodes = new HashMap<String, String>();
	private HashMap<String, String> instrFormat = new HashMap<String, String>();
	
	
	private void initialization()
	{
		// -------------------------------------------- Initialize Instructions 
		// R-type
		opcodes.put("add",  "100000");
		opcodes.put("addu", "100001");
        opcodes.put("and",  "100100");
        opcodes.put("jr",   "001000");
        opcodes.put("nor",  "100111");
        opcodes.put("or",   "100101");
        opcodes.put("slt",  "101010");
        opcodes.put("sltu", "101011");
        opcodes.put("sll",  "000000");
        opcodes.put("srl",  "000010");
        opcodes.put("sub",  "100010");
        opcodes.put("subu", "100011");
        
        // I-type
        opcodes.put("addi", "001000");
        opcodes.put("addiu","001001");
        opcodes.put("andi", "001100");
        opcodes.put("beq",  "000100");
        opcodes.put("bne",  "000101");
        opcodes.put("lbu",  "100100");
        opcodes.put("lhu",  "100101");
        opcodes.put("ll",   "110000");
        opcodes.put("lui",  "001111");
        opcodes.put("lw",   "100011");
        opcodes.put("ori",  "001101");
        opcodes.put("slti", "001010");
        opcodes.put("sltiu","001011");
        opcodes.put("sb",   "101000");
        opcodes.put("sc",   "111000");
        opcodes.put("sh",   "101001");
        opcodes.put("sw",   "101011");

        // J-type
        opcodes.put("j",    "000010");
        opcodes.put("jal",  "000011");
		
        // -------------------------------------------- Associate Instruction Format
        
        
        
	}
}
