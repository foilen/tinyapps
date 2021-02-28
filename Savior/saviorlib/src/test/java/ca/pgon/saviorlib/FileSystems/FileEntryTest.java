/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.saviorlib.FileSystems;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileEntryTest {
    private List<FileEntry> expected = new ArrayList<FileEntry>();
    
    @Before
    public void setUp() {
        expected.clear();
        expected.add(FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-4", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-5", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-6", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-7", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "second dir/sub_dir", "entry-2", 1000, 1000));
        expected.add(FileSystemTools.createFileEntry(false, "third dir/", "entry-3", 1000, 1000));
    }
    
    @Test
    public void orderingTest() {
        List<FileEntry> actual = new ArrayList<FileEntry>();
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-6", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-4", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "first dir/sub_dir", "entry-1", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "second dir/sub_dir", "entry-2", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "third dir/", "entry-3", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-5", 1000, 1000));
        actual.add(FileSystemTools.createFileEntry(false, "fourth dir/", "entry-7", 1000, 1000));
        
        Collections.sort(actual);
        
        compareLists(expected, actual);
    }

    private void compareLists(List<FileEntry> expected, List<FileEntry> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i=0; i<expected.size(); ++i) {
            assertEquals("Problem with number: " + i, expected.get(i), actual.get(i));
        }
    }
}
