import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlParser {
	
	public static HtmlParsedData parse(String link, String html) {
		try {
			Document doc = Jsoup.parse(html);
	        String title = doc.select("head").select("title").text();
	        String description = doc.select("head").select("meta[property='og:description']").attr("content");
	        if (description.isEmpty()) {
	        	description = title + "...";
	        }
	        String text = doc.select("body").text();
	        
	        List<String> urls = new ArrayList<>();
	        for(Element e : doc.select("body").select("a")) {
	        	String url = e.attr("href");
	        	if (!url.isEmpty() && isValid(url)) {
	        		urls.add(url);
	        	}
	        }
	        
	        return new HtmlParsedData(link, title, description, text, urls);
		} catch (Exception e) {
			return null;
		}
        
    }
	
	private static boolean isValid(String url) {
        try { 
            new URL(url).toURI(); 
            return true; 
        } catch (Exception e) { 
            return false; 
        } 
	}
}
