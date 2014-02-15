package Summarizer;

public class SentenceObject {
	String content;
	int indexNumber;
	String fileName;
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
	}
	@Override
	public String toString() {
		return "SentenceObject [content=" + content + ", indexNumber="
				+ indexNumber + ", fileName=" + fileName + "]";
	}
	
	
}
