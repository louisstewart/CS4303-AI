package dungeon;

import processing.core.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by ls99
 */
public class GameScreen extends PApplet {

    protected int sWidth = Helpers.WIDTH;
    protected int sHeight = Helpers.HEIGHT;
    protected int tileSize = Helpers.TILE;
    protected int SCALE = Helpers.SCALE;

    Logger logger = Logger.getLogger(GameScreen.class.getName());

    public static final int PANEL_HEIGHT = 50;
    public static PImage player;
    public static PImage monster;
    public static PImage monsterHard;
    public static PImage ground;
    public static PImage [] wall;
    public static PImage exit;
    public static PImage armour;
    public static PImage chest;
    public static PImage potion;
    public static PImage wand;
    public static PImage boots;
    public static PImage sword;

    private Game game;

    static public void main(String[] passedArgs) {

        String[] appletArgs = new String[]{"dungeon.GameScreen"};

        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        }
        else {
            PApplet.main(appletArgs);
        }
    }

    public void settings() {
        size(sWidth*SCALE, sHeight*SCALE + PANEL_HEIGHT);
    }

    public void setup() {
        background(121, 76, 19);

        try {
            FileHandler fh = new FileHandler("Game.log");
            logger.addHandler(fh);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player = loadImage("player.png");
        monster = loadImage("monster.png");
        monsterHard = loadImage("monster-hard.png");
        ground = loadImage("ground.png");
        PImage [] w = {loadImage("wall.png"), loadImage("wall1.png")};
        wall = w;
        exit = loadImage("exitportal.png");
        boots = loadImage("dexboots.png");
        sword = loadImage("sword.png");
        armour = loadImage("armour.png");
        chest = loadImage("chest.png");
        potion = loadImage("potion.png");
        wand = loadImage("wand.png");

        game = new Game(this);


    }

    public void draw() {
        game.tick();
        game.render();
    }

    public void keyPressed() {
        game.keyPressed();
    }

    public void keyReleased() {
        game.keyReleased();
    }
}
