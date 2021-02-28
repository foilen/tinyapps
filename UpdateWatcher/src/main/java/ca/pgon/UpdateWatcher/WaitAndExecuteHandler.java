/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
