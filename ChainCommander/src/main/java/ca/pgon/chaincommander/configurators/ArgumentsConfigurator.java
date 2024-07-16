/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
