/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
