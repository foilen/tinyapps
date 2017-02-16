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
package ca.pgon.saviorlib.CheckMods;

import ca.pgon.saviorlib.FileSystems.FileEntry;

/**
 * This class is used to check if the modification time says the file needs an update
 */
public class DateCM implements CheckMod {
    /**
     * The type of check to do.
     * NOTSAME means that if the time is different, it needs update
     * DESTINATION_OLDER means that if the destination time is older than the source, it needs update. Mostly used with FTP since it is not possible to change the time on it
     */
    public enum Type {
        NOTSAME, 
        DESTINATION_OLDER
    }
    
    private Type type = Type.NOTSAME;
    
    @Override
    public boolean needUpdate(FileEntry source, FileEntry destination) {
        switch (type) {
            case NOTSAME:
                return destination.modificationTime != source.modificationTime;
            case DESTINATION_OLDER:
                return destination.modificationTime < source.modificationTime;
            default:
                return false;
        }
    }    

    public Type getType() {
        return type;
    }

    /**
     * Set the check type (default NOTSAME)
     * @param type 
     */
    public void setType(Type type) {
        this.type = type;
    }
}
