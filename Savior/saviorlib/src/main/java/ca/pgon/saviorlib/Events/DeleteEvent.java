package ca.pgon.saviorlib.Events;

import ca.pgon.saviorlib.FileSystems.FileEntry;

public interface DeleteEvent {
    void deleteEventHandler(FileEntry destination);
}
