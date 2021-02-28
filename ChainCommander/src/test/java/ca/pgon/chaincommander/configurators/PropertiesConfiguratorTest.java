/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.pgon.helpers.FileHelper;

/**
 * @author user
 * 
 */
public class PropertiesConfiguratorTest {

    private PropertiesConfigurator propertiesConfigurator;
    private Map<String, String> configMap;

    @Before
    public void setUp() {
        propertiesConfigurator = new PropertiesConfigurator();
        configMap = new HashMap<>();
    }

    @Test
    public void testGet100Percent() {
        String filename = "thename";
        propertiesConfigurator.setFileName(filename);
        Assert.assertEquals(filename, propertiesConfigurator.getFileName());
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureNoFile() {
        propertiesConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureNoFileNull() {
        propertiesConfigurator.configure(null);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureNull() {
        propertiesConfigurator.setFileName("/tmp");
        propertiesConfigurator.configure(null);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureNotExistingFile() {
        propertiesConfigurator.setFileName("/tmp/not/existing");
        propertiesConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     * 
     * @throws IOException
     */
    @Test
    public void testConfigureNotComplete() throws IOException {
        String content = "mode=master";
        File file = File.createTempFile("junit", null);
        Assert.assertTrue(FileHelper.saveFile(file, content));

        propertiesConfigurator.setFileName(file.getAbsolutePath());
        propertiesConfigurator.configure(configMap);

        Assert.assertEquals(1, configMap.size());
        Assert.assertEquals("master", configMap.get("mode"));

        Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.PropertiesConfigurator#configure(java.util.Map)} .
     * 
     * @throws IOException
     */
    @Test
    public void testConfigureMasterComplete() throws IOException {
        String content = "mode=master\nport=10";
        File file = File.createTempFile("junit", null);
        Assert.assertTrue(FileHelper.saveFile(file, content));

        propertiesConfigurator.setFileName(file.getAbsolutePath());
        propertiesConfigurator.configure(configMap);

        Assert.assertEquals(2, configMap.size());
        Assert.assertEquals("master", configMap.get("mode"));
        Assert.assertEquals("10", configMap.get("port"));

        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

}
