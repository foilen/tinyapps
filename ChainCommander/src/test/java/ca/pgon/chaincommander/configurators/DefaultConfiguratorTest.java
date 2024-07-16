/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author user
 * 
 */
public class DefaultConfiguratorTest {

    /**
     * The defaults must be sufficient to run.
     */
    @Test
    public void testMustBeComplete() {
        Map<String, String> configMap = new HashMap<>();
        DefaultConfigurator defaultConfigurator = new DefaultConfigurator();
        defaultConfigurator.configure(configMap);

        Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
    }

}
