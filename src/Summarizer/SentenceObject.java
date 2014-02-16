package Summarizer;

public class SentenceObject implements Comparable<SentenceObject> {
	String content;
	int indexNumber;
	String fileName;
	float score;
	float topicWordScore;
	float finalScore;
	
	public float getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(float finalScore) {
		this.finalScore = finalScore;
	}
	public SentenceObject(String content, int indexNumber, String fileName,
			float score, float topicWordScore) {
		super();
		this.content = content;
		this.indexNumber = indexNumber;
		this.fileName = fileName;
		this.score = score;
		this.topicWordScore = topicWordScore;
	}
	public float getTopicWordScore() {
		return topicWordScore;
	}
	public void setTopicWordScore(float topicWordScore) {
		this.topicWordScore = topicWordScore;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public SentenceObject(String content, int indexNumber, String fileName) {
		super();
		this.content = content;
		this.indexNumber = indexNumber;
		this.fileName = fileName;
		this.score = 0.0f;
		this.topicWordScore = 0.0f;
	}
	
	public SentenceObject(String content, int indexNumber, String fileName,
			int score) {
		super();
		this.content = content;
		this.indexNumber = indexNumber;
		this.fileName = fileName;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "SentenceObject [content=" + content + ", indexNumber="
				+ indexNumber + ", fileName=" + fileName + ", score=" + score
				+ ", topicWordScore=" + topicWordScore + ", finalScore="
				+ finalScore + "]";
	}
	public int compareTo(SentenceObject obj)
	{
		if(obj.finalScore > finalScore)
			return 1;
		else
			return -1;
			
	}
	
}
