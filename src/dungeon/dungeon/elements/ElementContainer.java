package dungeon.dungeon.elements;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 31/10/2016.
 */
public class ElementContainer extends Element {

    private List<Element> elems = new ArrayList<>();

    public void add(Element child) {
        elems.add(child);
    }

    public void remove(Element child) {
        elems.remove(child);
    }

    @Override
    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(this.position.x, this.position.y);
        p.rotate(rotation);
        p.scale(scaleX, scaleY);
        for (Element el : elems) {
            el.render(p);
        }
        p.popMatrix();
    }
}
