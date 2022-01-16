import java.util.ArrayList;
import java.util.List;

public class HtmlParsedData {
	String link;
	String title;
	String description;
	String text;
	List<String> outgoingUrls = new ArrayList<>();
	
	HtmlParsedData(String link, String title, String description, String text, List<String> outgoingUrls) {
		this.link = link;
		this.title = title;
		this.description = description;
		this.text = text;
		this.outgoingUrls = outgoingUrls;
	}
	
	@Override
	public String toString() {
		return String.format("link: %s \ntitle: %s \ndescription: %s", link, title, description);
	}
}
