import java.util.HashMap;


public class Register {

	// Registers B_25-21, B_20-16, B_15-11 (5-bit)
	private HashMap<String, String> registers = new HashMap<String, String>();
	
	private void initialization()
	{
        // -------------------------------------------- Initialize Registers
        
        registers.put("$zero", "00000"); // Constant 0
        registers.put("$at", "00001"); // Assembler Temporary

        // Function results & expression evaluation
        registers.put("$v0", "00010");
        registers.put("$v1", "00011");

        // Arguments
        registers.put("$a0", "00100");
        registers.put("$a1", "00101");
        registers.put("$a2", "00110");
        registers.put("$a3", "00111");

        // Temporaries
        registers.put("$t0", "01000");
        registers.put("$t1", "01001");
        registers.put("$t2", "01010");
        registers.put("$t3", "01011");
        registers.put("$t4", "01100");
        registers.put("$t5", "01101");
        registers.put("$t6", "01110");
        registers.put("$t7", "01111");

        // Saved temporaries
        registers.put("$s0", "10000");
        registers.put("$s1", "10001");
        registers.put("$s2", "10010");
        registers.put("$s3", "10011");
        registers.put("$s4", "10100");
        registers.put("$s5", "10101");
        registers.put("$s6", "10110");
        registers.put("$s7", "10111");

        // Temporaries
        registers.put("$t8", "11000");
        registers.put("$t9", "11001");

        // Reserved for OS Kernel
        registers.put("$k0", "11010");
        registers.put("$k1", "11011");

        registers.put("$gp", "11100"); // Global pointer
        registers.put("$sp", "11101"); // Stack pointer
        registers.put("$fp", "11110"); // Frame pointer
        registers.put("$ra", "11111"); // Return address
        
	}
	
    private String getRegister(String reg) {
        // Numeral address reference, e.g. $8
        if (reg.matches("[$]\\d+")) {
            return parseUnsigned5BitBin(Integer.parseInt(reg.substring(1)));
        }
        // Standard reference, e.g. $t0
        return registers.get(reg);
    }
}
