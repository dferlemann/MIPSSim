import java.util.HashMap;


public class Register {

	// Registers B_25-21, B_20-16, B_15-11 (5-bit)
	private HashMap<String, String> registers = new HashMap<String, String>();
	private String hi, lo; // special registers: $hi, $lo
	
	// ============================================================================== Get/Sets
	public String getHi()
	{
		return hi;
	}
	public void setHi(String val)
	{
		hi = val;
	}
	
	public String getLo()
	{
		return lo;
	}
	public void setLo(String val)
	{
		lo = val;
	}
	
	// ============================================================================== Setup
	public void Register()
	{
		init();
	}
	
	private void init()
	{
        // -------------------------------------------- Initialize Standard Registers
        
        registers.put("$0", "00000"); // Constant 0
        registers.put("$1", "00001"); // Assembler Temporary

        // Function results & expression evaluation 		$v0 - $v1
        registers.put("$2", "00010");
        registers.put("$3", "00011");

        // Arguments 										$a0 - $a3
        registers.put("$4", "00100");
        registers.put("$5", "00101");
        registers.put("$6", "00110");
        registers.put("$7", "00111");

        // Temporaries										$t0 - $t7
        registers.put("$8", "01000");
        registers.put("$9", "01001");
        registers.put("$10", "01010");
        registers.put("$11", "01011");
        registers.put("$12", "01100");
        registers.put("$13", "01101");
        registers.put("$14", "01110");
        registers.put("$15", "01111");

        // Saved temporaries								$s0 - $s7
        registers.put("$16", "10000");
        registers.put("$17", "10001");
        registers.put("$18", "10010");
        registers.put("$19", "10011");
        registers.put("$20", "10100");
        registers.put("$21", "10101");
        registers.put("$22", "10110");
        registers.put("$23", "10111");

        // Temporaries										$t8 - $t9
        registers.put("$24", "11000");
        registers.put("$25", "11001");

        // Reserved for OS Kernel
        registers.put("$26", "11010"); // $k0
        registers.put("$27", "11011"); // $k1

        registers.put("$28", "11100"); // $gp - Global pointer
        registers.put("$29", "11101"); // $sp - Stack pointer
        registers.put("$30", "11110"); // $fp - Frame pointer
        registers.put("$31", "11111"); // $ra - Return address
        
	}
	
    private String getRegister(String reg) {
        // Numeral address reference, e.g. $8
        if (reg.matches("[$]\\d+")) {
            //return parseUnsigned5BitBin(Integer.parseInt(reg.substring(1)));
        }
        // Standard reference, e.g. $t0
        return registers.get(reg);
    }
}
