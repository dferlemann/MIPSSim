
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.List;

public class FileParser {

	private File asm_file;
	private ArrayList<String> fileContent;
	
	public void setFile(File file)
	{
		asm_file = file;
	}
	
	public File getFile()
	{
		return asm_file;
	}
	
	public String getFileName()
	{
		return asm_file.getName();
	}
	
	public ArrayList<String> getFileContent()
	{
		return fileContent;
	}
	
	// ----------------------------------------------------------- Constructor
	
	public FileParser(File file)
	{
		fileContent = new ArrayList<String>(); // initialize ArrayList
		
		this.setFile(file);
		LinesToArrayList(); // convert each line in the file to a String and store in a list
	}
	
	
	// ----------------------------------------------------------- Local Methods
	
	private void LinesToArrayList()
	{
		Scanner s;
		try 
		{
			
			File file = getFile();
			s = new Scanner(file);
			
			while (s.hasNextLine())
			{
				String nextLine = s.nextLine();
				
				if (!nextLine.isEmpty() || !nextLine.equals("\n"))
				{
					fileContent.add(nextLine);
				}
			}
				
			s.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// ------------------------------------------------------------ Supporting Methods
	public void PrintFileContent()
	{
		println(getFileName());
		
		for (int i = 0; i < fileContent.size(); i++)
		{
			println(fileContent.get(i));
		}
	}
	
	public static void println(Object line) 
	{
	    System.out.println(line);
	}
}
