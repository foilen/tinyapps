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
import org.junit.Before;
import org.junit.Test;

import ca.pgon.chaincommander.configurators.exceptions.BadArgumentsException;

public class ArgumentsConfiguratorTest {

    private ArgumentsConfigurator argumentsConfigurator;
    private Map<String, String> configMap;

    @Before
    public void setUp() {
        argumentsConfigurator = new ArgumentsConfigurator();
        configMap = new HashMap<>();
    }

    @Test
    public void testGet100Percent() {
        String[] someArgs = new String[] { "a" };
        argumentsConfigurator.setArgs(someArgs);
        Assert.assertArrayEquals(someArgs, argumentsConfigurator.getArgs());
        argumentsConfigurator.configure(null);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureNothing() {
        argumentsConfigurator.configure(configMap);
        Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureUnknown() {
        argumentsConfigurator.setArgs(new String[] { "-unknown" });
        argumentsConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureNotStartingWithDash() {
        argumentsConfigurator.setArgs(new String[] { "unknown" });
        argumentsConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureMasterComplete() {
        argumentsConfigurator.setArgs(new String[] { "-m", "-p", "1234" });
        argumentsConfigurator.configure(configMap);
        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureSlaveIncomplete() {
        argumentsConfigurator.setArgs(new String[] { "-s" });
        argumentsConfigurator.configure(configMap);
        Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureSlaveMissingHost() {
        argumentsConfigurator.setArgs(new String[] { "-s", "-h" });
        argumentsConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureSlaveMissingPort() {
        argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p" });
        argumentsConfigurator.configure(configMap);
        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureSlaveBadHostArguments() {
        argumentsConfigurator.setArgs(new String[] { "-h", "-s" });
        argumentsConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test(expected = BadArgumentsException.class)
    public void testConfigureSlaveBadPortArguments() {
        argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p", "-missing" });
        argumentsConfigurator.configure(configMap);
    }

    /**
     * Test method for {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)} .
     */
    @Test
    public void testConfigureSlaveComplete() {
        argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p", "1234" });
        argumentsConfigurator.configure(configMap);
        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

}
