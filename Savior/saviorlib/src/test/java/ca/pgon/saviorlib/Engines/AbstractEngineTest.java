/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ca.pgon.saviorlib.TestTools;
import ca.pgon.saviorlib.FileSystems.FileEntry;
import ca.pgon.saviorlib.FileSystems.FileSystem;
import ca.pgon.saviorlib.FileSystems.LocalFS;
import ca.pgon.saviorlib.FileSystems.LocalFSTest;

public class AbstractEngineTest {
    private Engine engine = new AbstractEngine() {
        private int addCount = 0;
        private int deleteCount = 0;
        private int modifiedCount = 0;

        @Override
        protected void processDelete(FileEntry destination) {
            ++deleteCount;
            if (destination.path.equals("First directory/Sub directory") && destination.name.equals("Very good.txt")) return;
            
            fail("Request deleting for " + destination.path + " | " + destination.name);
        }

        @Override
        protected void processAlreadyExists(FileEntry source, FileEntry destination) {
            ++modifiedCount;
        }

        @Override
        protected void processAdd(FileEntry source) {
            ++addCount;
            if (source.path.equals("First directory/Sub directory") && source.name.equals("That file.txt")) return;
            
            fail("Request adding for " + source.path + " | " + source.name);
        }

        @Override
        public void start() {
            checkValidEngine();
            processDirectory(null);
            
            assertEquals("Not the right amount of added", 1, addCount);
            assertEquals("Not the right amount of deleted", 1, deleteCount);
            assertEquals("Not the right amount of modified", 5, modifiedCount);
        }
    };
    
    @Test
    public void processTest() throws IOException {
        // Create 2 file system
        String sourceDir = TestTools.createTempDir();
        LocalFSTest.createTestFiles(sourceDir);
        
        String destinationDir = TestTools.createTempDir();
        LocalFSTest.createTestFiles(destinationDir);
        
        // Remove, add and modify some files to the destination
        assertTrue(new File(destinationDir + File.separatorChar + "First directory" + File.separatorChar + "Sub directory" + File.separatorChar + "That file.txt").delete());
        assertTrue(new File(sourceDir + File.separatorChar + "First directory" + File.separatorChar + "Sub directory" + File.separatorChar + "Very good.txt").delete());
        
        
        // Process
        FileSystem sourceFS = new LocalFS();
        sourceFS.setBasePath(sourceDir);
        engine.setSourceFileSystem(sourceFS);
        
        FileSystem destinationFS = new LocalFS();
        destinationFS.setBasePath(destinationDir);
        engine.setDestinationFileSystem(destinationFS);
        
        engine.start();
        
    }
}
