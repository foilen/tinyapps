package ca.pgon.freenetknowledge.action;

/**
 * This interface defines a class that will create new actions for the ActionController.
 */
public interface ActionGenerator {

    /**
     * This method creates a new action and returns it.
     *
     * @return an action as a Runnable
     */
    Runnable getOne();

}
