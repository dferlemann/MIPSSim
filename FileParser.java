
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.List;

public class FileParser {

	private File asm_file;
	
	
	
	public void setFile(File file)
	{
		asm_file = file;
	}
	
	public String getFileName()
	{
		return asm_file.getName();
	}
	
	//
	
	public FileParser(File file)
	{
		this.setFile(file);
	}
	
	
	// ----------------------------------------------------------- Local Methods
	
	private ArrayList LinesToArrayList(File file)
	{
		Scanner s;
		ArrayList<String> list = new ArrayList<String>();
		try 
		{
			s = new Scanner(file);
			while (s.hasNextLine())
			    list.add(s.nextLine());
			s.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
}
