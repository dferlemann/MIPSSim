

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Debugger {
	
	
	public static void main(String[] args)
	{
		ArrayList<String> instrContent, dataContent;
		String startAddr, instrAddr, dataAddr;
		
		if (args.length == 1) 
		{
			File file_input = new File(args[0]);
			
			Simulator sim = new Simulator(file_input);
			
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
