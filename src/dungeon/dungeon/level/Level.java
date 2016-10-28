package dungeon.dungeon.level;

import dungeon.Helpers;
import dungeon.dungeon.character.Character;
import processing.core.PApplet;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.Arrays;
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
    private Character player;
    private Random rand;
    private int width = Helpers.WIDTH* Helpers.SCALE/ Helpers.TILE; // Base level width.
    private int height = Helpers.HEIGHT* Helpers.SCALE/ Helpers.TILE; // Base level height.
    private int MAX_ROOMS;

    private List<Room> rooms = new LinkedList<Room>();

    public Level(int number, PApplet p, Character player) {
        this.number = number;
        this.p = p;
        this.player = player;
        this.MAX_ROOMS = number+5 > 30 ? 30 : number+5; // Cap at 30 rooms max, min 6.
        this.map = new boolean[width][height];


        generateLevel();
        populateLevel();
    }

    public void render() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int x = i* Helpers.TILE;
                int y = j* Helpers.TILE;
                if(!map[i][j]) {
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
     * Generate the map using a cellular automata method.
     *
     * Cellular automata create a more organic cave.
     * After the map is generated, need to check it with flood fill to make sure
     * that the caves are all connected.
     */
    private void generateLevel() {
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

    /**
     * Procedurally generate the level using a naive room placer.
     */
    private void generateLevelNaive() {
        int maxRoomSize = 9; // Already adjusted for tile size.
        int minRoomSize = 2;
        int numberOfRooms = 0; // Count how many rooms we've placed.
        // First get a random location for digger to start.
        rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < MAX_ROOMS; i++) {
            int w = (int) (minRoomSize + Math.random()*(maxRoomSize-minRoomSize));
            int h = (int) (minRoomSize + Math.random()*(maxRoomSize-minRoomSize));
            int x;
            int y;

            x = rand.nextInt(width - w - 1);
            y = rand.nextInt(height - h - 1);

            Room r = new Room(x,y,w,h);
            createRoom(r);

            if(numberOfRooms == 0) {
                this.player.position = new PVector(x, y);
            }
            else {
                Room last = rooms.get(numberOfRooms - 1);
                createHtunnel(last.centerX(), r.centerX(), last.centerY());
                createVtunnel(last.centerY(), r.centerY(), last.centerX());
            }
            rooms.add(r);
            numberOfRooms++;
        }
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
