import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Markov implements MarkovInterface {

	private List<ListNode> wordList;

	private String starter;

	private Random generator;

	/* Utility methods */
	
	public String readTextFile() {
		
		String textString = "";
		String line = null;
		
		try {
			FileReader fileReader = new FileReader("training.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
	
			
			while ((line = bufferedReader.readLine()) != null) {
				textString += line + "\n";
			}
			bufferedReader.close();
		
		} catch(Exception ex) {
			
			System.out.println("ERROR FUCKWIT");
			
		}
		
		return textString;
		
	}
	
	public void save() {
		
		try {
			FileOutputStream fos = new FileOutputStream("t.tmp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(wordList);
			oos.close();
		} catch (Exception ex) {
			
			System.out.println("SAVING ERROR");
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		
		try {
			FileInputStream fis = new FileInputStream("t.tmp");
			ObjectInputStream ois = new ObjectInputStream(fis);
			wordList =  (List<ListNode>) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			
			System.out.println("LOADING ERROR" + ex);
			
		}
		
		starter = wordList.get(0).getWord();
		System.out.println(wordList);
	}
	
	@Override
	public String toString() {
		String toReturn = "FINAL LISTNODE STRUCTURE\n--------------------\n";
		for (ListNode n : wordList) {
			toReturn += n.toString();
		}
		return toReturn;
	}


	public int searchPos(String text) {

		for (int i = 0; i < wordList.size(); i++) {

			if (wordList.get(i).getWord().equals(text)) {

				System.out.println("Word found");
				return i;

			}

		}

		return -1;

	}

	public int indexOf(String text) {
		for (int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).getWord().equals(text)) {

				return i;

			}
		}
		return -1;
	}

	protected List<String> getTokens(String pattern, String text) {
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern splitter = Pattern.compile(pattern);
		Matcher m = splitter.matcher(text);

		while (m.find()) {
			tokens.add(m.group());
		}

		return tokens;
	}

	// Constructor
	public Markov(Random generator) {

		this.generator = generator;
		wordList = new LinkedList<ListNode>();
		starter = "";

	}

	@Override
	public void train(String sourceText) {

		String prevWord;

		List<String> words = getTokens("[a-zA-Z,.!'’‘/\r\n]+", sourceText);

		starter = words.get(0);
		prevWord = starter;
		int size = words.size();
		int index = 0;
		
		System.out.println("Training");
		System.out.println("-------------");

		for (int i = 1; i <= size; i++) {
			
			index = indexOf(prevWord);

			// if no node, create it
			if (index == -1) {
				wordList.add(new ListNode(prevWord));
				index = wordList.size() -1;
			}

			// if end of words, break
			if (i == size) {

				break;

			// else add new next word
			} else {

				wordList.get(index).addNextWord(words.get(i));

			}
			
			//Print progress percentage
			double percentage = ((double)i/size)*100;
			String percentageFormat = String.format("%.2f", percentage);
			System.out.println(percentageFormat + "%");
			
			prevWord = words.get(i);

		}
		
		//add first word to list of next words for last word
		wordList.get(index).addNextWord(starter);
		System.out.println("100.00%\nTRAINING COMPLETE\n");

	}

	@Override
	public String generateText(int numWords) {

		if(wordList.size()<=0)return  null;
		
		
		//always starts on same word as input - change?
		String currWord = starter;
		
		System.out.println("currWord -> " + currWord);
		
		String output = " ";
		output += currWord + " ";
		
		int pos = 0;
		for(int i = 0; i < numWords; i++) {
			
			//System.out.println(i + "th iteration");
			pos = indexOf(currWord);
			//System.out.println("pos -> " + pos);
			
			String randomWord = wordList.get(pos).getRandomNextWord(generator);
			output += randomWord + " ";
			currWord = randomWord;
		}
		
		return output;
	}

	@Override
	public void retrain(String sourceText) {
		wordList.clear();
		train(sourceText);
	}
	
	public int intInput(String prompt) {
		
		Scanner reader = new Scanner(System.in);
		System.out.println(prompt);
		int input = reader.nextInt();
		//reader.close();
		return input;
		
	}
	
	
	public static void main(String[] args) {
		
		Markov gen = new Markov(new Random());
		int noOfWords = 50;
		
		while (true) {
			
			int choice = gen.intInput("1) Train, Save and Generate [training.txt]\n2) Load and Generate\n");
			noOfWords = gen.intInput("Enter number of words to generate: ");
			//reader.close();
			
			if (choice == 1) {
				
				String sourceText = gen.readTextFile();
				gen.train(sourceText);
				gen.save();
				
			} else if (choice == 2) {
				
				gen.load();
				
			}
			
			//System.out.println(gen);
			System.out.println(gen.generateText(noOfWords) + "\n");
		}
		
	}

}
