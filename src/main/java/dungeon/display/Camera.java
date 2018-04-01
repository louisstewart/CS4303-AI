package dungeon.display;

import dungeon.Helpers;
import processing.core.PApplet;

public class Camera {

    public static final float SCALE = Helpers.SCALE/2;

    static final int ROOM_WIDTH_IN_TILES = 10;
    static final int ROOM_HEIGHT_IN_TILES = 7;
    static final int TILE = Helpers.TILE;

    public float x, y;
    PApplet p;

    public Camera(PApplet p) {
        this.p = p;
    }

    public void begin() {
        p.scale(SCALE);
        p.pushMatrix();
        p.translate(-x, -y);
    }

    public void end() {
        p.popMatrix();
    }

    public void setRoom(int x, int y) {
        this.x = x * ROOM_WIDTH_IN_TILES * TILE;
        this.y = y * ROOM_HEIGHT_IN_TILES * TILE;
    }
}