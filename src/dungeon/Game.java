package dungeon;

import dungeon.dungeon.level.Level;
import processing.core.PApplet;

/**
 * Created by Louis on 17/10/2016.
 */
public class Game {

    PApplet p;
    Level level;

    public Game(PApplet p) {
        this.p = p;
        this.level = new Level(1,p);
    }

    public void render() {
        level.render();
    }

}
