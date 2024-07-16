/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.chaincommander.configurators;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import ca.pgon.chaincommander.modes.ModeConstants;

/**
 * The list of used properties.
 */
public class ConfigManager {

    private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());

    // Mandatory
    public static final String MODE = "mode";
    public static final String PORT = "port";

    // Mandatory if slave
    public static final String MASTER_NODE_INFO = "masterModeInfo";

    // Optional
    public static final String MAX_UPLOAD_SPEED = "mode";

    // During the execution
    public static final String NEXT_NODE_INFO = "nextNodeInfo";
    public static final String LAST_NODE_INFO = "lastNodeInfo";

    public static boolean validateAllConfigured(Map<String, String> configMap) {
        // Validate mandatory
        for (String property : new String[] { MODE, PORT }) {
            if (StringUtils.isEmpty(configMap.get(property))) {
                logger.log(Level.SEVERE, "Missing the property: {0}", property);
                return false;
            }
        }

        // Validate slave
        if (StringUtils.equals(configMap.get(MODE), ModeConstants.SLAVE)) {
            for (String property : new String[] { MASTER_NODE_INFO }) {
                if (StringUtils.isEmpty(configMap.get(property))) {
                    logger.log(Level.SEVERE, "Missing the property: {0}", property);
                    return false;
                }
            }
        }

        return true;
    }

}
