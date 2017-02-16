/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
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
