package dungeon.dungeon.elements;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by Louis on 01/11/2016.
 */
public class Monster extends Character {

    private enum AI {wandering, seeking, testing}
    private AI state;

    private float SPEED = 0.5f;
    private float MAX_SPEED_WANDERING = 0.5f;
    private float MAX_SPEED_SEEKING = 1f;

    private PVector linear ;

    public Monster(int strength, int dex, int health) {
        super(strength, dex, health);
        state = AI.wandering;
    }

    @Override
    public void render(PApplet p) {
        p.fill(255,0,0);
        p.ellipse(position.x, position.y, 16, 16);
    }


    public void integrate(PApplet p, PVector targetPos) {
        if(state == AI.wandering) {
            velocity.x = PApplet.cos(orientation) ;
            velocity.y = PApplet.sin(orientation) ;
            velocity.mult(MAX_SPEED_WANDERING) ;

            position.add(velocity) ;

            // randomly update orientation a little
            orientation += p.random(0, PApplet.PI/64) - p.random(0, PApplet.PI/64) ;

            // Keep in bounds
            if (orientation > PApplet.PI) orientation -= 2*PApplet.PI ;
            else if (orientation < -PApplet.PI) orientation += 2*PApplet.PI ;
        }
        else if(state == AI.seeking) {
            float angular = 0;
            position.add(velocity) ;
            // Apply an impulse to bounce off the edge of the screen
            if ((position.x < 0) || (position.x > p.width)) velocity.x = -velocity.x ;
            if ((position.y < 0) || (position.y > p.height)) velocity.y = -velocity.y ;

            orientation += rotation ;
            if (orientation > PApplet.PI) orientation -= 2*PApplet.PI ;
            else if (orientation < -PApplet.PI) orientation += 2*PApplet.PI ;

            linear.x = targetPos.x - position.x ;
            linear.y = targetPos.y - position.y ;

            linear.normalize() ;
            linear.mult(MAX_ACCEL) ;
            velocity.add(linear) ;
            if (velocity.mag() > MAX_SPEED_SEEKING) {
                velocity.normalize() ;
                velocity.mult(MAX_SPEED_SEEKING) ;
            }

            rotation += angular ;
            if (rotation > MAX_ROTATION) rotation = MAX_ROTATION ;
            else if (rotation  < -MAX_ROTATION) rotation = -MAX_ROTATION ;
        }
        else {
            if(velocity.mag() > MAX_SPEED){
                velocity.normalize();
                velocity.mult(MAX_SPEED);
            }

            position.add(velocity); // Update position vector;

            // a = f * 1/m

            velocity.mult(0.998f); // Hack in the drag for now
        }
        if((position.y > p.height) || (position.y < 0)) {velocity.y = -velocity.y; orientation +=PApplet.PI;}
        if((position.x > p.width) || (position.x < 0)) {velocity.x = -velocity.x; orientation +=PApplet.PI;}
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
}
