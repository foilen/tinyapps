/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

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
