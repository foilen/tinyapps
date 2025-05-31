package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface CreateDirectoryEvent {
    void createDirectoryEventHandler(FileEntry destination);
}
