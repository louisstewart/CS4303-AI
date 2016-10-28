package dungeon;

import processing.core.*;

/**
 * Created by ls99
 */
public class GameScreen extends PApplet {

    protected int sWidth = Helpers.WIDTH;
    protected int sHeight = Helpers.HEIGHT;
    protected int tileSize = Helpers.TILE;
    protected int SCALE = Helpers.SCALE;
    private int PANEL_HEIGHT = 50;

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
        size(sWidth*SCALE, sHeight*SCALE+PANEL_HEIGHT);
    }

    public void setup() {
        background(121, 76, 19);

        game = new Game(this);

    }

    public void draw() {

        game.render();
    }

    public void keyPressed() {
        game.keyPressed();
    }
}
