/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;

import slbh.scene.Scene;
import slbh.scene.SceneObject;

@SuppressWarnings("serial")
public class TopView2DEdit extends JComponent implements MouseListener, MouseMotionListener {
    private final int zoom = 20;

    private int mouseStart[] = new int[2];
    private int mouseEnd[] = new int[2];
    private boolean mousePressed = false;
    private String objectSelected = "";
    private int editFloor;
    private Scene myScene;
    private TopView2D editView;
    private TopView2D backgroundView;

    public TopView2DEdit() {
        init();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int changeBackFloor(int selectedFloor) {
        backgroundView.setCurrentFloor(selectedFloor);
        repaint();
        return selectedFloor;
    }

    public int changeFloor(int number) {
        editFloor = number;
        editView.setCurrentFloor(editFloor);
        repaint();
        return number;
    }

    public void changeObject(String object) {
        objectSelected = object;
    }

    public int createFloor(int number, boolean after) {
        int result = myScene.createFloor(number, after);
        changeFloor(result);

        return result;
    }

    public Vector<SceneObject> createListObjects(boolean force) {
        Vector<SceneObject> result = new Vector<SceneObject>();

        if (force || mousePressed) {
            // Wall
            if (objectSelected.compareTo("wall") == 0) {
                // In X or Y
                if (Math.abs((mouseEnd[0] - mouseStart[0])) < Math.abs((mouseEnd[1] - mouseStart[1]))) {
                    // X fixed
                    if (mouseStart[1] < mouseEnd[1]) {
                        for (double y = mouseStart[1]; y < mouseEnd[1]; y++) {
                            result.add(new SceneObject(objectSelected, mouseStart[0] + .5, y));
                        }
                    } else {
                        for (double y = mouseEnd[1]; y < mouseStart[1]; y++) {
                            result.add(new SceneObject(objectSelected, mouseStart[0] + .5, y));
                        }
                    }

                } else {
                    if (mouseStart[0] < mouseEnd[0]) {
                        // Y fixed
                        for (double x = mouseStart[0]; x < mouseEnd[0]; x++) {
                            result.add(new SceneObject(objectSelected, x, mouseStart[1] + .5));
                        }
                    } else {
                        for (double x = mouseEnd[0]; x < mouseStart[0]; x++) {
                            result.add(new SceneObject(objectSelected, x, mouseStart[1] + .5));
                        }
                    }
                }
            } else {
                // Other
                int SX = mouseStart[0] < mouseEnd[0] ? mouseStart[0] : mouseEnd[0];
                int EX = mouseStart[0] < mouseEnd[0] ? mouseEnd[0] : mouseStart[0];
                int SY = mouseStart[1] < mouseEnd[1] ? mouseStart[1] : mouseEnd[1];
                int EY = mouseStart[1] < mouseEnd[1] ? mouseEnd[1] : mouseStart[1];

                for (int x = SX; x <= EX; x++) {
                    for (int y = SY; y <= EY; y++) {
                        result.add(new SceneObject(objectSelected, x, y));
                    }
                }
            }
        }

        return result;
    }

    public void deleteNearObject(int _x, int _y) {
        double x, y;

        if ((objectSelected.compareTo("floor") == 0) || ((objectSelected.length() > 6) && (objectSelected.substring(0, 6).compareTo("stairs") == 0))) {
            // FLOOR or STAIRS
            x = _x / zoom;
            y = _y / zoom;
        } else if (objectSelected.compareTo("wall") == 0) {
            // WALL
            double posX = (_x + zoom / 2) % zoom;
            double posY = (_y + zoom / 2) % zoom;
            if (((posX < zoom * .3) || (posX > zoom * .7)) && ((posY < zoom * .7) && (posY > zoom * .3))) {
                // -
                x = _x / zoom;
                y = _y / zoom + .5;
            } else if (((posY < zoom * .3) || (posY > zoom * .7)) && ((posX < zoom * .7) && (posX > zoom * .3))) {
                // |
                x = _x / zoom + .5;
                y = _y / zoom;
            } else {
                x = -1;
                y = -1;
            }
        } else {
            // None
            x = -1;
            y = -1;
        }

        // Remove the objects at that position if any
        myScene.removeObject(editFloor, objectSelected, x, y);
    }

    public Scene getScene() {
        return myScene;
    }

    public void init() {
        myScene = new Scene();
        myScene.createFloor(0, true);

        setScene(myScene);
        changeFloor(0);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        if (mousePressed) {
            if (objectSelected.compareTo("wall") == 0) {
                mouseEnd[0] = (arg0.getX() + 5) / zoom;
                mouseEnd[1] = (arg0.getY() + 5) / zoom;
            } else {
                mouseEnd[0] = arg0.getX() / zoom;
                mouseEnd[1] = arg0.getY() / zoom;
            }
        } else {
            if ((arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                // RIGHT BUTTON
                deleteNearObject(arg0.getX(), arg0.getY());
            }
        }

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // LEFT BUTTON
        if ((arg0.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            if (objectSelected.compareTo("wall") == 0) {
                mouseStart[0] = mouseEnd[0] = (arg0.getX() + 5) / zoom;
                mouseStart[1] = mouseEnd[1] = (arg0.getY() + 5) / zoom;
            } else {
                mouseStart[0] = mouseEnd[0] = arg0.getX() / zoom;
                mouseStart[1] = mouseEnd[1] = arg0.getY() / zoom;
            }

            mousePressed = true;
        } else if ((arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            // RIGHT BUTTON
            deleteNearObject(arg0.getX(), arg0.getY());
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        mousePressed = false;
        if (arg0.getButton() == MouseEvent.BUTTON1) {
            if (objectSelected.compareTo("start") == 0) {
                myScene.startPosition[0] = arg0.getX() / zoom;
                myScene.startPosition[1] = arg0.getY() / zoom;
                myScene.startPosition[2] = editFloor;
            } else {
                myScene.addNonDuplicateObjects(editFloor, createListObjects(true));
            }

            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the constants
        int width = getWidth();
        int height = getHeight();

        // Clear the component
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        // Check if a scene
        if (myScene == null)
            return;

        // Show the grid
        g.setColor(Color.white);
        for (int x = 0; x < width; x += zoom)
            g.drawLine(x, 0, x, height);
        for (int y = 0; y < height; y += zoom)
            g.drawLine(0, y, width, y);

        // Show the background floor
        backgroundView.paintScene(g, zoom);

        // Show the edited floor
        editView.paintScene(g, zoom);

        // Show the temporary objects
        editView.paintObjects(g, createListObjects(false), zoom);
    }

    public int removeFloor(int number) {
        int result = myScene.removeFloor(number);
        changeFloor(result);

        return result;
    }

    public void setScene(Scene newScene) {
        myScene = newScene;
        mouseStart = new int[2];
        mouseEnd = new int[2];
        mousePressed = false;

        editView = new TopView2D(myScene);
        backgroundView = new TopView2D(myScene);
        backgroundView.setColors(new Color(1.0f, 1.0f, 1.0f, 0.25f), new Color(0.0f, 0.0f, 1.0f, 0.25f), new Color(0.0f, 1.0f, 0.0f, 0.25f), new Color(1.0f, 0.0f, 0.0f, 0.25f));

        changeFloor(0);
    }
}
