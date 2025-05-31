package ca.pgon.saviorlib.Exceptions;

@SuppressWarnings("serial")
public class EnginePauseException extends Exception {
    private long bytesRead;
    
    public EnginePauseException(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getBytesRead() {
        return bytesRead;
    }
}
