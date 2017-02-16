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

public class LSLObject {
    private String name;
    private int position[] = new int[3];
    private double rotation[] = new double[3];

    public LSLObject(String name, int[] position, double[] rotation) {
        this.name = name;
        this.position = position;
        this.rotation = rotation;
    }

    public void Print() {
        System.out.print("Object " + name + " ");
        for (int i = 0; i < 3; i++) {
            System.out.print(position[i] + ",");
        }
        for (int i = 0; i < 3; i++) {
            System.out.print(rotation[i] + ",");
        }

        System.out.println();
    }

    public int DistanceSquare(int[] other) {
        return (int) (Math.pow((position[0] - other[0]), 2) + Math.pow((position[1] - other[1]), 2) + Math.pow((position[2] - other[2]), 2));
    }

    public int[] Direction(int[] other, int mult) {
        int result[] = new int[3];

        // Get the delta
        for (int i = 0; i < 3; i++)
            result[i] = position[i] - other[i];

        // Normalize
        double size = 0;
        for (int i = 0; i < 3; i++)
            size += Math.pow(result[i], 2);

        // Multiply
        if (mult != 0)
            size = mult / Math.sqrt(size);
        else
            size = 1;

        // Divide
        for (int i = 0; i < 3; i++)
            result[i] *= size;

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public double[] getRotation() {
        return rotation;
    }

    public void setRotation(double[] rotation) {
        this.rotation = rotation;
    }

}
