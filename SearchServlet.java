

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUM = 10;
	private static final Map<Integer, HtmlParsedData> linkWithData = new HashMap<>();
	private static final Map<String, Map<Integer, Integer>> convertedInvertedIndex = new HashMap<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("search");
		
		populateMapOnce();
		
		response.getWriter().append(search(keyword));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void populateMapOnce() {
		System.out.println("Call populateMapOnce");
		try {
			if (convertedInvertedIndex.isEmpty()) {
				String invertedIndexStr = readFromResource("/WEB-INF/invertedIndex.txt");
				System.out.println(invertedIndexStr.length());
				convertedInvertedIndex.putAll(JsonConverter.invertedIndexFromJson(invertedIndexStr));
			}
			if (linkWithData.isEmpty()) {
				String linkWithDataStr = readFromResource("/WEB-INF/metadata.txt");
				System.out.println(linkWithDataStr.length());
				linkWithData.putAll(JsonConverter.fromJson(linkWithDataStr));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String search(String keyword) throws IOException {
    	Map<String, List<Integer>> tempMap = new HashMap<>();
    	for (String key : convertedInvertedIndex.keySet()) {
    		List<Integer> list = new ArrayList<>();
    		for (int u : convertedInvertedIndex.get(key).keySet()) {
    			list.add(u);
    		}
    		Collections.sort(list, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return convertedInvertedIndex.get(key).get(o2) - convertedInvertedIndex.get(key).get(o1);
				}
    		});
    		tempMap.put(key, list);
    	}
    	if (tempMap.containsKey(keyword)) {
    		List<HtmlParsedData> retList = new ArrayList<>();
    		for (int i = 0; i < Math.min(tempMap.get(keyword).size(), MAX_NUM); i++) {
    			linkWithData.get(tempMap.get(keyword).get(i)).text = "";
    			retList.add(linkWithData.get(tempMap.get(keyword).get(i)));
    		}
    		return JsonConverter.searchResultToJson(retList);
    	} else {
    		return "";
    	}
    }
	
	private String readFromResource(String fileName) throws IOException {
    	StringBuffer content = new StringBuffer();
    	InputStream is = getServletContext().getResourceAsStream(fileName);
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String text;
            while ((text = reader.readLine()) != null) {
            	content.append(text);
            }
            reader.close();
        }
        return content.toString();
    }

}
