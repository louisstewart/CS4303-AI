package dungeon;

import processing.core.*;

/**
 * Created by ls99 on 17/10/2016.
 */
public class GameScreen extends PApplet {

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
        size(1200, 700);
    }

    public void setup() {
        background(0);
    }

    public void draw() {

        int d = 20;

        fill(153);
        rect(100, 200, d, d);
    }
}
