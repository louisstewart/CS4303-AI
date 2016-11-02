package dungeon.elements;

import dungeon.Helpers;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by Louis on 29/10/2016.
 */
public class Item extends Element{

    private String name;
    private String stats;
    private Attribute attr;
    private int bonus;

    public PImage img;
    public int width;

    public Item(PImage img, String name, String stats, Attribute attr, int bonus) {
        this.img = img;
        this.name = name;
        this.stats = stats;
        this.attr = attr; // The value to increase (strength, dexterity, magic, gold, defence)
        this.bonus = bonus; // The amount that this item increases a value.
        this.width = Helpers.TILE;
    }

    public void render(PApplet p) {
        if(this.img != null) {
            p.image(img, this.position.x, this.position.y);
        }
        else {
            p.fill(255, 120, 120);
            p.rect(this.position.x, this.position.y, Helpers.TILE, Helpers.TILE);
        }
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStats() { return stats; }

    public void setStats(String stats) { this.stats = stats; }

    public Attribute getAttr() { return attr; }

    public void setAttr(Attribute attr) { this.attr = attr; }

    public int getBonus() { return bonus; }

    public void setBonus(int bonus) { this.bonus = bonus; }
}
