package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface ProgressEvent {
    void progressEventHandler(FileEntry destination, long currentPos);
}
