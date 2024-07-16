/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.action;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The action controller is a Bean that executes actions indefinitely. It is using an ActionGenerator to always fill it with new actions.
 */
public class ActionController {
    private static final Logger logger = Logger.getLogger(ActionController.class.getName());

    private static final long keepAliveTime = 1;
    private static final TimeUnit unit = TimeUnit.MINUTES;
    private static final long WAITING_TIME = 10000; // 10 seconds

    private Executor executor;
    private BlockingQueue<Runnable> workQueue;

    private int corePoolSize;
    private int maximumPoolSize;
    private ActionGenerator actionGenerator;

    /**
     * This method is called by the ActionControllerFillerThread to request new actions.
     */
    public void fill() {

        // Check if there are some place left in the queue
        if (workQueue.remainingCapacity() > 0) {
            logger.fine("Getting new action");

            // Get a new action
            Runnable action = actionGenerator.getOne();
            if (action == null) {
                // There are none available so sleep
                logger.fine("No action available");
                try {
                    Thread.sleep(WAITING_TIME);
                } catch (InterruptedException e) {
                }
            } else {
                // Add to the queue
                logger.fine("One action added");
                executor.execute(action);
            }

        } else {

            // There is no place left in the queue so sleep
            logger.fine("The queue is full for now");
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
            }
        }

    }

    /**
     * @return the actionGenerator
     */
    public ActionGenerator getActionGenerator() {
        return actionGenerator;
    }

    /**
     * @return the corePoolSize
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * @return the maximumPoolSize
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * Initialization that creates the executor and starts the action filler.
     */
    @PostConstruct
    public void init() {
        logger.info("Initialization of the ActionController");

        workQueue = new ArrayBlockingQueue<Runnable>(maximumPoolSize * 5);

        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

        new ActionControllerFillerThread(this).start();

        logger.info("Initialization of the ActionController completed");
    }

    /**
     * @param actionGenerator
     *            the actionGenerator to set
     */
    public void setActionGenerator(ActionGenerator actionGenerator) {
        this.actionGenerator = actionGenerator;
    }

    /**
     * @param corePoolSize
     *            the corePoolSize to set
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * @param maximumPoolSize
     *            the maximumPoolSize to set
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

}
