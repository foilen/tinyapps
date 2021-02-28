/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ca.pgon.chaincommander.modes.ModeConstants;

public class ConfigManagerTest {

    @Test
    public void testGet100Percent() {
        new ConfigManager();
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ConfigManager#validateAllConfigured(java.util.Map)} .
     */
    @Test
    public void testValidateAllConfiguredNone() {
        Map<String, String> configMap = new HashMap<>();
        Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ConfigManager#validateAllConfigured(java.util.Map)} .
     */
    @Test
    public void testValidateAllConfiguredMaster() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put(ConfigManager.MODE, ModeConstants.MASTER);
        configMap.put(ConfigManager.PORT, "1234");
        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ConfigManager#validateAllConfigured(java.util.Map)} .
     */
    @Test
    public void testValidateAllConfiguredSlaveFail() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put(ConfigManager.MODE, ModeConstants.SLAVE);
        configMap.put(ConfigManager.PORT, "1234");
        Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ConfigManager#validateAllConfigured(java.util.Map)} .
     */
    @Test
    public void testValidateAllConfiguredSlave() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put(ConfigManager.MODE, ModeConstants.SLAVE);
        configMap.put(ConfigManager.MASTER_NODE_INFO, "127.0.0.1:1234");
        configMap.put(ConfigManager.PORT, "1234");
        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

}
