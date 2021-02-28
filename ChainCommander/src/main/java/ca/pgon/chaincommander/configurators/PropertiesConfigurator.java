/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Configure the software via property file.
 */
public class PropertiesConfigurator implements Configurator {

    private String fileName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(Map<String, String> configMap) {
        if (fileName == null || configMap == null) {
            return;
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(fileName));
            for (Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                configMap.put(key, value);
            }
        } catch (IOException e) {
            return;
        }

    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
