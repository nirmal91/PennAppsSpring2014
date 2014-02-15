package Summarizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class SkipItMainClass {
	public static void main(String args[])
	{
		ArrayList<SentenceObject> fileInformations;
		fileInformations = getContentFromFile(1,"/Users/nirmalutwani/Documents/StudyNotes/PennAppsSpring2014/resources/1.txt");
		for(SentenceObject currentSentence : fileInformations)
		{
			System.out.println(currentSentence);
		}
	
	}
	
	
	private static ArrayList<SentenceObject> getContentFromFile(int mode, String string) {
	
		ArrayList<SentenceObject> fileInformations = new ArrayList<SentenceObject>();
		String newLine = null;

		if(mode == 1)
		{
			/*
			 * It just deals with a single file. 
			 */
			try {
				int i=0;
				BufferedReader br = new BufferedReader(new FileReader(string));
				while((newLine = br.readLine()) != null)
				{
					fileInformations.add(new SentenceObject(newLine,++i,string));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			/*
			 * It is a list of files, read each file and then add it's sentenceObject objects
			 */
			
		}
		return fileInformations;
	}


	public static ArrayList<String> getSummaries(ArrayList<String> fileInformations)
	{
		ArrayList<SentenceObject> returnList;
		for(String currentFileAllContent : fileInformations)
		{
			String[] allLines = currentFileAllContent.replaceAll("\\r", "").split("\\n");
			
			for(String line : allLines)
				System.out.println(line);
		}
		return null;
		
	//	return null;
	}
}
