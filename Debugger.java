

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Debugger {
	public static void main(String[] args)
	{
		if (args.length == 1) 
		{
			File file_input = new File(args[0]);
			BufferedReader bufferRead = null;
			
			try {

		        String sCurrentLine;

		        bufferRead = new BufferedReader(new FileReader(file_input));

		        while ((sCurrentLine = bufferRead.readLine()) != null) 
		        {
		            System.out.println(sCurrentLine);
		        }

		    } 

		    catch (IOException e) {
		        e.printStackTrace();
		    } 

		    finally {
		        try 
		        {
		            if (bufferRead != null)bufferRead.close();
		        } 
		        catch (IOException ex) 
		        {
		            ex.printStackTrace();
		        }
		    }
		} 
		else
		{
			println("Usage: Debugger <filename>");
		}
		
		
	}
	
	public static void println(Object line) 
	{
	    System.out.println(line);
	}
}
