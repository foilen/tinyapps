/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

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

import org.apache.commons.lang3.StringUtils;

import ca.pgon.chaincommander.configurators.exceptions.BadArgumentsException;
import ca.pgon.chaincommander.modes.ModeConstants;

/**
 * Configure the software via the command line arguments.
 */
public class ArgumentsConfigurator implements Configurator {

    public static final String MASTER_MODE = "-m";
    public static final String SLAVE_MODE = "-s";
    public static final String MASTER_NODE_INFO = "-h";
    public static final String PORT = "-p";

    private String[] args;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(Map<String, String> configMap) {
        if (args == null || configMap == null) {
            return;
        }

        for (int i = 0; i < args.length; ++i) {
            // Check that it starts with a "-"
            String next = args[i];
            if (!StringUtils.startsWith(next, "-")) {
                throw new BadArgumentsException("The argument [" + next + "] must start with -");
            }

            // Get the right
            String param;
            switch (next) {
            case MASTER_MODE:
                configMap.put(ConfigManager.MODE, ModeConstants.MASTER);
                break;
            case SLAVE_MODE:
                configMap.put(ConfigManager.MODE, ModeConstants.SLAVE);
                break;
            case MASTER_NODE_INFO:
                ++i;
                if (i >= args.length) {
                    throw new BadArgumentsException("Missing the master node info after " + MASTER_NODE_INFO);
                }

                param = args[i];
                // Check that it does not starts with a "-"
                if (StringUtils.startsWith(param, "-")) {
                    throw new BadArgumentsException("The value of [" + next + "] must not start with -. It is [" + param + "]");
                }
                configMap.put(ConfigManager.MASTER_NODE_INFO, param);
                break;
            case PORT:
                ++i;
                if (i >= args.length) {
                    throw new BadArgumentsException("Missing the port after " + PORT);
                }

                param = args[i];
                // Check that it does not starts with a "-"
                if (StringUtils.startsWith(param, "-")) {
                    throw new BadArgumentsException("The value of [" + next + "] must not start with -. It is [" + param + "]");
                }
                configMap.put(ConfigManager.PORT, param);
                break;
            default:
                throw new BadArgumentsException("Unknown argument [" + next + "]");
            }
        }
    }

    /**
     * @return the args
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @param args
     *            the args to set
     */
    public void setArgs(String[] args) {
        this.args = args;
    }
}
