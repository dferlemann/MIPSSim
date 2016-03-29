
// need to work on it after fixing project 1, otherwise don't see the point.

public class FuncUnit {

	private int latency; // unit: cycles
	
	// ============================================= Constructor
	
	public FuncUnit()
	{
		latency = 0;
	}
	
	
	// ============================================= Properties

	public int getLatency()
	{
		return latency;
	}
	
	
	// ============================================= Function Units
	
	public void IntALU1()
	{
		
	}
	
	public void IntALU2()
	{
		
	}
	
	public void IntMult()
	{
		latency = 4;
	}
	
	public void IntDiv()
	{
		latency = 16;
	}
	
	public void LoadUnitBuffer()
	{
		
	}
	
	public void StoreUnitBuffer()
	{
		
	}
	
	public void BranchProcessingUnit()
	{
		latency = 1;
	}
}
