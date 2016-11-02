package dungeon.elements;

import dungeon.Helpers;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Louis on 01/11/2016.
 */
public class ExitNode extends Element {

    private PImage img;
    public int width = Helpers.TILE;
    public int height = Helpers.TILE;

    public ExitNode(PImage img, int x, int y) {
        this.img = img;
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void render(PApplet p) {
        if(img != null) p.image(img, this.position.x, this.position.y);
        else {
            p.fill(10,205,34);
            p.ellipse(this.position.x, this.position.y, width, height);
        }
    }

}
