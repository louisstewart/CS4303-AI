package dungeon.dungeon.elements;

import dungeon.Helpers;
import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Louis on 28/10/2016.
 */
public class Player extends Character {

    private int magic;
    private List<Item> inventory = new LinkedList<>();
    private float SPEED = 0.8f * Helpers.SCALE;

    public Player(int strength, int dex, int health, int magic) {
        super(strength, dex, health);
        this.magic = magic;
    }

    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(position.x, position.y);
        p.rotate(rotation);
        p.scale(scaleX, scaleY);

        p.fill(123);
        p.ellipse(this.position.x, this.position.y, 12, 12);

        p.popMatrix();

    }

    public void moveUp() {
        this.position.y -= SPEED;
        System.out.printf("Player pos x = %f, player pos y = %f \n", position.x, position.y);
    }

    public void moveDown() {
        this.position.y += SPEED;
        System.out.printf("Player pos x = %f, player pos y = %f \n", position.x, position.y);
    }

    public void moveLeft() {
        this.position.x -= SPEED;
        System.out.printf("Player pos x = %f, player pos y = %f \n", position.x, position.y);
    }

    public void moveRight() {
        this.position.x += SPEED;
        System.out.printf("Player pos x = %f, player pos y = %f \n", position.x, position.y);
    }

    public List<Item> getInventory() { return inventory; }

    public void setInventory(List<Item> inventory) { this.inventory = inventory; }

    public void addItem(Item i) { this.inventory.add(i); }

    public int getMagic() { return magic; }

    public void setMagic(int magic) { this.magic = magic; }
}
