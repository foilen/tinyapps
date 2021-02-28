/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.action;

/**
 * This class will populate the ActionController with new items indefinitely.
 */
public class ActionControllerFillerThread extends Thread {

    private ActionController actionController;

    /**
     * The constructor.
     *
     * @param actionController
     *            the ActionController that created it.
     */
    public ActionControllerFillerThread(ActionController actionController) {
        this.actionController = actionController;
    }

    /**
     * This method will request the creation of new actions indefinitely.
     */
    @Override
    public void run() {
        while (true) {
            actionController.fill();
        }
    }

}
