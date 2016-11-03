package dungeon.elements;

import dungeon.Helpers;
import dungeon.level.Level;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louis on 28/10/2016.
 */
public class Player extends Character {

    private int magic;
    private int gold;
    private int exp;
    private int score;
    private int MAX_MAGIC;
    private boolean wand;

    public List<Item> potions = new ArrayList<>();
    public Item shield;
    public Item sword;
    public Item magicwand;
    public Item boots;

    private float SPEED = 1f;

    public Player(PImage img, int strength, int dex, int health, int defence, int magic) {
        super(img, strength, dex, health, defence);
        this.magic = magic;
        this.MAX_MAGIC = magic;
        this.movable = true;
        this.exp = 0;
        this.width = Helpers.TILE-3;
    }

    public void render(PApplet p) {
        //p.pushMatrix();
        //p.translate(position.x, position.y);
        //p.rotate(rotation);
        //p.scale(scaleX, scaleY);

        if(this.img != null) {
            p.image(img, position.x, position.y, width, width);
        }
        else {
            p.fill(123);
            p.ellipse(position.x, position.y, width, width);
        }
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

    public void stopVertical() {
        this.velocity.y = 0;
    }

    public void stopHorizontal() {
        this.velocity.x = 0;
    }

    public void print(Level level) {
        System.out.printf("Player x = %f, player y = %f \n", position.x, position.y);
        System.out.println("Player X tile = "+(position.x / Helpers.TILE));
        System.out.println("Player Y tile = "+(position.y / Helpers.TILE));
        int neX = (int)Math.floor(((position.x+1 + velocity.x + width))/ Helpers.TILE);
        int neY = (int)Math.floor(((position.y+1 + velocity.y + width))/ Helpers.TILE);
        System.out.println("neXt = " + neX + "walkable = "+level.getMap()[neX][neY].walkable);
        System.out.println("nextY = " + neY);

    }

    public void integrate(PApplet p) {
        float MAX_SPEED = 2f + 5 * (getDex()+0.0f)/100;
        if(velocity.mag() > MAX_SPEED){
            velocity.normalize();
            velocity.mult(MAX_SPEED);
        }

        position.add(velocity); // Update position vector;


        if((position.y > p.height) || (position.y < 0)) velocity.y = -velocity.y;
        if((position.x > p.width) || (position.x < 0)) velocity.x = -velocity.x;

    }

    public int usePotion() {
        int heal = 0;
        if(health == MAX_HEALTH)  return 0;
        if (potions.size() > 0 && health < MAX_HEALTH) {
            Item p = potions.get(0);
            potions.remove(p);
            heal = health + p.getBonus() > MAX_HEALTH ? MAX_HEALTH-health : p.getBonus(); // Work out how much healed by
            health = health + p.getBonus() > MAX_HEALTH ? MAX_HEALTH : health + p.getBonus(); // Set health
            return heal;
        }
        else {
            return -1;
        }

    }

    public int getMagic() { return magic; }

    public void incrementMagic(int magic) { this.magic = this.magic + magic > MAX_MAGIC ? MAX_MAGIC : this.magic + magic; }

    public void decrementMagic(int magic) { this.magic = this.magic - magic < 0 ? 0 : this.magic - magic; }

    public int getGold() { return gold; }

    public void setGold(int gold) { this.gold = gold; }

    public void incrementGold(int gold) { this.gold += gold; }

    public int getExp() { return exp; }

    public void setExp(int exp) { this.exp = exp; }

    public boolean hasWand(){ return this.wand; }

    public void giveWand() { this.wand = true; }

    public int getMAX_MAGIC() { return MAX_MAGIC; }

    public void incrementMaxMagic(int max_magic) { this.MAX_MAGIC += max_magic; }

    /**
     * Return the player score. A function of gold, experience, and items.
     * @return the score
     */
    public int getScore(int currentlevel) {
        int score = 0;
        score += 10 * inventorySize();
        score += gold + exp * potions.size() + currentlevel * 10;
        return score;
    }

    public int inventorySize() {
        int size = 0;
        if(sword != null) size++;
        if(shield != null) size++;
        if(magicwand != null) size++;
        if(boots != null) size++;
        size += potions.size();
        return size;
    }

    public List<Item> getInventory() {
        List<Item> rtn = new ArrayList<>();
        if(sword != null) rtn.add(sword);
        if(shield != null) rtn.add(shield);
        if(magicwand != null) rtn.add(magicwand);
        if(boots != null) rtn.add(boots);
        return rtn;
    }
}
