package dungeon.elements;

import processing.core.*;

/**
 * Created by ls99
 */
public abstract class Character extends Element {

    protected PApplet p; // The parent application - stores all of the processing methods and variables.

    // Character Attributes - both NPC and player share these
    protected int strength; // Attack stat.
    protected int dex; // Dexterity improves movement speed.
    protected int health;
    protected int defence;
    protected int MAX_HEALTH;

    public PImage img;
    public int width;

    public Character(PImage img, int strength, int dex, int health, int defence) {
        this.img = img;
        this.position = new PVector(0, 0);
        this.orientation = 0;
        this.velocity = new PVector(0, 0);
        this.rotation = 0;
        this.strength = strength;
        this.dex = dex;
        this.health = health;
        this.defence = defence;
        this.MAX_HEALTH = health;
    }

    /************************************************************************
     * MUTATORS
     ************************************************************************/
    public void setHealth(int health) { this.health = health; }

    public void setDex(int dex) { this.dex = dex; }

    public void setStrength(int strength) { this.strength = strength; }

    public void setMAX_HEALTH(int h) { this.MAX_HEALTH = h; }

    public void incrementDex(int amount) { this.dex+=amount; }

    public void incrementStrength(int amount) { this.strength+=amount; }

    public void incrementDefence(int amount) { this.defence+=amount; }

    public void incrementMaxHealth(int amount) { this.MAX_HEALTH+=amount; }

    public void incrementHealth(int amount) {
        this.health = this.health+amount < this.MAX_HEALTH ? this.health+=amount : this.MAX_HEALTH;
    }

    /************************************************************************
     * ACCESSORS
     ************************************************************************/
    public int getHealth() { return this.health;}

    public int getDex() { return this.dex;}

    public int getStrength() { return this.strength;}

    public int getMAX_HEALTH() { return this.MAX_HEALTH; }

    public int getDefence() { return defence; }


}
