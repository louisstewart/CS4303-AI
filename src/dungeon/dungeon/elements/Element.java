package dungeon.dungeon.elements;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by ls99
 */
public abstract class Element {

    // Game movement constants
    protected final float MAX_SPEED = 1f ;
    protected final float MAX_ACCEL = 0.1f ;
    protected final float MAX_ROTATION = PApplet.PI/4 ;

    // Public access for speed - like in example.
    public PVector position = new PVector(0,0);
    public float orientation = 0f;

    public PVector velocity = new PVector(0,0);
    public float rotation = 0f;

    public float scaleX = 1f, scaleY = 1f;

    public abstract void render(PApplet p);
}
