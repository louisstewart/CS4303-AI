package dungeon.dungeon.level;

import dungeon.Constants;
import processing.core.PApplet;

import java.util.Random;

/**
 * Created by ls99.
 *
 * A level contains a map representing the game board,
 * as well
 */
public class Level {

    private int number;
    private boolean[][] map;
    private PApplet p;
    private Random rand;
    private int width = Constants.WIDTH/Constants.TILE; // Base level width.
    private int height = Constants.HEIGHT/Constants.TILE;

    public Level(int number, PApplet p) {
        this.number = number;
        this.p = p;

        this.map = new boolean[width][height];

        generateLevel();
        populateLevel();
    }

    /**
     * Procedurally generate the level.
     *
     * For this, an agent will be used to produce an interesting-ish game level.
     * The reason for using an agent is to produce a more organic style of cave.
     */
    private void generateLevel() {
        // First get a random location for digger to start.
        rand = new Random(System.currentTimeMillis());
        int startX = rand.nextInt(width - 1);
        int startY = rand.nextInt(height -1);



    }

    /**
     * Fill the level up with some content, based on the level number.
     *
     * The higher the number, the more goodies and enemies will be placed\
     * onto the grid.
     */
    private void populateLevel() {

    }


}
