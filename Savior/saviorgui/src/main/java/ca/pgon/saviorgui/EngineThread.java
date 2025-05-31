package ca.pgon.saviorgui;

import ca.pgon.saviorlib.Engines.Engine;

public class EngineThread extends Thread {
    private Engine engine;
    
    public EngineThread(Engine engine) {
        this.engine = engine;
    }
    
    @Override
    public void run() {
        engine.start();
    }
}
