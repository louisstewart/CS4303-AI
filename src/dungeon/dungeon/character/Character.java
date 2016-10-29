package dungeon.dungeon.character;

import dungeon.GameScreen;
import processing.core.*;

/**
 * Created by Louis on 17/10/2016.
 */
public abstract class Character {

    PApplet p; // The parent application - stores all of the processing methods and variables.

    // Game movement constants
    protected final float MAX_SPEED = 1f ;
    protected final float MAX_ACCEL = 0.1f ;
    protected final float MAX_ROTATION = p.PI/4 ;

    // Character Attributes - both NPC and player share these
    private int strength; // Attack stat.
    private int dex; // Dexterity improves movement speed.
    private int health;
    private int MAX_HEALTH;

    // Public access for speed - like in example.
    public PVector position;
    public float orientation;

    public PVector velocity;
    public float rotation;

    private PVector linear;

    public Character(PApplet p, int x, int y, float or, float xVel, float yVel, float rot, int strength, int dex, int health) {
        position = new PVector(x, y);
        orientation = or;
        velocity = new PVector(xVel, yVel);
        rotation = rot;
        linear = new PVector(0, 0);
        this.strength = strength;
        this.dex = dex;
        this.health = health;
        this.MAX_HEALTH = health;
    }

    // update position, orientation, velocity and rotation
    void integrate(PVector targetPos, float angular) {
        position.add(velocity);
        // Apply an impulse to bounce off the edge of the screen
        if ((position.x < 0) || (position.x > p.width)) velocity.x = -velocity.x;
        if ((position.y < 0) || (position.y > p.height)) velocity.y = -velocity.y;

        orientation += rotation;
        if (orientation > p.PI) orientation -= 2*p.PI;
        else if (orientation < -p.PI) orientation += 2*p.PI;

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
