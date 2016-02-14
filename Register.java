import java.util.LinkedHashMap;


public class Register {

	private String init_val = "00000000000000000000000000000000";
	// Registers B_25-21, B_20-16, B_15-11 (5-bit)
	private LinkedHashMap<String, String> regdef = new LinkedHashMap<String, String>();
	private String hi, lo; // special registers: $hi, $lo
	private LinkedHashMap<String, String> register = new LinkedHashMap<String, String>();
	private CUtils ut = new CUtils();
	
	// ============================================================================== Properties
	// ------------------------------------------------------ Get properties
	public String getHi()
	{
		return hi;
	}
	public String getLo()
	{
		return lo;
	}
	public String getRegValByReg(String regnum) // Get register content by register name such as $1
	{
		return register.get(regnum);
	}
	public String getRegValByBin(String binary) // Get register content by register 5-bit binary
	{
		return register.get(getRegByBin(binary)); 
	}
    public String getRegByBin(String binary)  // Get register name (e.g. $1) by binary 
    {
        return ut.getKeyByValue(regdef, binary);
    }
    public String getBinByReg(String regnum)  // Get binary definition by register name
    {
        return regdef.get(regnum);
    }
    // ------------------------------------------------------ Set properties
	public void setRegValByReg(String regnum, String val) 
	{
		register.put(regnum, val);
	}
	public void setRegValByBin(String binary, String val) 
	{
		register.put(getRegByBin(binary), val);
	}	
	public void setHi(String val)
	{
		hi = val;
	}
	public void setLo(String val)
	{
		lo = val;
	}
	
	// ============================================================================== Constructor
	public Register()
	{
		init();
	}
	
	private void init()
	{
		// -------------------------------------------- Initialize Register values
		hi = init_val; 
		lo = init_val;
		register.put("$0", init_val); // Constant 0
		register.put("$1", init_val); // Assembler Temporary
		register.put("$2", init_val);
		register.put("$3", init_val);
		register.put("$4", init_val);
		register.put("$5", init_val);
		register.put("$6", init_val);
		register.put("$7", init_val);
		register.put("$8", init_val);
		register.put("$9", init_val);
		register.put("$10", init_val);
		register.put("$11", init_val);
		register.put("$12", init_val);
		register.put("$13", init_val);
		register.put("$14", init_val);
		register.put("$15", init_val);
		register.put("$16", init_val);
		register.put("$17", init_val);
		register.put("$18", init_val);
		register.put("$19", init_val);
		register.put("$20", init_val);
		register.put("$21", init_val);
		register.put("$22", init_val);
		register.put("$23", init_val);
		register.put("$24", init_val);
		register.put("$25", init_val);
		register.put("$26", init_val); // $k0
		register.put("$27", init_val); // $k1
		register.put("$28", init_val); // $gp - Global pointer
		register.put("$29", init_val); // $sp - Stack pointer
        register.put("$30", init_val); // $fp - Frame pointer
        register.put("$31", init_val); // $ra - Return address
		
        // -------------------------------------------- Initialize Standard Registers Definition
        
		regdef.put("$0", "00000"); // Constant 0
		regdef.put("$1", "00001"); // Assembler Temporary

        // Function results & expression evaluation 		$v0 - $v1
		regdef.put("$2", "00010");
		regdef.put("$3", "00011");

        // Arguments 										$a0 - $a3
		regdef.put("$4", "00100");
		regdef.put("$5", "00101");
		regdef.put("$6", "00110");
		regdef.put("$7", "00111");

        // Temporaries										$t0 - $t7
		regdef.put("$8", "01000");
		regdef.put("$9", "01001");
		regdef.put("$10", "01010");
		regdef.put("$11", "01011");
		regdef.put("$12", "01100");
		regdef.put("$13", "01101");
		regdef.put("$14", "01110");
		regdef.put("$15", "01111");

        // Saved temporaries								$s0 - $s7
		regdef.put("$16", "10000");
		regdef.put("$17", "10001");
		regdef.put("$18", "10010");
		regdef.put("$19", "10011");
		regdef.put("$20", "10100");
		regdef.put("$21", "10101");
		regdef.put("$22", "10110");
		regdef.put("$23", "10111");

        // Temporaries										$t8 - $t9
		regdef.put("$24", "11000");
		regdef.put("$25", "11001");

        // Reserved for OS Kernel
		regdef.put("$26", "11010"); // $k0
		regdef.put("$27", "11011"); // $k1

		regdef.put("$28", "11100"); // $gp - Global pointer
		regdef.put("$29", "11101"); // $sp - Stack pointer
        regdef.put("$30", "11110"); // $fp - Frame pointer
        regdef.put("$31", "11111"); // $ra - Return address
        
	}
    
    public void printAllRegisters()
    {
    	ut.println(String.format("%4s", "hi") + "=" + ut.binStr32ToHexString(hi));
    	ut.println(String.format("%4s", "lo") + "=" + ut.binStr32ToHexString(lo));
    	ut.println("  -----------");
    	for(String key : register.keySet())
    	{
    		ut.println(String.format("%4s", key) + "=" + ut.binStr32ToHexString(register.get(key)));
    	}
    }
}
