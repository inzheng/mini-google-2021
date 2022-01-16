import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

	private static final Map<Integer, HtmlParsedData> linkWithData = new HashMap<>();
	private static final Map<String, Map<Integer, Integer>> invertedIndex = new HashMap<>();
	private static final Map<String, List<Integer>> convertedInvertedIndex = new HashMap<>();
	private static final int MAX_DEPTH = 2;
	private static int number = 0;
	
    public static void main(String[] args) throws IOException, InterruptedException {
        //String link = "https://www.brandeis.edu/";
        //String output = HttpFetcher.getResponse(link);
        //String filePath = "data/brandeis.html";
        //FileStore.save(output, filePath);
    	//FileStore.save(HttpFetcher.getResponse(link2), "data/sitemap.html");
    	//String seedUrl = "https://www.brandeis.edu/search/sitemap.html";
    	//bfs(seedUrl);
    	//convertSearchIndex();
    	//String jsonLinkWithData = JsonConverter.toJson(linkWithData);
    	//FileStore.save(jsonLinkWithData, "data/linkWithData.txt");
    	//String jsonInvertedIndex = JsonConverter.invertedIndexToJson(invertedIndex);
    	//FileStore.save(jsonInvertedIndex, "data/invertedIndex.txt");
    	//System.out.println(search("employee"));
    	List<HtmlParsedData> list = search("student");
    	for (HtmlParsedData d : list) {
    		System.out.println(d);
    	}
    }
    
    public static List<HtmlParsedData> search(String keyword) throws IOException {
    	String invertedIndexStr = FileStore.read("data/invertedIndex.txt");
    	Map<String, Map<Integer, Integer>> tempInvertedIndex = JsonConverter.invertedIndexFromJson(invertedIndexStr);
    	String linkWithData = FileStore.read("data/linkWithData.txt");
    	Map<Integer, HtmlParsedData> tempLinkWithData = JsonConverter.fromJson(linkWithData);
    	Map<String, List<Integer>> tempMap = new HashMap<>();
    	for (String key : tempInvertedIndex.keySet()) {
    		List<Integer> list = new ArrayList<>();
    		for (int u : tempInvertedIndex.get(key).keySet()) {
    			list.add(u);
    		}
    		Collections.sort(list, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return tempInvertedIndex.get(key).get(o2) - tempInvertedIndex.get(key).get(o1);
				}
    		});
    		tempMap.put(key, list);
    	}
    	if (tempMap.containsKey(keyword)) {
    		List<HtmlParsedData> retList = new ArrayList<>();
    		for (int i = 0; i < Math.min(tempMap.get(keyword).size(), 10); i++) {
    			retList.add(tempLinkWithData.get(tempMap.get(keyword).get(i)));
    		}
    		return retList;
    	} else {
    		return new LinkedList<>();
    	}
    }
    
    private static void bfs(String seed) throws InterruptedException {
    	Map<String, Integer> visitedLinks = new HashMap<>();
    	LinkedList<String> toVisitLinks = new LinkedList<>();
    	visitedLinks.put(seed, 0);
    	toVisitLinks.addLast(seed);
    	while(!toVisitLinks.isEmpty()) {
    		String link = toVisitLinks.removeFirst();
    		int curDepth = visitedLinks.get(link);
    		List<String> outgoingUrls = process(link);
    		if (curDepth < MAX_DEPTH) {
    			for (String url : outgoingUrls) {
        			if (!visitedLinks.containsKey(url)) {
        				visitedLinks.put(url, curDepth + 1);
        				toVisitLinks.push(url);
        			}
        		}
    		}
    		
    	}
    }
    
    private static List<String> process(String link) throws InterruptedException {
    	Thread.sleep(1000);
    	System.out.println("Start processing " + link);
    	String output = HttpFetcher.getResponse(link);
    	if (output == null) {
    		System.out.println("failed");
    		return new LinkedList<>();
    	}
    	//String output = FileStore.read("data/sitemap.html");
        HtmlParsedData data = HtmlParser.parse(link, output);
        if (data == null) {
        	System.out.println("failed");
    		return new LinkedList<>();
        }
        List<String> retList = new ArrayList<>(data.outgoingUrls);
        data.outgoingUrls.clear();
        linkWithData.put(number, data);
        Map<String, Integer> freq = WordCounter.count(data.text);
        for (String key : freq.keySet()) {
        	Map<Integer, Integer> map = invertedIndex.getOrDefault(key, new HashMap<>());
        	map.put(number, freq.get(key));
        	invertedIndex.put(key, map);
        }
        number++;
        System.out.println("Successful processing " + link);
        return retList;
    }
    
    private static void convertSearchIndex() {
    	for (String word : invertedIndex.keySet()) {
    		Map<Integer, Integer> map = invertedIndex.get(word);
    		List<Integer> listOfUrl = new ArrayList<>();
    		for (int url : map.keySet()) {
    			listOfUrl.add(url);
    		}
    		Collections.sort(listOfUrl, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return map.get(o1) - map.get(o2);
				}
    		});
    		convertedInvertedIndex.put(word, listOfUrl);
    	}
    }
}
