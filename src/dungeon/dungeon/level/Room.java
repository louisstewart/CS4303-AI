package dungeon.dungeon.level;

/**
 * Created by Louis on 27/10/2016.
 */
public class Room {

    public int x;
    public int y;
    public int x2;
    public int y2;
    private int width;
    private int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.x2 = x + width -1;
        this.y2 = y + height -1;
        this.width = width;
        this.height = height;
    }

    public int centerX() {
        return (x+x2)/2;
    }

    public int centerY() {
        return (y+y2)/2;
    }

    public boolean intersects(Room r) {
        return (this.x <= r.x2 && this.x2 >= r.x && this.y <= r.y2 && this.y2 >= r.y);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return height;
    }
}
