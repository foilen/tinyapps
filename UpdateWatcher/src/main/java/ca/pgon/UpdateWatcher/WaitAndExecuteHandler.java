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
package ca.pgon.UpdateWatcher;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ca.pgon.st.light.Console;
import ca.pgon.st.light7.filesystemupdatewatcher.FileSystemUpdateHandler;

/**
 * Waits a certain amount of time after the last event to execute a command.
 */
public class WaitAndExecuteHandler extends Thread implements FileSystemUpdateHandler {

    private int delayInSec;
    private String command;

    private AtomicInteger countDown = new AtomicInteger();
    private AtomicBoolean waitingForExecution = new AtomicBoolean(false);

    public WaitAndExecuteHandler(int delayInSec, String command) {
        this.delayInSec = delayInSec;
        this.command = command;
        start();
    }

    @Override
    public void created(File file) {
        touch(file);
    }

    @Override
    public void deleted(File file) {
        touch(file);
    }

    @Override
    public void modified(File file) {
        touch(file);
    }

    @Override
    public void run() {
        for (;;) {
            try {
                // Wait one second
                Thread.sleep(1000);

                // Decrement and check if time has come
                if (waitingForExecution.get()) {
                    int value = countDown.decrementAndGet();
                    if (value < 0) {
                        waitingForExecution.set(false);

                        // Execute
                        System.out.println("Execute: " + command);
                        System.out.println();
                        Console.executeAndWait(command);
                        System.out.println();
                        System.out.println("Execution Completed");
                        System.out.println();
                    }
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void touch(File file) {

        System.out.println("Event on: " + file.getAbsolutePath() + " waiting " + delayInSec + " seconds");

        countDown.set(delayInSec);
        waitingForExecution.set(true);
    }

}
