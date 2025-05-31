package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface ChangeDirectoryEvent {
    void changeDirectoryEventHandler(FileEntry source);
}
