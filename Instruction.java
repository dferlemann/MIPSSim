import java.util.HashMap;


public class Instruction {
	
	// Opcode B_31-26 (6-bit)
	private HashMap<String, String> instrs = new HashMap<String, String>();
	private HashMap<String, String> instrsFormat = new HashMap<String, String>();
	
	
	private void init()
	{
		// -------------------------------------------- Initialize instructions 
		// R-type (funts)
		instrs.put("add",  "100000");
		instrs.put("addu", "100001");
        instrs.put("and",  "100100");
        instrs.put("jr",   "001000");
        instrs.put("nor",  "100111");
        instrs.put("or",   "100101");
        instrs.put("slt",  "101010");
        instrs.put("sltu", "101011");
        instrs.put("sll",  "000000");
        instrs.put("srl",  "000010");
        instrs.put("sub",  "100010");
        instrs.put("subu", "100011");
        
        // I-type (opcodes)
        instrs.put("addi", "001000");
        instrs.put("addiu","001001");
        instrs.put("andi", "001100");
        instrs.put("beq",  "000100");
        instrs.put("bne",  "000101");
        instrs.put("lbu",  "100100");
        instrs.put("lhu",  "100101");
        instrs.put("ll",   "110000");
        instrs.put("lui",  "001111");
        instrs.put("lw",   "100011");
        instrs.put("ori",  "001101");
        instrs.put("slti", "001010");
        instrs.put("sltiu","001011");
        instrs.put("sb",   "101000");
        instrs.put("sc",   "111000");
        instrs.put("sh",   "101001");
        instrs.put("sw",   "101011");

        // J-type (opcodes)
        instrs.put("j",    "000010");
        instrs.put("jal",  "000011");
		
        // -------------------------------------------- Associate instrsuction Format
        
	}
	
	private interface instrParser 
	{
        String parse(String[] parts);
    }
	
	
}
