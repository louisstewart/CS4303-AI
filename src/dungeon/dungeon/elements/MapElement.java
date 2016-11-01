package dungeon.dungeon.elements;

import dungeon.Helpers;
import dungeon.dungeon.level.Level;
import dungeon.dungeon.level.Tile;
import processing.core.PApplet;

/**
 * Created by ls99
 */
public class MapElement extends Element {

    private Tile[][] map;

    public MapElement(Tile[][] l) {

        this.map = l;
    }

    public void set(Tile[][] l) {
        this.map = l;
    }

    @Override
    public void render(PApplet p) {
        //boolean[][] map = level.getMap();
        /*for (int i = 0; i < map.length; i++) {
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
        }*/
    }
}
