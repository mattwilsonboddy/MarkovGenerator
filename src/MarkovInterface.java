
public interface MarkovInterface{

	public void train(String sourceText);
	
	public String generateText(int numWords);
	
	public void retrain(String sourceText);
	
}
