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
package slbh.lsl;

import java.util.Vector;

import slbh.scene.Scene;
import slbh.scene.SceneObject;

public class LSLScript {
    private final int maxRange = 750;

    private int error = -1;
    private Vector<LSLObject> theObjects = new Vector<LSLObject>();
    private String objectsConf = "";

    public LSLScript(Scene myScene) {
        Vector<Vector<SceneObject>> floors = myScene.getFloors();

        // Say the type of objects
        addObjectsConf("floor - " + myScene.getDeltaXY() + "x" + myScene.getDeltaXY() + "x0.1");
        addObjectsConf("wall - 0.1x" + myScene.getDeltaXY() + "x" + myScene.getDeltaZ());
        addObjectsConf("step - " + (myScene.getDeltaXY() / 10) + "x" + myScene.getDeltaXY() + "x" + (myScene.getDeltaZ() / 10));

        // All floors
        for (int f = 0; f < floors.size(); f++) {
            // All objects
            Vector<SceneObject> nextObj = floors.get(f);
            for (int i = 0; i < nextObj.size(); i++) {
                double position[] = new double[3];
                double rotation[] = new double[3];
                SceneObject current = nextObj.get(i);

                // Common
                position[0] = (myScene.startPosition[1] - current.position[1]) * myScene.getDeltaXY();
                position[1] = (myScene.startPosition[0] - current.position[0]) * myScene.getDeltaXY();
                position[2] = (f - myScene.startPosition[2]) * myScene.getDeltaZ();

                rotation[0] = 0;
                rotation[1] = 0;
                rotation[2] = 0;

                if (current.type.compareTo("wall") == 0) {
                    // WALL
                    int pos = (int) current.position[0];
                    position[2] += myScene.getDeltaZ() / 2;
                    if (current.position[0] == pos) {
                        // Placed like -
                        position[0] += myScene.getDeltaXY();
                    } else {
                        // Placed like |
                        position[1] += myScene.getDeltaXY();
                        rotation[2] = 90;
                    }

                    addObject(current.type, position, rotation);
                } else if (current.type.compareTo("floor") == 0) {
                    // FLOOR
                    addObject(current.type, position, rotation);
                } else if (current.type.compareTo("stairsN") == 0) {
                    // STAIRS NORTH
                    position[0] -= myScene.getDeltaXY() / 2 - myScene.getDeltaXY() / 20;
                    position[2] += +myScene.getDeltaZ() / 20;
                    for (int j = 0; j < 10; j++) {
                        addObject("step", position, rotation);
                        position = new double[] { Math.floor(position[0] * 10 + myScene.getDeltaXY()) / 10, position[1], position[2] + myScene.getDeltaZ() / 10 };

                    }
                } else if (current.type.compareTo("stairsS") == 0) {
                    // STAIRS SOUTH
                    position[0] += myScene.getDeltaXY() / 2 - myScene.getDeltaXY() / 20;
                    position[2] += +myScene.getDeltaZ() / 20;
                    for (int j = 0; j < 10; j++) {
                        addObject("step", position, rotation);
                        position = new double[] { Math.floor(position[0] * 10 - myScene.getDeltaXY()) / 10, position[1], position[2] + myScene.getDeltaZ() / 10 };

                    }
                } else if (current.type.compareTo("stairsE") == 0) {
                    // STAIRS EAST
                    rotation[2] = 90;
                    position[1] += myScene.getDeltaXY() / 2 - myScene.getDeltaXY() / 20;
                    position[2] += +myScene.getDeltaZ() / 20;
                    for (int j = 0; j < 10; j++) {
                        addObject("step", position, rotation);
                        position = new double[] { position[0], Math.floor(position[1] * 10 - myScene.getDeltaXY()) / 10, position[2] + myScene.getDeltaZ() / 10 };

                    }
                } else if (current.type.compareTo("stairsW") == 0) {
                    // STAIRS WEST
                    rotation[2] = 90;
                    position[1] -= myScene.getDeltaXY() / 2 - myScene.getDeltaXY() / 20;
                    position[2] += +myScene.getDeltaZ() / 20;
                    for (int j = 0; j < 10; j++) {
                        addObject("step", position, rotation);
                        position = new double[] { position[0], Math.floor(position[1] * 10 + myScene.getDeltaXY()) / 10, position[2] + myScene.getDeltaZ() / 10 };

                    }
                }
            }
        }
    }

    private void addObject(String name, double position[], double rotation[]) {
        int pos[] = new int[3];
        for (int i = 0; i < 3; i++)
            pos[i] = (int) Math.round(position[i] * 100);
        theObjects.add(new LSLObject(name, pos, rotation));
    }

    private void addObjectsConf(String text) {
        objectsConf += "// " + text + "\n";
    }

    public String toSLScript(int high, int repeat) {
        String result = objectsConf;
        result += "integer live = 60;\n\n";
        result += "integer i;\n";
        result += "integer j;\n";
        result += "vector posinit;\n";
        result += "default {\n";
        result += "\tstate_entry() {posinit = llGetPos();}\n";
        result += "\ttouch (integer param) {\n";
        result += "\t\tllSetAlpha(0, ALL_SIDES);\n";

        if (repeat > 1) {
            result += "\n\t\tinteger r;\n";
            result += "\t\tfor(r=0; r<" + repeat + "; r++) {\n\n";
        }

        result += generateCode(high);

        if (repeat > 1) {
            result += "\n\t\t}\n\n";
        }

        result += "\t\tllDie();\n";
        result += "\t}\n";
        result += "}";

        return result;
    }

    public String generateCode(int high) {
        String result = "";

        // The starting point
        int[] currentPos = new int[3];
        for (int i = 0; i < 3; i++)
            currentPos[i] = 0;

        // Make a new list and the current position
        Vector<LSLObject> rest = theObjects;

        // Check for the biggest lines
        result += biggestLines(currentPos, rest);

        // Ask for the normal procedure
        result += normal(currentPos, rest);

        // Up the last floor
        result += goSomewhere(currentPos, new int[] { 0, 0, high });

        return result;
    }

    // All the objects are created one at a time
    private String normal(int currentPos[], Vector<LSLObject> objects) {
        String result = "";

        // Find the nearest object
        while (!objects.isEmpty()) {
            double min = objects.get(0).DistanceSquare(currentPos);
            int index = 0;
            for (int i = 1; i < objects.size(); i++) {
                double next = objects.get(i).DistanceSquare(currentPos);
                if (next < min) {
                    min = next;
                    index = i;
                }
            }

            // Take it
            LSLObject myObject = objects.remove(index);

            // Go to it if needed
            result += goSomewhere(currentPos, myObject);

            // Create it
            int position[] = myObject.Direction(currentPos, 0);
            double rotation[] = myObject.getRotation();
            if ((rotation[0] == 0) && (rotation[1] == 0) && (rotation[2] == 0))
                result += "\t\tllRezObject(\"" + myObject.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2])
                        + ">, <0,0,0>, <0,0,0,0>, live);\n";
            else
                result += "\t\tllRezObject(\"" + myObject.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2])
                        + ">, <0,0,0>, llEuler2Rot(<" + rotation[0] + "," + rotation[1] + "," + rotation[2] + ">* DEG_TO_RAD), live);\n";

        }

        return result;
    }

    private String posToString(int pos) {
        String result = "";

        if (pos < 0) {
            result += "-";
            pos *= -1;
        }

        result += pos / 100 + "." + pos % 100;
        return result;
    }

    // Create the biggest lines
    private String biggestLines(int currentPos[], Vector<LSLObject> objects) {
        String result = "";
        Vector<LSLLine> lines = new Vector<LSLLine>();

        do {
            // Remove all items
            lines.clear();

            // Find all the possible lines of at least 3 objects
            // For all objects
            LSLObject first, second;
            double x, y, z;
            int dx, dy, dz;
            for (int i = 0; i < objects.size(); i++) {
                first = objects.get(i);
                // Take another object
                for (int j = 0; j < objects.size(); j++) {
                    second = objects.get(j);
                    // Check for the same name and rotation
                    if ((i == j) || (first.getName().compareTo(second.getName()) != 0) || (first.getRotation()[0] != second.getRotation()[0]) || (first.getRotation()[1] != second.getRotation()[1])
                            || (first.getRotation()[2] != second.getRotation()[2]))
                        continue;

                    // Get the delta
                    dx = second.getPosition()[0] - first.getPosition()[0];
                    dy = second.getPosition()[1] - first.getPosition()[1];
                    dz = second.getPosition()[2] - first.getPosition()[2];
                    LSLLine newLine = new LSLLine();
                    newLine.startingObject = first;
                    newLine.numberOfObjects = 2;
                    newLine.delta[0] = dx;
                    newLine.delta[1] = dy;
                    newLine.delta[2] = dz;

                    // If all deltas are 0, don't bother
                    if ((dx == 0) && (dy == 0) && (dz == 0))
                        continue;

                    // Look if there is another object farther
                    int last;
                    do {
                        last = newLine.numberOfObjects;
                        x = second.getPosition()[0] + dx;
                        y = second.getPosition()[1] + dy;
                        z = second.getPosition()[2] + dz;
                        for (int k = 0; k < objects.size(); k++) {
                            second = objects.get(k);
                            if ((second.getPosition()[0] == x) && (second.getPosition()[1] == y) && (second.getPosition()[2] == z) && (first.getName().compareTo(second.getName()) == 0)
                                    && (first.getRotation()[0] == second.getRotation()[0]) && (first.getRotation()[1] == second.getRotation()[1])
                                    && (first.getRotation()[2] == second.getRotation()[2])) {
                                newLine.numberOfObjects++;
                                break;
                            }
                        }
                    } while (newLine.numberOfObjects != last);
                    if (newLine.numberOfObjects >= 3)
                        lines.add(newLine);
                }
            }

            // Check if empty
            if (lines.isEmpty())
                break;

            // Take the biggest and nearest line
            int index = 0;
            int max = lines.get(0).numberOfObjects;
            int distance = lines.get(0).startingObject.DistanceSquare(currentPos);
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).numberOfObjects >= max) {
                    max = lines.get(i).numberOfObjects;
                    // The nearest of the two
                    if (lines.get(i).numberOfObjects == max) {
                        if (lines.get(i).startingObject.DistanceSquare(currentPos) < distance) {
                            index = i;
                            distance = lines.get(i).startingObject.DistanceSquare(currentPos);
                        }
                    } else {
                        // The biggest
                        index = i;
                        distance = lines.get(i).startingObject.DistanceSquare(currentPos);
                    }
                }
            }

            // Draw it
            // Get the first one
            first = lines.get(index).startingObject;
            int numberOfObjects = lines.get(index).numberOfObjects;
            int delta[] = new int[3];
            for (int i = 0; i < 3; i++)
                delta[i] = lines.get(index).delta[i];

            // Go to the first one
            result += goSomewhere(currentPos, first);

            // Keep all the lines in range
            for (int i = 0; i < lines.size(); i++) {
                // When to delete
                if ((lines.get(i).numberOfObjects != numberOfObjects) || (lines.get(i).delta[0] != delta[0]) || (lines.get(i).delta[1] != delta[1]) || (lines.get(i).delta[2] != delta[2])
                        || (lines.get(i).startingObject.DistanceSquare(currentPos) >= maxRange * maxRange)) {
                    lines.remove(i);
                    i--;
                }
            }

            // The header
            result += "\t\tfor(i=0; i<" + numberOfObjects + "; i++) {\n";
            // Get the maximum i
            int imax = (int) ((maxRange / 2) / (Math.sqrt((Math.pow(delta[0], 2) + Math.pow(delta[1], 2) + Math.pow(delta[2], 2)))) + 1);
            // All the lines
            for (int i = 0; i < lines.size(); i++) {
                first = lines.get(i).startingObject;
                int position[] = first.Direction(currentPos, 0);
                double rotation[] = first.getRotation();

                // The base
                result += "\t\t\tllRezObject(\"" + first.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2]) + "> ";
                if (imax != 1) {
                    // The delta multiplier
                    if (imax < numberOfObjects)
                        result += "+ (i%" + imax + ")";
                    else
                        result += "+ i";
                    // The delta
                    result += "*<" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">";
                }
                // The speed
                result += ", <0,0,0>, ";
                // The rotation
                if ((rotation[0] == 0) && (rotation[1] == 0) && (rotation[2] == 0))
                    result += "<0,0,0,0>, live);\n";
                else
                    result += "llEuler2Rot(<" + rotation[0] + "," + rotation[1] + "," + rotation[2] + ">* DEG_TO_RAD), live);\n";
            }

            // The footer
            if (imax == 1) {
                result += goSomewhere(currentPos, new int[] { currentPos[0] + delta[0], currentPos[1] + delta[1], currentPos[2] + delta[2] });
                for (int advance = 1; advance < numberOfObjects; advance++) {
                    goSomewhere(currentPos, new int[] { currentPos[0] + delta[0], currentPos[1] + delta[1], currentPos[2] + delta[2] });
                }
            } else if (imax < numberOfObjects) {
                result += "\t\t\tif(i%" + imax + "==" + (imax - 1) + ") llSetPos(llGetPos() + <" + posToString(delta[0] * imax) + "," + posToString(delta[1] * imax) + ","
                        + posToString(delta[2] * imax) + ">);\n";
                // Advance
                int numberOfJumps = (numberOfObjects) / imax;
                for (int i = 0; i < 3; i++)
                    currentPos[i] += numberOfJumps * delta[i] * imax;
            }
            result += "\t\t}\n";
            if (error != -1)
                result += "if (llGetPos() - posinit - <" + posToString(currentPos[0]) + "," + posToString(currentPos[1]) + "," + posToString(currentPos[2]) + "> != <0,0,0>) llOwnerSay( \""
                        + (error++) + " -> " + currentPos[0] + " " + currentPos[1] + " " + currentPos[2] + "\" + (string) (llGetPos() - posinit));\n";

            // Erase them all
            for (int i = 0; i < lines.size(); i++)
                lines.get(i).DeleteLine(objects);
        } while (!lines.isEmpty());

        return result;
    }

    // Move near an object
    private String goSomewhere(int currentPos[], LSLObject object) {
        String result = "";
        int counter = 0;

        // Get the direction
        int ddelta[] = object.Direction(currentPos, maxRange);
        int delta[] = new int[3];
        for (int i = 0; i < 3; i++)
            delta[i] = (int) ddelta[i] / 100;
        for (int i = 0; i < 3; i++)
            delta[i] = (int) delta[i] * 100;
        // Advance as much as needed
        while (object.DistanceSquare(currentPos) > maxRange * maxRange) {
            counter++;
            for (int i = 0; i < 3; i++)
                currentPos[i] += delta[i];
        }

        // Check how much time we need to advance
        if (counter > 0) {
            if (counter > 1)
                result += "\t\tfor(j=0; j<" + counter + "; j++) ";
            result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
            if (error != -1)
                result += "if (llGetPos() - posinit - <" + posToString(currentPos[0]) + "," + posToString(currentPos[1]) + "," + posToString(currentPos[2]) + "> != <0,0,0>) llOwnerSay( \""
                        + (error++) + " -> " + currentPos[0] + " " + currentPos[1] + " " + currentPos[2] + "\" + (string) (llGetPos() - posinit));\n";
        }

        return result;
    }

    private String goSomewhere(int currentPos[], int nextPos[]) {
        String result = "";
        int counter = 0;

        // Get the direction
        LSLObject object = new LSLObject("temp", nextPos, null);
        int ddelta[] = object.Direction(currentPos, maxRange);
        int delta[] = new int[3];
        for (int i = 0; i < 3; i++)
            delta[i] = (int) ddelta[i] / 100;
        for (int i = 0; i < 3; i++)
            delta[i] = (int) delta[i] * 100;
        // Advance as much as needed
        while (object.DistanceSquare(currentPos) > maxRange * maxRange) {
            counter++;
            for (int i = 0; i < 3; i++)
                currentPos[i] += delta[i];
        }

        // Check how much time we need to advance
        if (counter > 0) {
            if (counter > 1)
                result += "\t\tfor(j=0; j<" + counter + "; j++) ";
            result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
        }

        // The last part
        if (object.DistanceSquare(currentPos) != 0) {
            delta = object.Direction(currentPos, 0);
            result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
            for (int i = 0; i < 3; i++)
                currentPos[i] += delta[i];
        }

        return result;
    }
}
