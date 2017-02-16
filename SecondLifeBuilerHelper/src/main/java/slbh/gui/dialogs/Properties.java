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
package slbh.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import slbh.scene.Scene;

@SuppressWarnings("serial")
public class Properties extends JFrame implements ActionListener {
    private JTextField deltaXY = new JTextField();
    private JTextField deltaZ = new JTextField();
    private JTextField repeat = new JTextField();
    private Scene myScene;
    private JButton bOk = new JButton("OK");
    private JButton bCancel = new JButton(slbh.lang.Config.getMyLanguage().buttonCancel());

    public Properties(Scene myScene) {
        this.myScene = myScene;

        // Set the window
        setTitle("Properties");
        setSize(500, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the components
        deltaXY.setText(String.valueOf(myScene.getDeltaXY()));
        deltaZ.setText(String.valueOf(myScene.getDeltaZ()));
        repeat.setText(String.valueOf(myScene.getRepeat()));

        // Add the components
        add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesDeltaXY()));
        add(deltaXY);
        add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesDeltaZ()));
        add(deltaZ);
        add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesRepeat()));
        add(repeat);
        add(bOk);
        add(bCancel);

        // Set the listener
        bOk.setActionCommand("ok");
        bCancel.setActionCommand("cancel");
        bOk.addActionListener(this);
        bCancel.addActionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().compareTo("ok") == 0) {
            myScene.changeDelta(Double.valueOf(deltaXY.getText()).doubleValue(), Double.valueOf(deltaZ.getText()).doubleValue());
            myScene.setRepeat(Integer.valueOf(repeat.getText()));
        }

        dispose();
    }
}
