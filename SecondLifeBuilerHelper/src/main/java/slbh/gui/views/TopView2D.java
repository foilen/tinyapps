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
import java.util.Vector;

import slbh.scene.Scene;
import slbh.scene.SceneObject;

public class TopView2D {
    private Scene myScene;
    private int currentFloor;
    private Color colorFloor = Color.white;
    private Color colorWall = Color.blue;
    private Color colorStairs = Color.green;
    private Color colorStart = Color.red;

    public TopView2D(Scene scene) {
        myScene = scene;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Scene getMyScene() {
        return myScene;
    }

    public void setColors(Color colorFloor, Color colorWall, Color colorStairs, Color colorStart) {
        this.colorFloor = colorFloor;
        this.colorWall = colorWall;
        this.colorStairs = colorStairs;
        this.colorStart = colorStart;
    }

    public void paintObjects(Graphics g, Vector<SceneObject> toDraw, int zoom) {
        for (SceneObject current : toDraw) {
            // FLOOR
            if (current.type.compareTo("floor") == 0) {
                g.setColor(colorFloor);
                int x = (int) current.position[0] * zoom;
                int y = (int) current.position[1] * zoom;
                g.fillRect(x + 1, y + 1, zoom - 1, zoom - 1);
            }

            // WALL
            if (current.type.compareTo("wall") == 0) {
                g.setColor(colorWall);
                int x = (int) current.position[0] * zoom;
                int y = (int) current.position[1] * zoom;
                if (current.position[0] * zoom != x) {
                    g.fillRect(x - 1, y - 1, 2, zoom + 1);
                } else {
                    g.fillRect(x - 1, y - 1, zoom + 1, 2);
                }
            }

            // STAIRS
            if (current.type.compareTo("stairsN") == 0) {
                g.setColor(colorStairs);
                g.fillRect((int) (current.position[0] * zoom + .2 * zoom), (int) current.position[1] * zoom, (int) (zoom * .6), (int) (.5 * zoom));
            }
            if (current.type.compareTo("stairsS") == 0) {
                g.setColor(colorStairs);
                g.fillRect((int) (current.position[0] * zoom + .2 * zoom), (int) (current.position[1] * zoom + .5 * zoom), (int) (zoom * .6), (int) (.5 * zoom));
            }
            if (current.type.compareTo("stairsW") == 0) {
                g.setColor(colorStairs);
                g.fillRect((int) current.position[0] * zoom, (int) (current.position[1] * zoom + .2 * zoom), (int) (zoom * .5), (int) (.6 * zoom));
            }
            if (current.type.compareTo("stairsE") == 0) {
                g.setColor(colorStairs);
                g.fillRect((int) (current.position[0] * zoom + .5 * zoom), (int) (current.position[1] * zoom + .2 * zoom), (int) (zoom * .5), (int) (.6 * zoom));
            }
        }

        // Draw the start position
        if (currentFloor == myScene.startPosition[2]) {
            int x = myScene.startPosition[0] * zoom;
            int y = myScene.startPosition[1] * zoom;
            g.setColor(colorStart);
            g.drawLine(x, y, x + zoom, y + zoom);
            g.drawLine(x, y + zoom, x + zoom, y);
        }
    }

    public void paintScene(Graphics g, int zoom) {
        paintObjects(g, myScene.getFloorObjects(currentFloor), zoom);
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }
}
