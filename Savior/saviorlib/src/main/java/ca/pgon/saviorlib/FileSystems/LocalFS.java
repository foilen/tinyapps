/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ca.pgon.saviorlib.Exceptions.FileSystemException;

/**
 * Backup files from and to a local disk
 */
public class LocalFS implements FileSystem {
    private String basePath;
    
    @Override
    public String getBasePath() {
        return basePath;
    }
    
    @Override
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    @Override
    public void checkIfValid() {
        if (basePath == null) {
            throw new FileSystemException("No base path defined");
        }
        if (!new File(basePath).exists()) {
            throw new FileSystemException("The base path does not exists");
        }
    }

    @Override
    public void createDirectory(FileEntry directory) {
        File d = new File(FileSystemTools.getAbsolutePath(directory, basePath));
        if (!d.mkdirs()) {
            if (!d.exists()) {
                throw new FileSystemException("Could not create the directory");
            }
        }
    }

    @Override
    public void deleteDirectory(FileEntry directory) {
        // Delete files and directories in that directory
        for (FileEntry entry : listDirectory(directory)) {
            if (entry.isDirectory) {
                deleteDirectory(entry);
            } else {
                deleteFile(entry);
            }
        }
        
        // Delete the current dir
        File d = new File(FileSystemTools.getAbsolutePath(directory, basePath));
        if (!d.delete()) {
            if (d.exists()) {
                throw new FileSystemException("Could not delete the directory and it is still present");
            }
        }
    }

    @Override
    public List<FileEntry> listDirectory(FileEntry directory) {
        String absolutePath = FileSystemTools.getAbsolutePath(directory, basePath);
        String relativePath = FileSystemTools.getRelativePath(directory);
        
        boolean isRoot = absolutePath.isEmpty();
        
        absolutePath = absolutePath.replace('/', File.separatorChar);
        
        File[] files;
        if (isRoot) {
            files = File.listRoots();
        } else {
            files = new File(absolutePath).listFiles();
        }
        
        List<FileEntry> result = new ArrayList<FileEntry>();
        
        for (File file : files) {
            String name;
            if (isRoot) {
                name = file.getPath();
                name = name.replace(File.separatorChar, '/');
            } else {
                name = file.getName();
            }

            FileEntry entry = FileSystemTools.createFileEntry(this, file.isDirectory(), relativePath, name, file.lastModified(), file.length());
            result.add(entry);
        }
        
        return result;
    }

    @Override
    public OutputStream createFile(FileEntry file) { 
        try {
            File f = new File(FileSystemTools.getAbsolutePath(file, basePath));
            return new FileOutputStream(f);
        } catch (FileNotFoundException ex) {
            throw new FileSystemException("Could not create the file", ex); 
        }
    }

    @Override
    public OutputStream appendFile(FileEntry file) {
        try {
            File f = new File(FileSystemTools.getAbsolutePath(file, basePath));
            return new FileOutputStream(f, true);
        } catch (FileNotFoundException ex) {
            throw new FileSystemException("Could not append to the file", ex); 
        }
    }

    @Override
    public void deleteFile(FileEntry file) {
        File f = new File(FileSystemTools.getAbsolutePath(file, basePath));
        if (!f.delete()) {
            if (f.exists()) {
                throw new FileSystemException("Could not delete the file and it is still present");
            }
        }
    }

    @Override
    public InputStream readFile(FileEntry file) {
        try {
            return new FileInputStream(FileSystemTools.getAbsolutePath(file, basePath));
        } catch (Exception ex) {
            throw new FileSystemException("Could not read the file ", ex);
        }
    }

    @Override
    public InputStream readFileFrom(FileEntry file, long offset) {
        try {
            FileInputStream in = new FileInputStream(FileSystemTools.getAbsolutePath(file, basePath));
            in.skip(offset);
            return in;
        } catch (Exception ex) {
            throw new FileSystemException("Could not read the file ", ex);
        }
    }

    @Override
    public void changeFileModificationTime(FileEntry file, long time) {
        File f = new File(FileSystemTools.getAbsolutePath(file, basePath));
        if (!f.setLastModified(time)) {
            throw new FileSystemException("Could not change the modification time of the file");
        }
    }
}
