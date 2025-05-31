package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class FileSystemException extends RuntimeException {
    private Exception exception;
    
    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getExeption() {
        return exception;
    }
}
