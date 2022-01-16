import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFetcher {

    public static String getResponse(String link) {
    	try {
    		// Establish connection
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            // Send request
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();

            // Handle response
            // int statusCode = con.getResponseCode();
            // System.out.println(statusCode);

            BufferedReader in = new BufferedReader((new InputStreamReader(con.getInputStream())));
            String input;
            StringBuffer content = new StringBuffer();
            while ((input = in.readLine()) != null) {
                content.append(input);
            }
            in.close();

            // Close connection
            con.disconnect();

            return content.toString();
    	} catch (Exception e) {
    		return null;
    	}
        
    }
}
