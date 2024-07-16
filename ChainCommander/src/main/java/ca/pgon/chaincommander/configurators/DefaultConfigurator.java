/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.util.Map;

import ca.pgon.chaincommander.modes.ModeConstants;

/**
 * Configure the software via some default values.
 */
public class DefaultConfigurator implements Configurator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(Map<String, String> configMap) {
        configMap.put(ConfigManager.PORT, "1000");
        configMap.put(ConfigManager.MODE, ModeConstants.MASTER);
    }
}
