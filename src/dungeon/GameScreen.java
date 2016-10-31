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
    private int PANEL_HEIGHT = 50;
    Logger logger = Logger.getLogger(GameScreen.class.getName());

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
        size(sWidth*SCALE, sHeight*SCALE);
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
        game = new Game(this);

    }

    public void draw() {

        game.render();
    }

    public void keyPressed() {
        game.keyPressed();
    }
}
