

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Debugger {
	
	public static void main(String[] args)
	{
		if (args.length == 1) 
		{
			File file_input = new File(args[0]);
			
			FileParser fpars = new FileParser(file_input);
			fpars.PrintFileContent();
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
