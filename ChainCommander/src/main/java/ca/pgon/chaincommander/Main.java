/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
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
