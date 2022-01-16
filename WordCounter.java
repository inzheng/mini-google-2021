import java.util.HashMap;
import java.util.Map;

public class WordCounter {
	
	public static Map<String, Integer> count(String text) {
		text = sanitize(text);
		text = text.toLowerCase();
		
		String[] arr = text.split(" ");
		Map<String, Integer> freq = new HashMap<>();
		for (String str : arr) {
			if (!str.isEmpty()) {
				freq.put(str, freq.getOrDefault(str, 0) + 1);
			}
		}
		
		return freq;
	}
	
	private static String sanitize(String text) {
		String lowerCasedSentence = text.toLowerCase();
		return lowerCasedSentence.replaceAll("[^a-zA-Z ]", "");
	}

}
