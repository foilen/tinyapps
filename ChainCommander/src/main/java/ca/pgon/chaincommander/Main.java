/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ca.pgon.chaincommander.configurators.ArgumentsConfigurator;
import ca.pgon.chaincommander.configurators.ConfigManager;
import ca.pgon.chaincommander.configurators.DefaultConfigurator;
import ca.pgon.chaincommander.configurators.PropertiesConfigurator;

/**
 * The entry point of the jar.
 */
public class Main {

    private static final String PROPERTIES_FILE_NAME = "chaincommander.properties";

    /**
     * Main entry point.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Configure the application
        Map<String, String> configMap = new ConcurrentHashMap<String, String>();

        DefaultConfigurator defaultConfigurator = new DefaultConfigurator();
        defaultConfigurator.configure(configMap);

        PropertiesConfigurator propertiesConfigurator = new PropertiesConfigurator();
        propertiesConfigurator.setFileName(PROPERTIES_FILE_NAME);
        propertiesConfigurator.configure(configMap);

        ArgumentsConfigurator argumentsConfigurator = new ArgumentsConfigurator();
        argumentsConfigurator.configure(configMap);

        // Validate that we have all the needed parameters
        if (!ConfigManager.validateAllConfigured(configMap)) {
            System.err.println("Missing some mandatory properties");
            return;
        }
    }

}
