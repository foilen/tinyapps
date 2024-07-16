/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.CheckMods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ca.pgon.saviorlib.TestTools;
import ca.pgon.saviorlib.FileSystems.FileEntry;
import ca.pgon.saviorlib.FileSystems.FileSystem;
import ca.pgon.saviorlib.FileSystems.FileSystemTools;
import ca.pgon.saviorlib.FileSystems.LocalFS;
import ca.pgon.saviorlib.FileSystems.LocalFSTest;

public class CheckModsTest {
    FileEntry a, b, c;
    
    @Before
    public void setUp() {
        a = FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 2000, 1000);
        b = FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 3000, 500);
        c = FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 1000, 300);
    }

    @Test
    public void dateCM() {
        DateCM dateCM = new DateCM();
        dateCM.setType(DateCM.Type.NOTSAME);
        assertFalse(dateCM.needUpdate(a, a));
        assertTrue(dateCM.needUpdate(a, b));
        assertTrue(dateCM.needUpdate(a, c));
        assertTrue(dateCM.needUpdate(b, a));
        assertFalse(dateCM.needUpdate(b, b));
        assertTrue(dateCM.needUpdate(b, c));
        assertTrue(dateCM.needUpdate(c, a));
        assertTrue(dateCM.needUpdate(c, b));
        assertFalse(dateCM.needUpdate(c, c));
        
        dateCM.setType(DateCM.Type.DESTINATION_OLDER);
        assertFalse(dateCM.needUpdate(a, a));
        assertFalse(dateCM.needUpdate(a, b));
        assertTrue(dateCM.needUpdate(a, c));
        assertTrue(dateCM.needUpdate(b, a));
        assertFalse(dateCM.needUpdate(b, b));
        assertTrue(dateCM.needUpdate(b, c));
        assertFalse(dateCM.needUpdate(c, a));
        assertFalse(dateCM.needUpdate(c, b));
        assertFalse(dateCM.needUpdate(c, c));
    }
    
    @Test
    public void sizeCM() {
        SizeCM sizeCM = new SizeCM();
        assertFalse(sizeCM.needUpdate(a, a));
        assertTrue(sizeCM.needUpdate(a, b));
        assertTrue(sizeCM.needUpdate(a, c));
        assertTrue(sizeCM.needUpdate(b, a));
        assertFalse(sizeCM.needUpdate(b, b));
        assertTrue(sizeCM.needUpdate(b, c));
        assertTrue(sizeCM.needUpdate(c, a));
        assertTrue(sizeCM.needUpdate(c, b));
        assertFalse(sizeCM.needUpdate(c, c));
    }
    
    @Test
    public void md5sum() throws IOException {
        File file = TestTools.createTempFile("This is the content of the file. It is wonderfull");
        String actual = MD5CM.computeMD5(new FileInputStream(file));
        file.delete();

        assertEquals("ad909610e858ccd382af46a724814669", actual);
    }
    
    @Test
    public void md5CM() throws IOException {
        MD5CM md5CM = new MD5CM();
        
        // Create structure
        String directory = TestTools.createTempDir();
        LocalFSTest.createTestFiles(directory);
        
        // Setup File System
        FileSystem fs = new LocalFS();
        fs.setBasePath(directory);
        fs.checkIfValid();
        
        FileEntry that_file_file = FileSystemTools.createFileEntry(fs, false, "First directory/Sub directory", "That file.txt", 0, 0);
        FileEntry testing_file = FileSystemTools.createFileEntry(fs, false, "", "Testing.txt", 0, 0);
        
        // Tests
        assertTrue(md5CM.needUpdate(that_file_file, testing_file));
        assertFalse(md5CM.needUpdate(that_file_file, that_file_file));
    }
}
