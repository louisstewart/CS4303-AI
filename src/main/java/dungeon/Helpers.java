package dungeon;

/**
 * Created by Louis on 27/10/2016.
 */
public class Helpers {
    public static final int WIDTH = 240;
    public static final int HEIGHT = 168;
    public static final int TILE = 24;
    public static final int SCALE = 4;

    public static float clamp(float x, float min, float max) {
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        return x;
    }
}
