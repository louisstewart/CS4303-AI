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
            p.image(img, position.x, position.y, width, width);
        }
        else {
            p.fill(255, 120, 120);
            p.rect(position.x, position.y, width, width);
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

    public String getBonusString() {
        String rtn = "";
        switch (attr) {
            case STRENGTH:
                rtn = "Strength";
                break;
            case DEFENCE:
                rtn = "Defence";
                break;
            case DEXTERITY:
                rtn = "Dexterity";
                break;
            case MAGIC:
                rtn = "Magic";
                break;
        }
        return rtn;
    }
}
