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
package slbh.gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import slbh.gui.dialogs.Properties;
import slbh.gui.dialogs.ViewLSL;
import slbh.lang.Config;
import slbh.lsl.LSLScript;
import slbh.scene.Scene;

@SuppressWarnings("serial")
public class Options extends JPanel implements ActionListener {
    private JList editFloorsList;
    private JList backFloorsList;
    private DefaultListModel floorsModel = new DefaultListModel();
    private JButton floorCreateB;
    private JButton floorCreateA;
    private JButton floorRemove;
    private JRadioButton start;
    private JRadioButton floor;
    private JRadioButton wall;
    private JRadioButton stairsN;
    private JRadioButton stairsS;
    private JRadioButton stairsE;
    private JRadioButton stairsW;
    private JButton properties;
    private JButton sl;
    private TopView2DEdit myViewport;
    private int backOffset = 0;

    public Options(TopView2DEdit myViewport) {
        this.myViewport = myViewport;
        reloadLang();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("floorCreateB") == 0) {
            int current = (Integer) editFloorsList.getSelectedValue();
            refreshFloorsList(myViewport.createFloor(current, false));
            return;
        }

        if (e.getActionCommand().compareTo("floorCreateA") == 0) {
            int current = (Integer) editFloorsList.getSelectedValue();
            refreshFloorsList(myViewport.createFloor(current, true));
            return;
        }

        if (e.getActionCommand().compareTo("floorRemove") == 0) {
            int current = (Integer) editFloorsList.getSelectedValue();
            refreshFloorsList(myViewport.removeFloor(current));
            return;
        }

        if (e.getActionCommand().compareTo("sl") == 0) {
            // Create the scene
            LSLScript myConf = new LSLScript(myViewport.getScene());

            // Show
            Scene theScene = myViewport.getScene();
            ViewLSL v = new ViewLSL(myConf.toSLScript((int) theScene.getDeltaZ() * 100 * theScene.getFloors().size(), theScene.getRepeat()));
            v.setVisible(true);
            return;
        }

        if (e.getActionCommand().compareTo("properties") == 0) {
            // Open the properties window
            Properties p = new Properties(myViewport.getScene());
            p.setVisible(true);
            return;
        }

        myViewport.changeObject(e.getActionCommand());

    }

    public void refreshFloorsList(int selected) {
        floorsModel.clear();
        final int size = myViewport.getScene().getFloors().size();

        for (int i = size - 1; i >= 0; --i) {
            floorsModel.addElement(i);
        }

        editFloorsList.setSelectedIndex(size - selected - 1);
    }

    public void reloadLang() {
        // Clear all
        if (getComponentCount() != 0)
            removeAll();

        // Create
        editFloorsList = new JList(floorsModel);
        editFloorsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        editFloorsList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane floorsScroller = new JScrollPane(editFloorsList);

        backFloorsList = new JList(floorsModel);
        backFloorsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        backFloorsList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane backFloorsScroller = new JScrollPane(backFloorsList);

        ButtonGroup group = new ButtonGroup();
        floorCreateB = new JButton(Config.getMyLanguage().buttonAddFloorBefore());
        floorCreateA = new JButton(Config.getMyLanguage().buttonAddFloorAfter());
        floorRemove = new JButton(Config.getMyLanguage().buttonRemoveFloor());
        start = new JRadioButton(Config.getMyLanguage().objectStart());
        floor = new JRadioButton(Config.getMyLanguage().objectFloor());
        wall = new JRadioButton(Config.getMyLanguage().objectWall());
        stairsN = new JRadioButton(Config.getMyLanguage().objectStairsN());
        stairsS = new JRadioButton(Config.getMyLanguage().objectStairsS());
        stairsE = new JRadioButton(Config.getMyLanguage().objectStairsE());
        stairsW = new JRadioButton(Config.getMyLanguage().objectStairsW());
        properties = new JButton(Config.getMyLanguage().buttonProperties());
        sl = new JButton("SL Script");

        // Create the objects
        group.add(start);
        group.add(floor);
        group.add(wall);
        group.add(stairsN);
        group.add(stairsS);
        group.add(stairsE);
        group.add(stairsW);

        // Set the components
        JLabel l;
        int y = 10;

        l = new JLabel(Config.getMyLanguage().labelCurrentFloor());
        l.setBounds(0, y, 100, 20);
        add(l);
        l = new JLabel(Config.getMyLanguage().labelBackgroundFloor());
        l.setBounds(100, y, 100, 20);
        add(l);

        refreshFloorsList(0);
        y += 20;
        floorsScroller.setBounds(0, y, 100, 100);
        backFloorsScroller.setBounds(100, y, 100, 100);
        y += 100;
        floorCreateB.setBounds(0, y, 200, 20);
        y += 20;
        floorCreateA.setBounds(0, y, 200, 20);
        y += 20;
        floorRemove.setBounds(0, y, 200, 20);

        y += 50;
        l = new JLabel(Config.getMyLanguage().labelObjects());
        l.setBounds(0, y, 100, 20);
        add(l);
        y += 20;
        start.setBounds(0, y, 200, 20);
        y += 20;
        floor.setBounds(0, y, 200, 20);
        y += 20;
        wall.setBounds(0, y, 200, 20);
        y += 20;
        stairsN.setBounds(0, y, 200, 20);
        y += 20;
        stairsS.setBounds(0, y, 200, 20);
        y += 20;
        stairsE.setBounds(0, y, 200, 20);
        y += 20;
        stairsW.setBounds(0, y, 200, 20);

        y += 50;
        properties.setBounds(0, y, 200, 20);
        y += 20;
        sl.setBounds(0, y, 200, 20);

        // Set the panel
        setLayout(null);

        add(floorsScroller);
        add(backFloorsScroller);
        add(floorCreateB);
        add(floorCreateA);
        add(floorRemove);
        add(start);
        add(floor);
        add(wall);
        add(stairsN);
        add(stairsS);
        add(stairsW);
        add(stairsE);
        add(properties);
        add(sl);

        // Place the listeners
        floorCreateB.setActionCommand("floorCreateB");
        floorCreateA.setActionCommand("floorCreateA");
        floorRemove.setActionCommand("floorRemove");
        floor.setActionCommand("floor");
        wall.setActionCommand("wall");
        stairsN.setActionCommand("stairsN");
        stairsS.setActionCommand("stairsS");
        stairsE.setActionCommand("stairsE");
        stairsW.setActionCommand("stairsW");
        properties.setActionCommand("properties");
        sl.setActionCommand("sl");
        start.setActionCommand("start");

        myViewport.changeObject("floor");
        floor.setSelected(true);

        editFloorsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Object selectedFloor = editFloorsList.getSelectedValue();
                if (selectedFloor != null) {
                    myViewport.changeFloor((Integer) selectedFloor);
                    int newIndex = (Integer) selectedFloor + backOffset;
                    if (newIndex < 0)
                        newIndex = 0;
                    if (newIndex > floorsModel.size() - 1)
                        newIndex = floorsModel.size() - 1;
                    backFloorsList.setSelectedIndex(floorsModel.size() - newIndex - 1);
                    backOffset = newIndex - (Integer) selectedFloor;
                }
            }
        });
        backFloorsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                Object selectedFloor = backFloorsList.getSelectedValue();
                if (selectedFloor != null) {
                    myViewport.changeBackFloor((Integer) selectedFloor);
                    backOffset = (Integer) selectedFloor - (Integer) editFloorsList.getSelectedValue();
                }
            }
        });
        floorCreateB.addActionListener(this);
        floorCreateA.addActionListener(this);
        floorRemove.addActionListener(this);
        floor.addActionListener(this);
        wall.addActionListener(this);
        stairsN.addActionListener(this);
        stairsS.addActionListener(this);
        stairsE.addActionListener(this);
        stairsW.addActionListener(this);
        properties.addActionListener(this);
        sl.addActionListener(this);
        start.addActionListener(this);

        repaint();
    }
}
