import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MachineCodeParser {
	
	private CUtils ut;
	private ArrayList<String> originalCodes;
	private HashMap<String, String> instrsAsm = new HashMap<String, String>(); 
	private LinkedHashMap<Integer, String> instructions = new LinkedHashMap<Integer, String>(); // instr#, instruction in binary
	private LinkedHashMap<Integer, String> opcodes = new LinkedHashMap<Integer, String>(); // instr#, opcode in binary
	private LinkedHashMap<Integer, String> instrTypes = new LinkedHashMap<Integer, String>(); // instr#, type of instr
	
	public MachineCodeParser(ArrayList<String> codes)
	{
		originalCodes = codes;
		
		ut = new CUtils();
		
		// Execute
		parseMachineCodes();
		categorizeInstructions();
		
	}
	
	private void parseMachineCodes()
	{
		String binary;
		Integer key = 0;
		for(String code : originalCodes)
		{
			binary = ut.hex2binary(code);
			if (binary.length() != 32)
				binary = String.format("%32s", binary).replace(' ', '0');
			instructions.put(key, binary);
			opcodes.put(key, binary.substring(0, 6));
			//ut.println(key + " " + binary.substring(0, 6));
			key++;
		}
	}
	
	private void categorizeInstructions()
	{
		Instruction instr = new Instruction();
		instrsAsm = instr.getInstrs();
		
		for (Integer key = 0; key < opcodes.size(); key++)
		{
			String instrAsm = ut.getKeyByValue(instrsAsm, opcodes.get(key));
			ut.println(instrAsm);
		}
		
	}
}
