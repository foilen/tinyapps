/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package ca.pgon.saviorlib.FileSystems;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import ca.pgon.saviorlib.Exceptions.FileSystemException;

/**
 * Backup files from and to an FTP server.
 * This class uses FTPClient from commons-net 
 * http://commons.apache.org/net/api-3.0.1/org/apache/commons/net/ftp/FTPClient.html
 */
public class FTPFS implements FileSystem {
    private String basePath;
    
    private FTPClient ftpClient = new FTPClient();
    private String hostname, user, pass;
    private int port = 21;
    
    @Override
    public String getBasePath() {
        return basePath;
    }
    
    @Override
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void checkIfValid() {
        if (basePath == null) {
            throw new FileSystemException("No base path defined");
        }
        
        if (hostname == null) {
            throw new FileSystemException("No host defined");
        }
        
        if (user == null) {
            throw new FileSystemException("No login defined");
        }
        
        if (pass == null) {
            throw new FileSystemException("No password defined");
        }
        
        connectIfNotConnected();
        
        try {
            if (!ftpClient.changeWorkingDirectory(basePath)) {
                throw new FileSystemException("The basepath is invalid");
            }
        } catch (IOException ex) {
            throw new FileSystemException("The basepath is invalid");
        }
    }

    public void createDirectory(FileEntry directory) {
        try {
            if (!ftpClient.makeDirectory(FileSystemTools.getAbsolutePath(directory, basePath))) {
                throw new FileSystemException("Could not create the directory"); 
            }
        } catch (Exception ex) {
            throw new FileSystemException("Could not create the directory", ex); 
        }
    }

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
        try {
            if (!ftpClient.removeDirectory(FileSystemTools.getAbsolutePath(directory, basePath))) {
                throw new FileSystemException("Could not delete the directory");
            }
        } catch (Exception e) {
            throw new FileSystemException("Could not delete the directory", e);
        }
    }

    public List<FileEntry> listDirectory(FileEntry directory) {
        String absolutePath = FileSystemTools.getAbsolutePath(directory, basePath);
        String relativePath = FileSystemTools.getRelativePath(directory);
        
        boolean isRoot = absolutePath.isEmpty();
        if (isRoot) {
            absolutePath = "/";
        }
        
        connectIfNotConnected();
        
        FTPFile[] ftplist = null;
        boolean retry = true;
        while (retry) {
            try {
                ftplist = ftpClient.listFiles(absolutePath);
                retry = false;
            } catch (IOException ex) {
                disconnect();
                try {Thread.sleep(30000);} catch (InterruptedException ex1) {}
                connectIfNotConnected();
            }
        }
        
        List<FileEntry> result = new ArrayList<FileEntry>();
        for (FTPFile ftpf: ftplist) {
            if (ftpf == null) continue;
            if (".".equals(ftpf.getName()) || "..".equals(ftpf.getName())) continue;
            
            FileEntry entry = FileSystemTools.createFileEntry(this, ftpf.isDirectory(), relativePath, ftpf.getName(), ftpf.getTimestamp().getTimeInMillis(), ftpf.getSize());
            result.add(entry);
        }
        
        return result;
    }

    public OutputStream createFile(FileEntry file) {
        try {
            return new WritingAutoComplete(ftpClient.storeFileStream(FileSystemTools.getAbsolutePath(file, basePath)));
        } catch (Exception ex) {
            throw new FileSystemException("Could not create the file", ex); 
        }
    }

    public OutputStream appendFile(FileEntry file) {
        try {
            return new WritingAutoComplete(ftpClient.appendFileStream(FileSystemTools.getAbsolutePath(file, basePath)));
        } catch (Exception ex) {
            throw new FileSystemException("Could not append to the file", ex); 
        }
    }

    public void deleteFile(FileEntry file) {
        try {
            if (!ftpClient.deleteFile(FileSystemTools.getAbsolutePath(file, basePath))) {
                throw new FileSystemException("Could not delete the file"); 
            }
        } catch (Exception ex) {
            throw new FileSystemException("Could not delete the file", ex); 
        }
    }

    public InputStream readFile(FileEntry file) {
        try {
            return new ReadingAutoComplete(ftpClient.retrieveFileStream(FileSystemTools.getAbsolutePath(file, basePath)));
        } catch (Exception ex) {
            throw new FileSystemException("Could not read the file ", ex);
        }
    }

    public InputStream readFileFrom(FileEntry file, long offset) {
        try {
            ftpClient.setRestartOffset(offset);
            return new ReadingAutoComplete(ftpClient.retrieveFileStream(FileSystemTools.getAbsolutePath(file, basePath)));
        } catch (Exception ex) {
            throw new FileSystemException("Could not read the file ", ex);
        }
    }

    public void changeFileModificationTime(FileEntry file, long time) {
        throw new FileSystemException("Not possible on FTP");
    }
    
    private void connectIfNotConnected() {
        // Check if connected
        if (ftpClient.isConnected()) return;
        
        // Connect
        try {
            ftpClient.connect(hostname, port);
        } catch (Exception ex) {
            throw new FileSystemException("Could not connect to the host", ex);
        }        
        if(!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            disconnect();
            throw new FileSystemException("The host refused the connection");
        }
        
        // Login
        try {
            if (!ftpClient.login(user, pass)) {
                throw new FileSystemException("The user/pass combinaison is invalid on this host");
            }
        } catch (IOException ex) {
            throw new FileSystemException("The host disconnected while logging in");
        }
        
        // Set up the connection
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException ex) {
            throw new FileSystemException("Could not set up the connection with the host", ex);
        }
    }
    
    private void disconnect() {
        try {ftpClient.logout();} catch (IOException ex) {}
        try {ftpClient.disconnect();} catch (IOException ex) {}
        
        ftpClient = new FTPClient();
    }
    
    private class ReadingAutoComplete extends InputStream {
        private InputStream wrapped;

        public ReadingAutoComplete(InputStream wrapped) {
            if (wrapped == null) {
                throw new FileSystemException("Could not read the file");
            }
            this.wrapped = wrapped;
        }
        
        @Override
        public void close() throws IOException {
            wrapped.close();
            if (!ftpClient.completePendingCommand()) {
                throw new FileSystemException("The file retrieval was not successful");
            }
        }
        
        @Override
        public int available() throws IOException {
            return wrapped.available();
        }
        
        @Override
        public synchronized void mark(int readlimit) {
            wrapped.mark(readlimit);
        }
        
        @Override
        public synchronized void reset() throws IOException {
            wrapped.reset();
        }
        
        @Override
        public boolean markSupported() {
            return wrapped.markSupported();
        }
        
        @Override
        public int read() throws IOException {
            return wrapped.read();
        }
        
        @Override
        public int read(byte b[]) throws IOException {
            return wrapped.read(b);
        }
        
        @Override
        public int read(byte b[], int off, int len) throws IOException {
            return wrapped.read(b, off, len);
        }
        
        @Override
        public long skip(long n) throws IOException {
            return wrapped.skip(n);
        }
    }
    
    private class WritingAutoComplete extends OutputStream {
        private OutputStream wrapped;

        public WritingAutoComplete(OutputStream wrapped) {
            if (wrapped == null) {
                throw new FileSystemException("Could not create the file");
            }
            this.wrapped = wrapped;
        }
        
        @Override
        public void close() throws IOException {
            wrapped.close();
            if (!ftpClient.completePendingCommand()) {
                throw new FileSystemException("The file storing was not successful");
            }
        }

        @Override
        public void write(int b) throws IOException {
            wrapped.write(b);
        }
        
        @Override
        public void write(byte b[]) throws IOException {
            wrapped.write(b);
        }
        
        @Override
        public void write(byte b[], int off, int len) throws IOException {
            wrapped.write(b, off, len);
        }
        
        @Override
        public void flush() throws IOException {
            wrapped.flush();
        }
    }
}
