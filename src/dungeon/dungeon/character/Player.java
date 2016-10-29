package dungeon.dungeon.character;

import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Louis on 28/10/2016.
 */
public class Player extends Character {

    private int magic;
    private List<Item> inventory = new LinkedList<>();

    public Player(PApplet p, int x, int y, float or, float xVel, float yVel, float rot, int strength, int dex, int health, int magic) {
        super(p, x, y, or, xVel, yVel, rot, strength, dex, health);
        this.magic = magic;
    }

    public List<Item> getInventory() { return inventory; }

    public void setInventory(List<Item> inventory) { this.inventory = inventory; }

    public void addItem(Item i) { this.inventory.add(i); }

    public int getMagic() { return magic; }

    public void setMagic(int magic) { this.magic = magic; }
}
