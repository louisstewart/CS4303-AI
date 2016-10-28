package dungeon.dungeon.level;

import dungeon.Helpers;
import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;
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
    private int width = Helpers.WIDTH* Helpers.SCALE/ Helpers.TILE; // Base level width.
    private int height = Helpers.HEIGHT* Helpers.SCALE/ Helpers.TILE; // Base level height.

    private List<Room> rooms = new LinkedList<Room>();

    public Level(int number, PApplet p) {
        this.number = number;
        this.p = p;

        this.map = new boolean[width][height];

        generateLevel();
        populateLevel();
    }

    public void render() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int x = i* Helpers.TILE;
                int y = j* Helpers.TILE;
                if(map[i][j]) {
                    p.fill(255);
                    p.rect(x, y, Helpers.TILE, Helpers.TILE);
                }
                else {
                    p.fill(0);
                    p.rect(x, y, Helpers.TILE, Helpers.TILE);
                }
            }
        }
    }

    public boolean[][] getMap() {
        return this.map;
    }

    /**
     * Procedurally generate the level.
     *
     * For this, an agent will be used to produce an interesting-ish game level.
     * The reason for using an agent is to produce a more organic style of cave.
     */
    private void generateLevel() {
        int maxRoomSize = 9; // Already adjusted for tile size.
        int minRoomSize = 2;
        int numberOfRooms = 0; // Count how many rooms we've placed.
        // First get a random location for digger to start.
        rand = new Random(System.currentTimeMillis());
        int startX = rand.nextInt(width - 1);
        int startY = rand.nextInt(height -1);
        int pc = 5; // probability of changing direction.
        int pr = 5; // probability of placing room.
        int roll = rand.nextInt(100); // Roll the die.

        Room r = new Room(20,15,10,15);
        rooms.add(r);
        createRoom(r);
        r = new Room(50,15,10,15);
        rooms.add(r);
        createRoom(r);

    }

    private void createRoom(Room r) {
        int a = Math.min(r.x2,map.length-1); // Clamp values to inside of matrix.
        for (int i = r.x; i < a; i++) {
            int b = Math.min(r.x2, map[i].length-1);
            for (int j = r.y; j < b; j++) {
                map[i][j] = true;
            }
        }
    }

    private void createHtunnel(int x1, int x2, int y) {
        int a = Math.min(x1,x2);
        int b = Math.max(x1,x2) + 1;
        for (int i = a; i < b; i++) {
            map[i][y] = true;
        }
    }

    private void createVtunnel(int y1, int y2, int x) {
        int a = Math.min(y1,y2);
        int b = Math.max(y1,y2) + 1;
        for (int i = a; i < b; i++) {
            map[x][i] = true;
        }
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
