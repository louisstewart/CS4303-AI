package dungeon.dungeon.elements;

import processing.core.*;

/**
 * Created by ls99
 */
public abstract class Character extends Element {

    protected PApplet p; // The parent application - stores all of the processing methods and variables.

    // Character Attributes - both NPC and player share these
    private int strength; // Attack stat.
    private int dex; // Dexterity improves movement speed.
    private int health;
    private int MAX_HEALTH;

    private PVector linear;

    public Character(int strength, int dex, int health) {
        this.position = new PVector(0, 0);
        this.orientation = 0;
        this.velocity = new PVector(0, 0);
        this.rotation = 0;
        this.linear = new PVector(0, 0);
        this.strength = strength;
        this.dex = dex;
        this.health = health;
        this.MAX_HEALTH = health;
    }

    // update position, orientation, velocity and rotation
    public void integrate(PVector targetPos, float angular) {
        position.add(velocity);
        // Apply an impulse to bounce off the edge of the screen
        if ((position.x < 0) || (position.x > p.width)) velocity.x = -velocity.x;
        if ((position.y < 0) || (position.y > p.height)) velocity.y = -velocity.y;

        orientation += rotation;
        if (orientation > PApplet.PI) orientation -= 2*PApplet.PI;
        else if (orientation < -PApplet.PI) orientation += 2*PApplet.PI;

        linear.x = targetPos.x - position.x;
        linear.y = targetPos.y - position.y;

        linear.normalize();
        linear.mult(MAX_ACCEL);
        velocity.add(linear);
        if (velocity.mag() > MAX_SPEED) {
            velocity.normalize();
            velocity.mult(MAX_SPEED);
        }

        rotation += angular;
        if (rotation > MAX_ROTATION) rotation = MAX_ROTATION;
        else if (rotation  < -MAX_ROTATION) rotation = -MAX_ROTATION;
    }

    /************************************************************************
     * MUTATORS
     ************************************************************************/
    public void setHealth(int health) { this.health = health; }

    public void setDex(int dex) { this.dex = dex; }

    public void setStrength(int strength) { this.strength = strength; }

    public void setMAX_HEALTH(int h) { this.MAX_HEALTH = h; }

    public void incrementDex() { this.dex++; }

    public void incrementStrength() { this.strength++; }

    public void incrementMaxHealth() { this.MAX_HEALTH++; }

    /************************************************************************
     * ACCESSORS
     ************************************************************************/
    public int getHealth() { return this.health;}

    public int getDex() { return this.dex;}

    public int getStrength() { return this.strength;}

    public int getMAX_HEALTH() { return this.MAX_HEALTH; }
}