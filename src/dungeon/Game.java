package dungeon;

import dungeon.dungeon.GameState;
import dungeon.dungeon.display.Camera;
import dungeon.dungeon.elements.Element;
import dungeon.dungeon.elements.ElementContainer;
import dungeon.dungeon.elements.Player;
import dungeon.dungeon.level.Level;
import dungeon.dungeon.level.Tile;
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
    GameState state;

    public Game(PApplet p) {
        this.p = p;
        this.objects = new ElementContainer();
        this.camera = new Camera(p);
        this.player = new Player(10, 10, 10, 10);
        this.level = new Level(1, objects);
        //this.map = new MapElement(level);
        System.out.printf("Map size x = %d Map size y = %d \n",level.getMap().length, level.getMap()[1].length);

        // Place the player in the map.
        level.placePlayer(player);
        //player.position = new PVector(15,15);

        // Add player to objects needing rendered.
        objects.player = player;
        state = GameState.startGame;
    }

    public void tick() {
        detectCollisions(); // Detect all the collisions.
        objects.integrate(p);
    }

    public void render() {
        int roomX = (int) player.position.x / (Helpers.WIDTH/ 2);
        int roomY = (int) player.position.y / (Helpers.HEIGHT/ 2);

        // draw
        p.background(200.0f, 200.0f, 200.0f);
        //camera.setRoom(roomX, roomY);
        //camera.begin();
        level.render(p);
        objects.render(p);
        //camera.end();

    }

    public void keyReleased() {
        player.stop();
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
            player.print();
        }
        else switch (p.key) {
            case '1':
                objects = new ElementContainer();
                level = new Level(1, objects);
                level.placePlayer(player);
                objects.player = player;
                break;

        }
    }

    public void detectCollisions() {
        Tile[][] map = level.getMap();

        /// Handle the player collision with walls first.
        int eX = (int)Math.floor(((player.position.x + player.velocity.x)*2)/ Helpers.TILE);
        int eY = (int)Math.floor(((player.position.y + player.velocity.y)*2)/ Helpers.TILE);

        if(eX < map.length && eY < map[1].length && !map[eX][eY].walkable) {
            // Work out direction to get back to safety
            player.velocity.x = 0;
            player.velocity.y = 0;
        }

        /// Now monsters and walls.
        for (Element e : objects.monsters) {
            if(!e.movable) continue;
            // Get position in the boolean representation of map.
            eX = (int)Math.floor(((e.position.x + e.velocity.x)*2)/ Helpers.TILE);
            eY = (int)Math.floor(((e.position.y + e.velocity.y)*2)/ Helpers.TILE);

            if(eX < map.length && eY < map[1].length && !map[eX][eY].walkable) {
                // Work out direction to get back to safety
                e.velocity.x = 0;
                e.velocity.y = 0;
            }
        }

        // Player and exit node
        PVector dist = new PVector(player.position.x - objects.exit.position.x, player.position.y - objects.exit.position.y);

        if(dist.mag() <= player.width) {
            state = GameState.levelOver;
            return;
        }
    }

}
