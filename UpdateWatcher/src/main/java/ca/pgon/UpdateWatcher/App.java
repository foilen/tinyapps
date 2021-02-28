/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.UpdateWatcher;

import java.io.IOException;

import ca.pgon.st.light.JavaEnvironmentValues;
import ca.pgon.st.light7.filesystemupdatewatcher.FileSystemUpdateWatcher;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Check the arguments
        if (args.length < 1) {
            System.out.println("Usage: <delay_after_last_update_in_seconds> command");
            return;
        }

        int delayInSec = 0;
        String command;

        if (args.length == 1) {
            command = args[0];
        } else {
            delayInSec = Integer.valueOf(args[0]);
            command = args[1];
        }

        // Start the system
        new FileSystemUpdateWatcher(JavaEnvironmentValues.getWorkingDirectory()).setRecursive(true).addHandler(new WaitAndExecuteHandler(delayInSec, command)).init();

    }
}
