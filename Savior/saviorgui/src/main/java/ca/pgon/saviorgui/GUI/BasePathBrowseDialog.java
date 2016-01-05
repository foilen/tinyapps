/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

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
package ca.pgon.saviorgui.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JTextField;

import ca.pgon.saviorlib.FileSystems.FileEntry;
import ca.pgon.saviorlib.FileSystems.FileSystemTools;

@SuppressWarnings("serial")
public class BasePathBrowseDialog extends javax.swing.JDialog {
    private JTextField pathField;
    private FileEntry fileEntry;
    private boolean rootHasSlash;
    
    /** Creates new form BrowseDialog */
    public BasePathBrowseDialog(java.awt.Frame parent, boolean modal, JTextField pathField, FileEntry fileEntry, boolean rootHasSlash) {
        super(parent, modal);
        initComponents();
        
        this.pathField = pathField;
        this.fileEntry = fileEntry;
        this.rootHasSlash = rootHasSlash;
        
        updateFileList();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pathList = new javax.swing.JList();
        choose = new javax.swing.JButton();
        currentPath = new javax.swing.JLabel();
        parentButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jLabel1.setText("Current path:");

        pathList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pathListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(pathList);

        choose.setText("Choose");
        choose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseActionPerformed(evt);
            }
        });

        parentButton.setText("Parent");
        parentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(choose, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentPath))
                    .addComponent(parentButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(currentPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(choose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseActionPerformed
        String fullPath = FileSystemTools.getAbsolutePath(getSelectedFileEntry());
        
        if (rootHasSlash) {
            if (!fullPath.startsWith("/")) {
                fullPath = "/" + fullPath;
            }
        }
        
        fullPath = fullPath.replaceAll("//", "/");
        pathField.setText(fullPath);
        dispose();
    }//GEN-LAST:event_chooseActionPerformed

    private void pathListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pathListMouseClicked
        if (evt.getClickCount() != 2) return;
        
        fileEntry = getSelectedFileEntry();
        updateFileList();
    }//GEN-LAST:event_pathListMouseClicked

    private void parentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parentButtonActionPerformed
        String fullPath = FileSystemTools.getAbsolutePath(getSelectedFileEntry());
        fullPath = fullPath.replaceAll("//", "/");
        int lastPos = fullPath.lastIndexOf("/");
        if (lastPos == -1) {
            return;
        }
        
        fileEntry.name = "";
        fileEntry.path = "";
        
        String[] parts = fullPath.split("/");
        StringBuilder newBasePathB = new StringBuilder();
        for(int i=0; i<parts.length - 1; ++i) {
            newBasePathB.append(parts[i]);
            if (i != parts.length - 2) newBasePathB.append('/');
        }
        
        String newBasePath = newBasePathB.toString();
        
        if (rootHasSlash) {
            if (!newBasePath.startsWith("/")) {
                newBasePath = "/" + newBasePath;
            }
        }
        
        fileEntry.fileSystem.setBasePath(newBasePath);
        
        updateFileList();
    }//GEN-LAST:event_parentButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton choose;
    private javax.swing.JLabel currentPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton parentButton;
    private javax.swing.JList pathList;
    // End of variables declaration//GEN-END:variables

    private FileEntry getSelectedFileEntry() {
        int index = pathList.getMinSelectionIndex();
        if (index == -1) return fileEntry;
        return (FileEntry) pathList.getModel().getElementAt(index);
    }
    
    private void updateFileList() {
        currentPath.setText(FileSystemTools.getAbsolutePath(fileEntry));
        final List<FileEntry> directories = new ArrayList<FileEntry>();
        for (FileEntry fe : fileEntry.fileSystem.listDirectory(fileEntry)) {
            if (fe.isDirectory) {
                directories.add(fe);
            }
        }
        
        Collections.sort(directories);
        
        pathList.setModel(new AbstractListModel() {
            List<FileEntry> dir = directories;
            @Override
            public int getSize() {
                return dir.size();
            }

            @Override
            public Object getElementAt(int index) {
                return dir.get(index);
            }
        });
    }
}
