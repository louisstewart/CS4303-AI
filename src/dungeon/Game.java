package dungeon;

import dungeon.dungeon.display.Camera;
import dungeon.dungeon.elements.ElementContainer;
import dungeon.dungeon.elements.MapElement;
import dungeon.dungeon.elements.Player;
import dungeon.dungeon.level.Level;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by Louis on 17/10/2016.
 */
public class Game {

    PApplet p;
    Level level;
    ElementContainer objects;
    Player player;
    Camera camera;
    MapElement map;



    public Game(PApplet p) {
        this.p = p;
        this.objects = new ElementContainer();
        this.camera = new Camera(p);
        this.player = new Player(10, 10, 10, 10);
        this.level = new Level(1);
        this.map = new MapElement(level);

        // Place the player in the map.
        //level.placePlayer(player);
        player.position = new PVector(15,15);

        // Add player and map to objects needing rendered.
        objects.add(map);
        objects.add(player);
    }

    public void render() {
        int roomX = (int) player.position.x / (Helpers.WIDTH/ 2);
        int roomY = (int) player.position.y / (Helpers.HEIGHT/ 2);

        // draw
        p.background(200.0f, 200.0f, 200.0f);
        //camera.setRoom(roomX, roomY);
        //camera.begin();
        objects.render(p);
        //camera.end();

    }

    public void keyPressed() {
        if(p.key == PApplet.CODED) {
            switch(p.keyCode) {
                case PApplet.LEFT:
                    player.moveLeft();
                    break;
                case PApplet.RIGHT:
                    player.moveRight();
                    break;
                case PApplet.UP:
                    player.moveUp();
                    break;
                case PApplet.DOWN:
                    player.moveDown();
                    break;
            }
        }
        else switch (p.key) {
            case '1':
                level = new Level(1);
                player.position = new PVector(15,15);
                map.set(level);
                break;

        }
    }

}
