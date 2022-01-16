import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileStore {

    public static void save(String content, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();
    }
    
    public static String read(String fileName) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(fileName));
    	String input;
        StringBuffer content = new StringBuffer();
        while ((input = reader.readLine()) != null) {
            content.append(input);
        }
        reader.close();
        return content.toString();
    }
}
