import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonConverter {
	
	public static String toJson(Map<Integer, HtmlParsedData> linkWithData) {
		Gson gson = new Gson();
		return gson.toJson(linkWithData);
	}
	
	public static String invertedIndexToJson(Map<String, Map<Integer, Integer>> invertedIndex) {
		Gson gson = new Gson();
		return gson.toJson(invertedIndex);
	}
	
	public static Map<Integer, HtmlParsedData> fromJson(String str) {
		Gson gson = new Gson();
		Type mapType = new TypeToken<Map<Integer, HtmlParsedData>>() {}.getType();
		Map<Integer, HtmlParsedData> map = gson.fromJson(str, mapType);		
		return map;
	}
	
	public static Map<String, Map<Integer, Integer>> invertedIndexFromJson(String str) {
		Gson gson = new Gson();
		Type mapType = new TypeToken<Map<String, Map<Integer, Integer>>>() {}.getType();
		Map<String, Map<Integer, Integer>> map = gson.fromJson(str, mapType);		
		return map;
	}
	
	public static String searchResultToJson(List<HtmlParsedData> searchResult) {
		Gson gson = new Gson();
		return gson.toJson(searchResult);
	}
}
