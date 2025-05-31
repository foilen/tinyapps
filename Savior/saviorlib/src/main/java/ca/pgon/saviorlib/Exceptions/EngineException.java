package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class EngineException extends RuntimeException {
    private Exception exception;
    
    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Exception exception) {
        this.exception = exception;
    }
    
    public Exception getExeption() {
        return exception;
    }
}
