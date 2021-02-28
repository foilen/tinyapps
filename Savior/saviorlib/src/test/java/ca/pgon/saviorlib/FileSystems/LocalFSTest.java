/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.pgon.saviorlib.TestTools;
import ca.pgon.saviorlib.Exceptions.FileSystemException;

public class LocalFSTest {
    String directory;
    
    static public void createTestFiles(String directory) throws IOException {
        /*
            Create this tree
            - First directory/
                    - Sub directory/
                            - That file.txt
                            - Very good.txt
                    - Single file here.txt
            - One file.txt
            - Testing.txt
         */
        String first_directory = directory + File.separatorChar + "First directory";
        String sub_directory = directory + File.separatorChar + "First directory" + File.separatorChar + "Sub directory";
        new File(sub_directory).mkdirs();
        
        TestTools.createFile(new File(sub_directory + File.separatorChar + "That file.txt"), "That file");
        TestTools.createFile(new File(sub_directory + File.separatorChar + "Very good.txt"), "Very good");
        TestTools.createFile(new File(first_directory + File.separatorChar + "Single file here.txt"), "Single file here");
        TestTools.createFile(new File(directory + File.separatorChar + "One file.txt"), "One file");
        TestTools.createFile(new File(directory + File.separatorChar + "Testing.txt"), "Testing");
    }
    
    @Before
    public void setUp() throws IOException {
        directory = TestTools.createTempDir();
        createTestFiles(directory);
    }
    
    @Test
    public void checkIfValidExists() {
        FileSystem fs = new LocalFS();
        
        // Exists
        fs.setBasePath(directory);
        fs.checkIfValid();
    }
    
    @Test(expected=FileSystemException.class)
    public void checkIfValidNotExists() {
        FileSystem fs = new LocalFS();  
        
        // Does not exits
        fs.setBasePath(directory + "0000");
        fs.checkIfValid();
    }
    
    @Test
    public void createDirectory() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);

        // Check
        fs.checkIfValid();
        
        // In base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, true, "", "New directory", 0, 0);
        entry.fileSystem.createDirectory(entry);
        
        List<FileEntry> base = fs.listDirectory(null);
        assertEquals(4, base.size());
        
        Collections.sort(base);
        
        assertEquals(true, base.get(1).isDirectory);
        assertEquals("", base.get(1).path);
        assertEquals("New directory", base.get(1).name);
    }
    
    @Test
    public void deleteDirectory() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);

        // Check
        fs.checkIfValid();
        
        // In base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, true, "", "First directory", 0, 0);
        entry.fileSystem.deleteDirectory(entry);
        
        List<FileEntry> base = fs.listDirectory(null);
        assertEquals(2, base.size());
    }
    
    @Test
    public void listDirectory() {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);
        
        // Check
        fs.checkIfValid();
        
        // Base dir
        List<FileEntry> base = fs.listDirectory(null);
        
        assertEquals(3, base.size());
        
        Collections.sort(base);
        
        assertEquals(true, base.get(0).isDirectory);
        assertEquals("", base.get(0).path);
        assertEquals("First directory", base.get(0).name);
        
        assertEquals(false, base.get(1).isDirectory);
        assertEquals("", base.get(1).path);
        assertEquals("One file.txt", base.get(1).name);
        assertEquals(8, base.get(1).size);
        
        assertEquals(false, base.get(2).isDirectory);
        assertEquals("", base.get(2).path);
        assertEquals("Testing.txt", base.get(2).name);
        assertEquals(7, base.get(2).size);
        
        // First directory dir
        List<FileEntry> first_directory = fs.listDirectory(base.get(0));
        
        assertEquals(2, first_directory.size());
        
        Collections.sort(base);
        
        assertEquals(false, first_directory.get(0).isDirectory);
        assertEquals("First directory", first_directory.get(0).path);
        assertEquals("Single file here.txt", first_directory.get(0).name);
        assertEquals(16, first_directory.get(0).size);
        
        assertEquals(true, first_directory.get(1).isDirectory);
        assertEquals("First directory", first_directory.get(1).path);
        assertEquals("Sub directory", first_directory.get(1).name);
        
        // First directory/Sub directory dir
        List<FileEntry> sub_directory = fs.listDirectory(first_directory.get(1));
        
        assertEquals(2, sub_directory.size());
        
        Collections.sort(base);
        
        assertEquals(false, sub_directory.get(0).isDirectory);
        assertEquals("First directory/Sub directory", sub_directory.get(0).path);
        assertEquals("That file.txt", sub_directory.get(0).name);
        assertEquals(9, sub_directory.get(0).size);
        
        assertEquals(false, sub_directory.get(1).isDirectory);
        assertEquals("First directory/Sub directory", sub_directory.get(1).path);
        assertEquals("Very good.txt", sub_directory.get(1).name);
        assertEquals(9, sub_directory.get(1).size);
    }
    
    @Test
    public void deleteFile() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);

        // Check
        fs.checkIfValid();
        
        // In base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, false, "", "Testing.txt", 0, 0);
        entry.fileSystem.deleteFile(entry);
        
        // In First directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory", "Single file here.txt", 0, 0);
        entry.fileSystem.deleteFile(entry);
        
        // In First directory/Sub directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory/Sub directory", "That file.txt", 0, 0);
        entry.fileSystem.deleteFile(entry);
    }
    
    @Test
    public void createAndAppendFile() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);

        // Check
        fs.checkIfValid();
        
        // Create in base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, false, "", "New file.txt", 0, 0);
        PrintWriter out = new PrintWriter(entry.fileSystem.createFile(entry));
        out.print("New");
        out.close();
        
        List<FileEntry> base = fs.listDirectory(null);
        assertEquals(4, base.size());
        
        Collections.sort(base);
        
        assertEquals(false, base.get(1).isDirectory);
        assertEquals("", base.get(1).path);
        assertEquals("New file.txt", base.get(1).name);
        assertEquals(3, base.get(1).size);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFile(entry)));
        String actual = in.readLine();
        in.close();
        
        assertEquals("New", actual);
        
        // Append in base dir
        out = new PrintWriter(entry.fileSystem.appendFile(entry));
        out.print(" file");
        out.close();
        
        base = fs.listDirectory(null);
        assertEquals(4, base.size());
        
        Collections.sort(base);
        
        assertEquals(false, base.get(1).isDirectory);
        assertEquals("", base.get(1).path);
        assertEquals("New file.txt", base.get(1).name);
        assertEquals(8, base.get(1).size);
        
        in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFile(entry)));
        actual = in.readLine();
        in.close();
        
        assertEquals("New file", actual);
    }
    
    @Test
    public void readFile() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);
        
        // Check
        fs.checkIfValid();
        
        // In base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, false, "", "Testing.txt", 0, 0);
        BufferedReader in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFile(entry)));
        String actual = in.readLine();
        in.close();
        
        assertEquals("Testing", actual);
        
        // In First directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory", "Single file here.txt", 0, 0);
        in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFile(entry)));
        actual = in.readLine();
        in.close();
        
        assertEquals("Single file here", actual);
        
        // In First directory/Sub directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory/Sub directory", "That file.txt", 0, 0);
        in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFile(entry)));
        actual = in.readLine();
        in.close();
        
        assertEquals("That file", actual);
    }
    
    @Test
    public void readFileFrom() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);
        
        // Check
        fs.checkIfValid();
        
        // In base dir
        FileEntry entry = FileSystemTools.createFileEntry(fs, false, "", "Testing.txt", 0, 0);
        BufferedReader in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFileFrom(entry, 2)));
        String actual = in.readLine();
        in.close();
        
        assertEquals("sting", actual);
        
        // In First directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory", "Single file here.txt", 0, 0);
        in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFileFrom(entry, 3)));
        actual = in.readLine();
        in.close();
        
        assertEquals("gle file here", actual);
        
        // In First directory/Sub directory
        entry = FileSystemTools.createFileEntry(fs, false, "First directory/Sub directory", "That file.txt", 0, 0);
        in = new BufferedReader(new InputStreamReader(entry.fileSystem.readFileFrom(entry, 4)));
        actual = in.readLine();
        in.close();
        
        assertEquals(" file", actual);
    }
    
    @Test
    public void changeFileModificationTime() throws IOException {
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);
        
        // Check
        fs.checkIfValid();
        
        // Get the current time
        List<FileEntry> base = fs.listDirectory(null);        
        Collections.sort(base);
        FileEntry fileEntry = base.get(2);
        long initialTime = fileEntry.modificationTime;
        long newTime = new Date().getTime() - 1000*60*60;
        
        // Change the time
        fileEntry.fileSystem.changeFileModificationTime(fileEntry, newTime);
        base = fs.listDirectory(null);        
        Collections.sort(base);
        fileEntry = base.get(2);
        
        assertTrue(fileEntry.modificationTime < initialTime);
        
        assertTrue("The time is not at the second", newTime - fileEntry.modificationTime < 1000);
    }
}
