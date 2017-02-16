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
package slbh.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import slbh.gui.views.Options;
import slbh.gui.views.TopView2DEdit;
import slbh.lang.Config;
import slbh.scene.Scene;
import slbh.scene.SceneObject;

@SuppressWarnings("serial")
public class Principal extends JFrame implements ComponentListener, ActionListener {
    static final public String version = "1.0";

    // Program Entry
    public static void main(String[] args) {
        Config.load();

        // Load the window
        Principal myWindow = new Principal();
        myWindow.setVisible(true);
    }

    private TopView2DEdit editView = new TopView2DEdit();
    private Options options = new Options(editView);

    private JMenuBar bar;

    // The constructor
    public Principal() {
        // Set the window
        setTitle("Second Life Builder Helper (" + version + ")");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        addComponentListener(this);

        // Lang
        reloadLang();
    }

    public void actionPerformed(ActionEvent arg0) {
        // NEW
        if (arg0.getActionCommand().equals("new")) {
            editView.init();
            options.refreshFloorsList(0);
        }

        // OPEN
        if (arg0.getActionCommand().equals("open")) {
            // Create a file chooser
            JFileChooser fc = new JFileChooser();

            // Open it
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    open(new FileInputStream(fc.getSelectedFile()));

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error reading file");
                }
            }

            options.refreshFloorsList(0);
        }

        // SAVE
        if (arg0.getActionCommand().equals("save")) {
            // Create a file chooser
            JFileChooser fc = new JFileChooser();

            // Save it
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    FileWriter f = new FileWriter(fc.getSelectedFile());
                    f.write(save());
                    f.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error writing file");
                }
            }
        }

        // CHANGE LANGUAGE
        if (arg0.getActionCommand().startsWith("lang-")) {
            Config.change(arg0.getActionCommand().substring(5));
            reloadLang();
        }

        editView.repaint();
    }

    public void componentHidden(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
    }

    public void componentResized(ComponentEvent arg0) {
        reposition();
        repaint();
    }

    public void componentShown(ComponentEvent arg0) {
    }

    private void open(InputStream in) throws NumberFormatException, IOException {
        BufferedReader myReader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        Vector<SceneObject> floorObjects = null;

        // Get a fresh document
        Scene newScene = new Scene();

        // Start position
        nextLine = myReader.readLine();
        String[] parts = nextLine.split(" ");
        for (int i = 0; i < 3; i++)
            newScene.startPosition[i] = Integer.valueOf(parts[i]).intValue();

        // Deltas
        nextLine = myReader.readLine();
        parts = nextLine.split(" ");
        newScene.setDeltaXY(Double.valueOf(parts[0]).doubleValue());
        newScene.setDeltaZ(Double.valueOf(parts[1]).doubleValue());

        // Repeat
        // For previous save, that value was not there
        nextLine = myReader.readLine();
        if (nextLine.charAt(0) == '-') {
            floorObjects = newScene.addFloor();
        } else {
            newScene.setRepeat(Integer.valueOf(nextLine));
        }

        // Get the objects
        while ((nextLine = myReader.readLine()) != null) {
            // Check if new floor
            if (nextLine.charAt(0) == '-') {
                floorObjects = newScene.addFloor();
                continue;
            }

            // Parse it
            String[] obj = nextLine.split(" ");

            // Objects
            floorObjects.add(new SceneObject(obj[0], Double.valueOf(obj[1]).doubleValue(), Double.valueOf(obj[2]).doubleValue()));
        }

        editView.setScene(newScene);
    }

    public void reloadLang() {
        // Clear
        if (getComponentCount() != 1)
            removeAll();

        // Prepare the menu
        JMenu file = new JMenu(Config.getMyLanguage().menuFile());
        bar = new JMenuBar();
        JMenuItem item = new JMenuItem(Config.getMyLanguage().menuNew());
        item.setActionCommand("new");
        item.addActionListener(this);
        file.add(item);
        item = new JMenuItem(Config.getMyLanguage().menuOpen());
        item.setActionCommand("open");
        item.addActionListener(this);
        file.add(item);
        item = new JMenuItem(Config.getMyLanguage().menuSave());
        item.setActionCommand("save");
        item.addActionListener(this);
        file.add(item);

        bar.add(file);
        JMenu lang = new JMenu(Config.getMyLanguage().menuLanguage());

        for (String s : Config.getAllLanguages()) {
            JMenuItem one = new JMenuItem(s);
            one.setActionCommand("lang-" + s);
            one.addActionListener(this);
            lang.add(one);
        }

        bar.add(lang);

        setJMenuBar(bar);

        // Adjust the components
        reposition();

        // Add the components
        add(options);
        add(editView);

        // Traverse
        options.reloadLang();
        setSize(getWidth() + 1, getHeight());
        setSize(getWidth() - 1, getHeight());
        repaint();
    }

    public void reposition() {
        int width = 200;
        options.setBounds(0, 0, width, getHeight());
        editView.setBounds(width, 0, getWidth() - width, getHeight());
    }

    private String save() {
        Scene theScene = editView.getScene();
        String result = "";

        // Start position
        for (int i = 0; i < 3; i++)
            result += theScene.startPosition[i] + " ";
        result += "\n";

        // Deltas
        result += theScene.getDeltaXY() + " " + theScene.getDeltaZ() + "\n";

        // Repeat
        result += theScene.getRepeat() + "\n";

        // Objects
        for (Vector<SceneObject> currentFloor : theScene.getFloors()) {
            result += "-\n";
            for (SceneObject currentObject : currentFloor) {
                result += currentObject.type + " " + currentObject.position[0] + " " + currentObject.position[1] + "\n";
            }
        }

        return result;
    }
}
