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
package slbh.scene;

import java.util.Iterator;
import java.util.Vector;

public class Scene {
    private Vector<Vector<SceneObject>> floors;
    public int startPosition[] = new int[3];

    private double deltaXY = 2;
    private double deltaZ = 3;
    private int repeat = 1;

    public Scene() {
        clearScene();
    }

    public void addNonDuplicateObject(int floor, SceneObject newObject) {
        Vector<SceneObject> floorObjects = floors.get(floor);

        for (SceneObject nextObject : floorObjects) {
            if ((nextObject.type.compareTo(newObject.type) == 0) && (nextObject.position[0] == newObject.position[0]) && (nextObject.position[1] == newObject.position[1])) {
                return;
            }
        }

        floorObjects.add(newObject);
    }

    public void addNonDuplicateObjects(int floor, Vector<SceneObject> newObjects) {
        for (SceneObject nextObject : newObjects) {
            addNonDuplicateObject(floor, nextObject);
        }
    }

    private void clearScene() {
        floors = new Vector<Vector<SceneObject>>();
        for (int i = 0; i < 3; ++i)
            startPosition[i] = 0;
    }

    public Vector<SceneObject> addFloor() {
        Vector<SceneObject> emptyFloor = new Vector<SceneObject>();
        floors.add(emptyFloor);
        return emptyFloor;
    }

    public int createFloor(int number, boolean after) {
        Vector<SceneObject> emptyFloor = new Vector<SceneObject>();

        if (floors.isEmpty()) {
            floors.add(emptyFloor);
        } else if (after) {
            ++number;
            floors.insertElementAt(emptyFloor, number);
        } else {
            ++startPosition[2];
            floors.insertElementAt(emptyFloor, number);
        }

        return number;
    }

    public double getDeltaXY() {
        return deltaXY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public Vector<SceneObject> getFloorObjects(int floor) {
        return floors.get(floor);
    }

    public Vector<Vector<SceneObject>> getFloors() {
        return floors;
    }

    public int getRepeat() {
        return repeat;
    }

    public int removeFloor(int number) {
        floors.remove(number);

        // Check if too big
        if (number >= floors.size())
            number = floors.size() - 1;

        // Check if no more floors
        if (floors.size() == 0) {
            floors.add(new Vector<SceneObject>());
            number = 0;
        }

        return number;
    }

    public void removeObject(int floor, String objectSelected, double x, double y) {
        Vector<SceneObject> floorObjects = floors.get(floor);

        Iterator<SceneObject> it = floorObjects.iterator();
        while (it.hasNext()) {
            SceneObject nextObject = it.next();
            if ((nextObject.type.compareTo(objectSelected) == 0) && (nextObject.position[0] == x) && (nextObject.position[1] == y)) {
                it.remove();
                break;
            }
        }
    }

    public void setDeltaXY(double deltaXY) {
        this.deltaXY = deltaXY;
    }

    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void changeDelta(double xy, double z) {
        setDeltaXY(xy);
        setDeltaZ(z);
    }
}
