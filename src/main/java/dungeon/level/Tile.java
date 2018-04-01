package dungeon.level;

import processing.core.PImage;

/**
 * Created by Louis on 01/11/2016.
 */
public class Tile {

    public boolean walkable;
    public boolean occupied;
    public PImage image;

    public Tile(boolean walk) {
        this.walkable = walk;
    }

}
