package dungeon;

import dungeon.dungeon.character.Character;
import dungeon.dungeon.character.Player;
import dungeon.dungeon.level.Level;
import processing.core.PApplet;

/**
 * Created by Louis on 17/10/2016.
 */
public class Game {

    PApplet p;
    Level level;
    Character player;

    public Game(PApplet p) {
        this.p = p;
        this.player = new Player(p, 0, 0, 0, 0, 0, 0, 10, 10);
        this.level = new Level(1,p, player);
    }

    public void render() {
        level.render();
    }

    public void keyPressed() {
        switch (p.key) {
            case '1':
                level = new Level(1, p, player);
                break;
        }
    }

}
