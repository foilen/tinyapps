/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorgui.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProfilesManagerTest {
    
    public ProfilesManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void saveAndLoadProfile() throws IOException {
        // Create the profile
        Profile profile = new Profile();
        
        profile.sourceFileSystemType = "Local";
        profile.sourceBasePath = "/tmp/source";
        profile.sourceParams = null;
        
        profile.destinationFileSystemType = "FTP";
        profile.destinationBasePath = "/tmp/destination";
        profile.destinationParams = new HashMap<String, String>();
        profile.destinationParams.put("host", "127.0.0.1");
        profile.destinationParams.put("port", "21");
        
        profile.modDate = true;
        
        // Save the profile
        File file = File.createTempFile("junit", "");
        assertTrue(ProfilesManager.save(file.getAbsolutePath(), profile));
        
        // Load the profile
        Profile result = ProfilesManager.load(file.getAbsolutePath());
        
        // Compare
        assertNotNull(result);
        
        assertEquals("Local", result.sourceFileSystemType);
        assertEquals("/tmp/source", result.sourceBasePath);
        assertEquals(0, result.sourceParams.size());
        
        assertEquals("FTP", result.destinationFileSystemType);
        assertEquals("/tmp/destination", result.destinationBasePath);
        assertEquals(2, result.destinationParams.size());
        assertEquals("127.0.0.1", result.destinationParams.get("host"));
        assertEquals("21", result.destinationParams.get("port"));
        
        assertEquals(true, result.modDate);
        assertEquals(false, result.modSize);
        assertEquals(false, result.modMD5);
    }
}
