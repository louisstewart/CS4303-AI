package dungeon.dungeon.elements;

import com.sun.tools.corba.se.idl.toJavaPortable.Helper;
import dungeon.Helpers;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Louis on 28/10/2016.
 */
public class Player extends Character {

    public int width = 16;

    private int magic;

    private int gold;
    private List<Item> inventory = new LinkedList<>();
    private float SPEED = 1f;
    private float MAX_SPEED = 2f;
    private float DAMPING = 0.998f;

    public Player(int strength, int dex, int health, int magic) {
        super(strength, dex, health);
        this.magic = magic;
        this.movable = true;
    }

    public void render(PApplet p) {
        //p.pushMatrix();
        //p.translate(position.x, position.y);
        //p.rotate(rotation);
        //p.scale(scaleX, scaleY);

        p.fill(123);
        p.ellipse(this.position.x, this.position.y, width, width);

        //p.popMatrix();

    }

    public void moveUp() {
        this.velocity.y += -SPEED;

    }

    public void moveDown() {
        this.velocity.y += SPEED;

    }

    public void moveLeft() {
        this.velocity.x += -SPEED;

    }

    public void moveRight() {
        this.velocity.x += SPEED;

    }

    public void stop() {
        this.velocity.x = 0;
        this.velocity.y = 0;
    }

    public void print() {
        System.out.printf("Player x = %f, player y = %f \n", position.x, position.y);
        System.out.println("Player X tile = "+(position.x / Helpers.TILE));
        System.out.println("Player Y tile = "+(position.y / Helpers.TILE));
    }

    public void integrate(PApplet p) {
        if(velocity.mag() > MAX_SPEED){
            velocity.normalize();
            velocity.mult(MAX_SPEED);
        }

        position.add(velocity); // Update position vector;

        // a = f * 1/m

        velocity.mult(DAMPING); // Hack in the drag for now


        if((position.y > p.height) || (position.y < 0)) velocity.y = -velocity.y;
        if((position.x > p.width) || (position.x < 0)) velocity.x = -velocity.x;

    }

    public List<Item> getInventory() { return inventory; }

    public void setInventory(List<Item> inventory) { this.inventory = inventory; }

    public void addItem(Item i) { this.inventory.add(i); }

    public int getMagic() { return magic; }

    public void setMagic(int magic) { this.magic = magic; }

    public int getGold() { return gold; }

    public void setGold(int gold) { this.gold = gold; }
}
