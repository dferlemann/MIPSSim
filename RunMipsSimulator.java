

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RunMipsSimulator {
	
	private static File file_input;
	
	public static void main(String[] args)
	{
		if (args.length == 1)
		{
			file_input = new File(args[0]);
			
			try {
					Simulator sim = new Simulator(file_input);
					
					sim.start();
			} 
			catch (DebugException ex) 
			{
				System.out.println("Error Message:   =========================================== ");
				System.out.println(ex.getMessage());
				System.out.println("Stack Trace:     =========================================== ");
				ex.printStackTrace();
				
				System.out.println("Test File Input: =========================================== ");
				try 
				{
					FileInputStream fis = new FileInputStream(file_input);
					int oneByte;
					while ((oneByte = fis.read()) != -1) {
						System.out.write(oneByte);
						// System.out.print((char)oneByte); // could also do this
					}
					System.out.flush();
					fis.close();
				} 
				catch (FileNotFoundException e) {
					System.out.println("File Not Found! ");
				}
				catch (IOException e) {
					System.out.println("Cannot Read from input stream! ");
				}
			}
		}
		else
		{
			System.out.println("Usage: Debugger <filename>");
		}
		
		
	}
}
