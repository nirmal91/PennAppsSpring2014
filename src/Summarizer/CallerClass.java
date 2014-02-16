package Summarizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CallerClass {
	public static void main(String args[])
	{

		// Prepare input from the previous part

		ArrayList<String> inputFromDhyanam = new ArrayList<String>();		

		StringBuffer information = new StringBuffer();
		String line = null;
		try 
		{
			for(int i=1;i<=19;i++)
			{
				BufferedReader br = new BufferedReader(new FileReader("resources/Kriti19Files/"+i+".txt"));
				while((line = br.readLine())!= null)
				{
					information.append(line+"\n");
				}
				inputFromDhyanam.add(new String(information));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		System.out.println("Input file has been put into an arraylist of strings");


		int numberOfLinesFromDhyanam = 50;
		System.out.println(inputFromDhyanam.size());
		try
		{
			Thread.sleep(1000);
		}
		catch(Exception e)
		{
			
		}
		int[] modes = new int[19];
		for(int i=0;i<19;i++)
			modes[i] = 1;
		String summary = SkipItMainClass.APICall(inputFromDhyanam, numberOfLinesFromDhyanam, modes);
		System.out.println(summary);

	}
}
