package ca.pgon.saviorlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class TestTools {
    static public void createFile(File file, String content) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.print(content);
        out.close();
    }
    
    static public File createTempFile(String content) throws IOException {
        File result = File.createTempFile("junit", null);
        
        createFile(result, content);
        
        return result;
    }
    
    static public String createTempDir() throws IOException {
        File result = File.createTempFile("junit", "");
        
        result.delete();
        result.mkdir();
        
        return result.getAbsolutePath();
    }
}
