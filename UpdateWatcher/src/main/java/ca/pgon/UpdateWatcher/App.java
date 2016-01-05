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
