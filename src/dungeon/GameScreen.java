package dungeon;

import processing.core.*;

/**
 * Created by ls99
 */
public class GameScreen extends PApplet {

    protected int sWidth = Constants.WIDTH;
    protected int sHeight = Constants.HEIGHT;
    protected int tileSize = Constants.TILE;
    protected int SCALE = Constants.SCALE;

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

        game = new Game(this);

    }

    public void draw() {

        int d = 20;

        fill(153);
        rect(100, 200, d, d);
    }
}
