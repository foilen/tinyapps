package ca.pgon.saviorgui.profile;

import java.util.List;
import java.util.Map;

public class Profile {
    public String sourceFileSystemType;
    public String sourceBasePath;
    public Map<String, String> sourceParams;
    
    public String destinationFileSystemType;
    public String destinationBasePath;
    public Map<String, String> destinationParams;
    
    public String engineType;
    public boolean modDate, modSize, modMD5;
    public List<String> ignoreList;
}
