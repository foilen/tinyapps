/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.Engines;

import java.util.List;

import ca.pgon.saviorlib.CheckMods.CheckMod;
import ca.pgon.saviorlib.Events.AddEvent;
import ca.pgon.saviorlib.Events.BackupEvent;
import ca.pgon.saviorlib.Events.ChangeDirectoryEvent;
import ca.pgon.saviorlib.Events.CreateDirectoryEvent;
import ca.pgon.saviorlib.Events.DeleteDirectoryEvent;
import ca.pgon.saviorlib.Events.DeleteEvent;
import ca.pgon.saviorlib.Events.ProgressEvent;
import ca.pgon.saviorlib.FileSystems.FileSystem;

/**
 * This interface defines an Engine that executes the backup. It checks the directories and files and sends the changes.
 */
public interface Engine {
    /**
     * To set the source directory from where the files comes.
     * 
     * @param sourceFS The source filesystem
     */
    void setSourceFileSystem(FileSystem sourceFS);
    
    /**
     * To set the destination directory to where the files will be copied.
     * 
     * @param destinationFS The source filesystem
     */
    void setDestinationFileSystem(FileSystem destinationFS);
    
    /**
     * To set the ways to know which files are to be added
     * 
     * @param checkMods The check modification objects
     */
    void setCheckMods(List<CheckMod> checkMods);
    
    /**
     * To set the files and directories to ignore in the source and destination
     * 
     * @param ignoreFiles The relative paths to files or directories not starting or ending with '/'
     */
    void setIgnoreFiles(List<String> ignoreFiles);
    
    /**
     * To set an handler that will be notified when a file is starting and completed to be added
     * @param addEvent 
     */
    void setAddEvent(AddEvent addEvent);
    
    /**
     * To set an handler that will be notified when the backup stars, pauses or terminate
     * @param backupEvent 
     */
    void setBackupEvent(BackupEvent backupEvent);
    
    /**
     * To set an handler that will be notified when there is a change in the current working directory
     * @param changeDirectoryEvent 
     */
    void setChangeDirectoryEvent(ChangeDirectoryEvent changeDirectoryEvent);
    
    /**
     * To set an handler that will be notified when a directory is created
     * @param createDirectoryEvent 
     */
    void setCreateDirectoryEvent(CreateDirectoryEvent createDirectoryEvent);
    
    /**
     * To set an handler that will be notified when a file is deleted
     * @param deleteEvent 
     */
    void setDeleteEvent(DeleteEvent deleteEvent);
    
    /**
     * To set an handler that will be notified when a directory is deleted
     * @param deleteDirectoryEvent 
     */
    void setDeleteDirectoryEvent(DeleteDirectoryEvent deleteDirectoryEvent);
    
    /**
     * To set an handler that will be notified when a file is copying with the current progress
     * @param progressEvent 
     */
    void setProgressEvent(ProgressEvent progressEvent);
    
    /**
     * To start the backup process
     */
    void start();
    
    /**
     * To pause the backup process
     */
    void pause();
    
    /**
     * To resume the backup process when paused
     */
    void resume();
    
    /**
     * To stop the backup process
     */
    void stop();
}
