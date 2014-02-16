package edu.pennapps.revisor.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import edu.pennapps.revisor.topics.TopicSignatures;

public class Revisor {

	HashMap<String, Float> topicWords;
	HashMap<String, Float> scoreMap;

	public Revisor(){
		topicWords = new HashMap<String, Float>();
		scoreMap = new HashMap<String, Float>();
	}

	public HashMap<String, ArrayList<Float>> getVectorMap(ArrayList<String> sentences, HashMap<String, Float> TFMap, HashMap<String,Float> IDFMap, HashMap<String,Float> topicWords){
		HashMap<String, ArrayList<Float>> vectorMap = new HashMap<String, ArrayList<Float>>(); 

		String[] words = (String[]) TFMap.keySet().toArray();
		String[] twords = (String[]) topicWords.keySet().toArray();

		Float[] vector = new Float[words.length + twords.length];

		for(String sent : sentences){
			int i;
			for (i=0; i< words.length;i++){
				if(sent.indexOf(words[i]) != -1){
					vector[i] = getTFIDFWord(words[i], TFMap, IDFMap);
				}else{
					vector[i] = 0.0f;
				}
			}
			for(;i<twords.length;i++){
				if(topicWords.containsKey(twords[i])){
					vector[i] = topicWords.get(twords[i]);
				}else{
					vector[i] = 0.0f;
				}
			}
			ArrayList<Float> value = new ArrayList<Float>();
			Collections.addAll(value, vector);
			vectorMap.put(sent, value );
		}
		
	return vectorMap;
}

private float getTFIDFWord(String string, HashMap<String, Float> tFMap,
		HashMap<String, Float> iDFMap) {
	// TODO Auto-generated method stub
	return 0;
}

public static void main(String[] args) throws IOException{
	Revisor rv = new Revisor();
	//Assuming array list of file contents is present
	ArrayList<String> contents = new ArrayList<String>();
	contents.add("Hello. My name is Anthony.");
	contents.add("I work as a professor in University of Pennsylvania.");
	contents.add("Let us now test this.");
	File file = new File("C:\\cis555Project\\PennApps\\contents\\contents.txt");
	FileWriter fw = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw);

	for(String content: contents){
		bw.write(content + '\n');
	}

	bw.close();

	rv.populateTopicWords();

	System.exit(0);
}

private void populateTopicWords() {
	String configFile = "resources/config.example";
	try {
		TopicSignatures.initiate(configFile); 

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("topic_words/lectures.ts"));

			while ((sCurrentLine = br.readLine()) != null) {
				String[] vals = sCurrentLine.split(" ");
				System.out.println(sCurrentLine);
				//System.out.println(vals);
				this.topicWords.put(vals[0], Float.parseFloat(vals[1]));
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
}

public void getSummary(ArrayList<String> sents, int sentCount){

	for(String sent: sents){
		float total = 0.0f;
		total += getTFIDFScore(sent);
		total += getTopicScore(sent, topicWords);
		total = total/sent.length();
		scoreMap.put(sent, total);
	}
}

private HashMap<String, Float> getSimilarity(HashMap<String, ArrayList<Float>> vectorMap){
	HashMap<String, Float> similarityScore = new HashMap<String, Float>();
	for(String sent1: vectorMap.keySet()){
		float score = 0.0f;
		ArrayList<Float> v1 = vectorMap.get(sent1);
		for(String sent2: vectorMap.keySet()){
			if(!sent1.equals(sent2)){
				ArrayList<Float> v2 = vectorMap.get(sent2);
				score += getCosineSim(v1,v2);
			}
		}
	}
	return similarityScore;
	
}
private float getCosineSim(ArrayList<Float> v1, ArrayList<Float> v2) {
	// TODO Auto-generated method stub
	return 0;
}

private float getTopicScore(String sent, HashMap<String, Float> topicWords2) {
	// TODO Auto-generated method stub
	return 0;
}

private float getTFIDFScore(String sent) {
	// TODO Auto-generated method stub
	return 0;
}
}
