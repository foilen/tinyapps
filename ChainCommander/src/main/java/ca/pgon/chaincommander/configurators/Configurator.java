/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.util.Map;

/**
 * To configure the application's instance.
 */
public interface Configurator {

    /**
     * Asks to configure the application.
     * 
     * @param configMap
     *            the current configuration that will be updated
     */
    void configure(Map<String, String> configMap);
}
