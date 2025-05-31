package ca.pgon.freenetknowledge.repository.starter;

/**
 * This interface cleans the table at startup and add some default entries if needed.
 */
public interface Starter {
    /**
     * Called when the application is starting. Should check the DB consistency and create some entries if needed.
     */
    void startup();
}
