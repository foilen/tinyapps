/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
