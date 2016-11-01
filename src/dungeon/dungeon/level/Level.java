package dungeon.dungeon.level;

import dungeon.Helpers;
import dungeon.dungeon.elements.*;
import dungeon.dungeon.elements.Character;
import processing.core.PApplet;

import java.util.*;

/**
 * Created by ls99.
 *
 * A level contains a map representing the game board,
 * as well
 */
public class Level {

    private int levelNumber;
    private Tile[][] map;
    private ElementContainer objects;

    private int width = Helpers.WIDTH* Helpers.SCALE/ Helpers.TILE; // Base level width.
    private int height = Helpers.HEIGHT* Helpers.SCALE/ Helpers.TILE; // Base level height.


    private List<Room> rooms = new LinkedList<>();

    public Level(int number, ElementContainer ec) {
        this.levelNumber = number;
        this.objects = ec;

        this.map = new Tile[width][height];

        /*MapElement me = */generateLevelPartition();
        //objects.add(me);

        populateLevel();
    }

    public void render(PApplet p) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int x = i* Helpers.TILE;
                int y = j* Helpers.TILE;
                if(map[i][j].walkable) {
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

    public Tile[][] getMap() {
        return this.map;
    }

    public void placePlayer(Player player) {
        Room start = rooms.get(0);
        player.position.x = start.centerX() * (Helpers.TILE/2);
        player.position.y = start.centerY() * (Helpers.TILE/2);
        System.out.printf("Room[0].centerX = %d, rooms[0].centerY = %d \n",start.centerX(), start.centerY());
        System.out.printf("Player at x: %f , y: %f \n", player.position.x, player.position.y);
    }

    private MapElement generateLevelPartition() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Tile(false);
            }
        }
        SPNode root = new SPNode(0, 0, width - 1, height - 1, null);
        int maxRoom = 10; // Total squares in map = 2560, so largest room roughly 1/10th of that.

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
        MapElement rtn = new MapElement(map); // Return a displayable object.
        return rtn;
    }

    /**
     * Flip the booleans in the map to true to represent a room (rectangle)
     * @param r - the room object to visualise.
     */
    private void createRoom(Room r) {
        for (int i = r.x; i < r.x2; i++) {
            for (int j = r.y; j < r.y2; j++) {
                map[i][j].walkable = true;
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
            map[i][y].walkable = true;
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
            map[x][i].walkable = true;
        }
    }

    /**
     * Fill the level up with some content, based on the level number.
     *
     * The higher the number, the more goodies and enemies will be placed\
     * onto the grid.
     */
    private void populateLevel() {
        // First put in the exit node.
        ExitNode e = null;
        // Select a random room that is not the same room as the player spawns in.
        int room = (int)(1 + Math.random() * (rooms.size() - 1));
        Room r = rooms.get(room);
        int exitX = r.centerX() * Helpers.TILE; // Put in the center of the room.
        int exitY = r.centerY() * Helpers.TILE;
        exitX = exitX + Helpers.TILE/2;
        exitY = exitY + Helpers.TILE/2;
        e = new ExitNode(null, exitX, exitY);
        objects.exit = e; // Add it to the scene to be rendered.

        int numMonsters = levelNumber < 5 ? 5 : levelNumber; // Some amount of monsters.
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
                    map[i][j].walkable = true;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            Tile[][] nMap = new Tile[width][height];
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    int alive = 0;
                    for (int l = -1; l <= 1; l++){
                        for (int m = -1; m <= 1; m++){
                            int nX = row + l;
                            int nY = col + m;
                            if (l == 0 && m == 0) continue;

                            if( nX < 0 || nX >= width || nY < 0 || nY >= height ) alive++;
                            else if (map[nX][nY].walkable) alive++;
                        }
                    }
                    if(map[row][col].walkable) {
                        nMap[row][col].walkable = alive >= die;
                    }
                    else {
                        nMap[row][col].walkable = alive >= born;
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
