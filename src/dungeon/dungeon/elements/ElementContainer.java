package dungeon.dungeon.elements;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 31/10/2016.
 */
public class ElementContainer extends Element {

    public List<Character> monsters = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public Player player;
    public ExitNode exit;


    public void integrate(PApplet p) {
        player.integrate(p);
        for (Element el: monsters) {
            el.integrate(p);
        }
    }

    @Override
    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(this.position.x, this.position.y);
        p.rotate(rotation);
        p.scale(scaleX, scaleY);
        // Render immovables first.
        exit.render(p);
        for (Item i: items) {
            i.render(p);
        }
        for (Element el : monsters) {
            el.render(p);
        }
        player.render(p); // Render player last.
        p.popMatrix();
    }
}
