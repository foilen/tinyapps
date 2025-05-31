package slbh.scene;

public class SceneObject {
    public String type = "";
    public double position[] = new double[2];

    public SceneObject(String type, double x, double y) {
        this.position[0] = x;
        this.position[1] = y;
        this.type = type;
    }
}
