package dungeon.dungeon.level;

import dungeon.Helpers;
import dungeon.dungeon.character.Character;
import processing.core.PApplet;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.*;

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
    private Character player;
    private int width = Helpers.WIDTH* Helpers.SCALE/ Helpers.TILE; // Base level width.
    private int height = Helpers.HEIGHT* Helpers.SCALE/ Helpers.TILE; // Base level height.


    private List<Room> rooms = new LinkedList<>();

    public Level(int number, PApplet p, Character player) {
        this.number = number;
        this.p = p;
        this.player = player;

        this.map = new boolean[width][height];

        generateLevelPartition();
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

    private void generateLevelPartition() {
        SPNode root = new SPNode(0, 0, width - 1, height - 1, null);
        int maxRoom = 20; // Total squares in map = 2560, so largest room roughly 1/10th of that.

        List<SPNode> nodes = new ArrayList<>();

        nodes.add(root);

        boolean split = true;

        while (split) {
            split = false;
            for (SPNode l: nodes) {
                if (l.left == null && l.right == null) {
                    // if this Leaf is too big.
                    if (l.width > maxRoom || l.height > maxRoom) {
                        if (l.split()) {
                            nodes.add(l.left);
                            nodes.add(l.right);
                            nodes.remove(l);
                            split = true;
                            break;
                        }
                    }
                }
            }
        }

        for (SPNode n: nodes) {
            Room r = n.createRoom();
            createRoom(r);
            rooms.add(r);
        }
        int s = rooms.size();
        for (int i = 0; i < s; i++) {
            if(i+1 < s) {
                Room c = rooms.get(i);
                Room next = rooms.get(i+1);
                if(next != null) {
                    int iX = c.centerX();
                    int iY = c.centerY();
                    int jX = next.centerX();
                    int jY = next.centerY();
                    createHtunnel(iX, jX, iY);
                    createVtunnel(iY, jY, jX);
                }
            }
        }
    }

    /**
     * Flip the booleans in the map to true to represent a room (rectangle)
     * @param r - the room object to visualise.
     */
    private void createRoom(Room r) {
        for (int i = r.x; i < r.x2; i++) {
            for (int j = r.y; j < r.y2; j++) {
                map[i][j] = true;
            }
        }
    }

    /**
     * Create a horizontal tunnel segment between 2 locations.
     * @param x1 - start
     * @param x2 - end
     * @param y - height of tunnel
     */
    private void createHtunnel(int x1, int x2, int y) {
        int a = Math.min(x1,x2);
        int b = Math.max(x1,x2) + 1;
        for (int i = a; i < b; i++) {
            map[i][y] = true;
        }
    }

    /**
     * Create vertical tunnel segment.
     * @param y1 - start
     * @param y2 - end
     * @param x - tunnel width
     */
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

    /**
     * Generate the map using a cellular automata method.
     *
     * Cellular automata create a more organic cave.
     * After the map is generated, need to check it with flood fill to make sure
     * that the caves are all connected.
     */
    private void generateLevelCellular() {
        double r = 0.3; // Chance of being walkable.
        int born = 4; // Birth limit.
        int die = 3; // Death limit.
        int n = 6; // Iterations for generation.

        // Initialise the map.
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(Math.random() < r) {
                    map[i][j] = true;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            boolean[][] nMap = new boolean[width][height];
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    int alive = 0;
                    for (int l = -1; l <= 1; l++){
                        for (int m = -1; m <= 1; m++){
                            int nX = row + l;
                            int nY = col + m;
                            if (l == 0 && m == 0) continue;

                            if( nX < 0 || nX >= width || nY < 0 || nY >= height ) alive++;
                            else if (map[nX][nY]) alive++;
                        }
                    }
                    if(map[row][col]) {
                        nMap[row][col] = alive >= die;
                    }
                    else {
                        nMap[row][col] = alive >= born;
                    }
                }
            }
            map = nMap;
        }
    }

    /*
     * TODO:  FLOOD FILL LEVEL
     * - need to smooth out the holes in the maps with something.
     */

}
