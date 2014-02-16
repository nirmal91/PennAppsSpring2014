package Summarizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.aliasi.tokenizer.*;

import edu.pennapps.revisor.topics.TopicSignatures;

public class SkipItMainClass {


	public static String APICall(ArrayList<String> inputFromDhyanam, int numberOfLinesFromDhyanam, int[] mode)
	{	

		/*
		 * Prepare the IDF hashMap. Change this to make it more realistic. 
		 */
		String idfPaths = "resources/idf.txt";
		HashMap<String, Float> idfCounts = getIdf(idfPaths);
		System.out.println("We have the idf back");

		/*
		 * Prepare the TFIDF hashMap from the list of various files the user has selected
		 */
		HashMap<String, Float> tfIdfCounts = getTfIdf(idfCounts, inputFromDhyanam, mode);
		System.out.println("We have the tfidf scores back");




		/*
		 * Prepare a list of sentenceObjects (content, fileInfo, chronological information) which will be used later. A score of zero is being associated with them. 
		 */
		ArrayList<SentenceObject> allInformation = new ArrayList<SentenceObject>();
		int lineIdentifier = 0;
		for(int i=0;i<inputFromDhyanam.size();i++)
		{	
			String[] allLines;
			System.out.println(mode[i]);

			if(mode[i] == 0)
				allLines = inputFromDhyanam.get(i).trim().split("\n");
			else
			{
				allLines = inputFromDhyanam.get(i).trim().replaceAll("\n"," ").split("[.]");
			}

			for(String eachLine : allLines)
			{	
				if(eachLine.trim().length() == 0)
					continue;

				eachLine = eachLine.trim();
				//				System.out.println(eachLine);
				allInformation.add(new SentenceObject(eachLine,++lineIdentifier,new String(i+"")));
			}
			allLines = null;
		}
		System.out.println("We have the allInformation file ready");

		ArrayList<ReturnTopWords> topWordsSorted = populateTopicWords();
		HashMap<String, Float> topicWordsMap = new HashMap<String, Float>();

		for(ReturnTopWords currentWord : topWordsSorted)
		{
			topicWordsMap.put(currentWord.getWord(),currentWord.getValue());
			//			System.out.println(currentWord);
		}
		System.out.println("Top words are ready as well");

		/*
		 * I have the tfidfMap and the top 200 words at this stage. 
		 */


		HashMap<SentenceObject, ArrayList<Float>> userVectors = getVectorMap(allInformation, tfIdfCounts, topWordsSorted);

		/*		for(SentenceObject sent : userVectors.keySet())
		{
			System.out.println(sent.content + " : " + userVectors.get(sent));
		}
		 */
		/*
		 * Assigning scores to the sentence based on the TF-IDF scores of the words in it. 
		 */

		float sentScore = 0.0f;
		float topicScore = 0.0f;
		for(SentenceObject currentSentence : allInformation)
		{
			sentScore = 0.0f;
			topicScore = 0.0f;

			String[] words = currentSentence.content.toLowerCase().split(" +");
			//String[] words = currentSentence.content.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" +");
			for(String word : words)
			{
				if(tfIdfCounts.get(word) == null)
				{
					/*
					 * TODO
					 * log it somewhere
					 */
				}
				else
				{
					sentScore += tfIdfCounts.get(word);
				}

				if(topicWordsMap.containsKey(word))
					topicScore += topicWordsMap.get(word);

			}
			sentScore = sentScore/words.length;
			currentSentence.setScore(sentScore);
			currentSentence.setTopicWordScore(topicScore/words.length);
		}



		/*
		 * Setting up the final score by adding both the scores.
		 */
		for(SentenceObject currSent : allInformation)
		{
			currSent.setFinalScore(currSent.getTopicWordScore() + currSent.getScore());
		}

		Collections.sort(allInformation);

		int numberOfLinesAdded = 0;

		ArrayList<SentenceObject> linesAlreadyAdded = new ArrayList<SentenceObject>();
		for(int i=0;i<allInformation.size();i++)
		{
			if(numberOfLinesAdded < numberOfLinesFromDhyanam)
			{
				if(isValid(allInformation.get(i),linesAlreadyAdded, userVectors))
				{
					linesAlreadyAdded.add(allInformation.get(i));
					numberOfLinesAdded++;
				}
			}
		}

		try
		{
			Thread.sleep(1000);
		}
		catch(Exception e)
		{

		}



		/*
		/*
		 * We have all the sentences based on the scores we have
		 */
		/*		System.out.println("Trying to sort");
		Collections.sort(allInformation);
		System.out.println("SORTING DONE");
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		/*
		 * We are just allowed to show a limited number of sentences based on the time the user has at his/her disposal
		 */
		/*		ArrayList<SentenceObject> importantSentences = new ArrayList<SentenceObject>();
		for(int i=0;i<numberOfLinesFromDhyanam && i<allInformation.size();i++)
		{
			importantSentences.add(allInformation.get(i));
		}
		//		System.out.println(importantSentences);
		//		System.out.println("Reduced number of sentences");
		//		System.out.println(importantSentences.size());
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		 */
		Collections.sort(linesAlreadyAdded, new IndexComparator());
		
		StringBuffer returnBuffer = new StringBuffer();
		for(SentenceObject sentence : linesAlreadyAdded)
		{
			returnBuffer.append(sentence);
		}
		return new String(returnBuffer);
		
	}
	private static boolean isValid(SentenceObject sentenceObject,
			ArrayList<SentenceObject> linesAlreadyAdded,
			HashMap<SentenceObject, ArrayList<Float>> userVectors) {

		if(sentenceObject.content.split(" +").length < 5 || sentenceObject.content.split(" +").length > 25)
			return false;

		if(sentenceObject.content.matches(".*\\d.*")){
			return false;
		}
		for(int i=0;i<linesAlreadyAdded.size();i++)
		{
			SentenceObject addedLine = linesAlreadyAdded.get(i);
			if(getCosineSim(userVectors.get(addedLine),userVectors.get(sentenceObject)) > 0.6)
			{
				return false;
			}
		}
		return true;
	}
	private static float getCosineSim(ArrayList<Float> v1, ArrayList<Float> v2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < v1.size(); i++) //docVector1 and docVector2 must be of same length
		{
			dotProduct += v1.get(i) * v2.get(i);  //a.b
			magnitude1 += Math.pow(v1.get(i), 2);  //(a^2)
			magnitude2 += Math.pow(v2.get(i), 2); //(b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0f;
		}
		System.out.println(cosineSimilarity);
		return (float) cosineSimilarity;
	}
	public static HashMap<SentenceObject, ArrayList<Float>> getVectorMap(ArrayList<SentenceObject> sentences, HashMap<String,Float> TFIDFMap, ArrayList<ReturnTopWords> topicWords){
		HashMap<SentenceObject, ArrayList<Float>> vectorMap = new HashMap<SentenceObject, ArrayList<Float>>(); 


		ArrayList<ReturnTopWords> returnObject = new ArrayList<ReturnTopWords>();

		for(String word : TFIDFMap.keySet())
		{
			returnObject.add(new ReturnTopWords(word, TFIDFMap.get(word)));
		}
		Collections.sort(returnObject);

		ArrayList<ReturnTopWords> finalReturn = new ArrayList<ReturnTopWords>();
		for(int i=0;i<200;i++)
			finalReturn.add(returnObject.get(i));



		Float[] vector = new Float[finalReturn.size() + topicWords.size()];

		for(SentenceObject sentOb : sentences){
			String sent = sentOb.content;
			int i;
			for (i=0; i< finalReturn.size();i++){
				if(sent.indexOf(finalReturn.get(i).word) != -1){
					vector[i] = finalReturn.get(i).value;
				}else{
					vector[i] = 0.0f;
				}
			}
			for(int j=0;i<topicWords.size()+finalReturn.size();i++,j++){
				if(sent.indexOf(topicWords.get(j).word) != -1){
					vector[i] = topicWords.get(j).value;
				}else{
					vector[i] = 0.0f;
				}
			}
			ArrayList<Float> value = new ArrayList<Float>();

			for(i=0;i<400;i++)
				value.add(vector[i]);
			vectorMap.put(sentOb, value);
			value = null;
		}
		//		System.out.println(vectorMap);
		return vectorMap;
	}


	private static ArrayList<ReturnTopWords> populateTopicWords() {
		String configFile = "resources/config.example";
		HashMap<String, Float> topicWords = new HashMap<String, Float>();
		try {
			//			TopicSignatures.initiate(configFile); 

			BufferedReader br = null;

			try {

				String sCurrentLine;

				br = new BufferedReader(new FileReader("topic_words/lectures.ts"));

				while ((sCurrentLine = br.readLine()) != null) {
					String[] vals = sCurrentLine.split(" ");
					System.out.println(sCurrentLine);
					//System.out.println(vals);
					topicWords.put(vals[0], Float.parseFloat(vals[1]));
				}
				System.out.println(topicWords);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				br.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<ReturnTopWords> returnObject = new ArrayList<ReturnTopWords>();

		for(String word : topicWords.keySet())
		{
			returnObject.add(new ReturnTopWords(word, topicWords.get(word)));
		}
		Collections.sort(returnObject);

		ArrayList<ReturnTopWords> finalReturn = new ArrayList<ReturnTopWords>();
		for(int i=0;i<200;i++)
			finalReturn.add(returnObject.get(i));

		return finalReturn;
	}


	private static HashMap<String, Float> getTfIdf(
			HashMap<String, Float> idfCounts,
			ArrayList<String> fileInformations, int[] mode) {

		int idfFiles = 0;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("resources/idf.txt"));
			idfFiles = Integer.parseInt(br.readLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		StringBuffer combinedText = new StringBuffer();

		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();

		for(int i=0;i<fileInformations.size();i++)
		{	
			String currentContent = fileInformations.get(i);
			if(mode[i] == 0)
			{
				currentContent = currentContent.replaceAll("\n", " ");
			}
			else
			{
				currentContent = currentContent.replaceAll("[.]"," ");
			}
			String[] allWords = currentContent.split(" +");

			for(String currentWord : allWords)
			{	
				currentWord = currentWord.toLowerCase().trim();

				if(!wordCount.containsKey(currentWord))
					wordCount.put(currentWord, 1);
				else
					wordCount.put(currentWord, (wordCount.get(currentWord))+1);
			}
		}

		HashMap<String, Float> tfIdfMap = new HashMap<String, Float>();
		Float currentIDFValue;
		for(String word : wordCount.keySet())
		{
			currentIDFValue = 0.0f;
			if(!idfCounts.containsKey(word))
			{
				currentIDFValue = (float)Math.log(2);
			}
			else
			{
				currentIDFValue = idfCounts.get(word);
			}
			tfIdfMap.put(word, currentIDFValue * wordCount.get(word));
		}	
		return tfIdfMap;
	}

	private static HashMap<String, Float> getIdf(String idfPath) {

		HashMap<String, Float> idfMap = new HashMap<String, Float>();

		String newLine = null;
		String word = null;
		float score = 0.0f;
		try {
			BufferedReader br = new BufferedReader(new FileReader(idfPath));
			br.readLine();
			while((newLine=br.readLine())!=null)
			{
				word = newLine.split("\t")[0];
				score = Float.parseFloat(newLine.split("\t")[1]);
				idfMap.put(word, score);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idfMap;

	}

	private static void doPreProcessing(
			ArrayList<SentenceObject> fileInformations) {

		for(SentenceObject currentSentence : fileInformations)
		{
			//		Stemmer.helper("resources/111.txt");
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
				String fileName = string.substring(string.lastIndexOf("/")+1);
				BufferedReader br = new BufferedReader(new FileReader(string));
				while((newLine = br.readLine()) != null)
				{
					if(newLine.trim().compareTo("")!=0)
					{	
						fileInformations.add(new SentenceObject(newLine.trim(),++i,fileName));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(mode == 2)
		{
			/*
			 * It is a list of files, read each file and then add it's sentenceObject objects
			 */

			File folder = new File(string);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					System.out.println("File " + listOfFiles[i].getName());
				} else if (listOfFiles[i].isDirectory()) {
					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
		}
		else
		{
			System.out.println("Illegal mode");
		}
		return fileInformations;
	}
}
