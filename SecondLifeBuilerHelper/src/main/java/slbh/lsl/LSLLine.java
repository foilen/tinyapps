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
package slbh.lsl;

import java.util.Vector;

public class LSLLine {
    public LSLObject startingObject = null;
    public int delta[] = new int[3];
    public int numberOfObjects = 0;

    public void DeleteLine(Vector<LSLObject> objects) {
        LSLObject second, first;
        second = first = startingObject;
        for (int i = 0; i < numberOfObjects; i++) {
            int x, y, z;
            x = second.getPosition()[0] + (i == 0 ? 0 : delta[0]);
            y = second.getPosition()[1] + (i == 0 ? 0 : delta[1]);
            z = second.getPosition()[2] + (i == 0 ? 0 : delta[2]);
            for (int k = 0; k < objects.size(); k++) {
                second = objects.get(k);
                if ((second.getPosition()[0] == x) && (second.getPosition()[1] == y) && (second.getPosition()[2] == z) && (first.getName().compareTo(second.getName()) == 0)
                        && (first.getRotation()[0] == second.getRotation()[0]) && (first.getRotation()[1] == second.getRotation()[1]) && (first.getRotation()[2] == second.getRotation()[2])) {
                    objects.remove(k);
                    break;
                }
            }
        }
    }
}
