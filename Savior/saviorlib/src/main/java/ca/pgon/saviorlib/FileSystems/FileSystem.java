/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ca.pgon.saviorlib.Exceptions.FileSystemException;

/**
 * This interface defines the functions needed for manipulating files and directories
 */
public interface FileSystem {
    /**
     * Set the path that is appended to all the file path
     * The separators in the path is always '/'. The FileSystems will take care of the right separator.
     * @param basePath The base path
     */
    void setBasePath(String basePath);
    
    /**
     * Get the path that is appended to all the file path
     * The separators in the path is always '/'. The FileSystems will take care of the right separator.
     */
    String getBasePath();
    
    /**
     * Check if the filesystem with the specified base path is accessible.
     * @throws FileSystemException if not accessible
     */
    void checkIfValid();
    
    /**
     * Create the directory
     * @param directory 
     */
    void createDirectory(FileEntry directory);
    /**
     * Delete the directory recursively
     * @param directory 
     */
    void deleteDirectory(FileEntry directory);
    /**
     * List the files and directories in that directory
     * @param directory - The directory or null for the base directory
     * @return The files and directories list
     */
    List<FileEntry> listDirectory(FileEntry directory);
    
    /**
     * Create a file or overwrite it
     * @param file
     * @return The stream to write to
     */
    OutputStream createFile(FileEntry file);
    /**
     * Open a file for appending
     * @param file
     * @return The stream pointing to the end of the file to write to
     */
    OutputStream appendFile(FileEntry file);
    /**
     * Delete the file
     * @param file 
     */
    void deleteFile(FileEntry file);
    /**
     * Open a file for reading
     * @param file
     * @return The stream to read
     */
    InputStream readFile(FileEntry file);
    
    /**
     * Open a file for reading at the specified offset
     * @param file
     * @param offset 
     * @return The stream to read
     */
    InputStream readFileFrom(FileEntry file, long offset);
    
    /**
     * Change the modification time of the file if the filesystem can do it
     * @param file
     * @param time 
     */
    void changeFileModificationTime(FileEntry file, long time);
}
