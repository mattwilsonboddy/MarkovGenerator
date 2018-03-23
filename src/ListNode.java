import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ListNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String word;
	
	private List<String> nextWords;
	
	ListNode(String word){
		
		this.word = word;
		nextWords = new LinkedList<String>();
		
	}
	
	public List<String> getNextWords() {
		
		return nextWords;
		
	}
	
	public String getWord() {
		
		return word;
		
	}
	
	public void addNextWord(String nextWord) {
	
		nextWords.add(nextWord);
		
	}
	
	public String getRandomNextWord(Random generator) {
		
		int size = nextWords.size();
		
		int rand = generator.nextInt(size);
		
		return nextWords.get(rand);
	
	}
	
	public String toString() {
		
		String toReturn = word + ": ";
		for (String s : nextWords) {
			
			toReturn += s + " -> ";
			
		}
		toReturn += "\n";
		return toReturn;
		
	}
	
}
