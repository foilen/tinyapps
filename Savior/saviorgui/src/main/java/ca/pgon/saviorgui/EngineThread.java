/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
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
