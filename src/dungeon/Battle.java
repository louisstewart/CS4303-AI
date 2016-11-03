package dungeon;

import dungeon.elements.Monster;
import dungeon.elements.Player;
import processing.core.PApplet;

/**
 * Created by Louis on 02/11/2016.
 */
public class Battle {

    public String message = "";

    private Player player;
    private Monster monster;
    private Game game;
    private boolean playerDefend, enemyDefend;

    private enum BS { yourTurn, enemyTurn, won, lost }
    private BS state;
    private long timer;


    public Battle(Game game, Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.game = game;
        this.state = BS.yourTurn;
    }

    public void doBattle(char key) {
        if(state == BS.yourTurn) {
            if(playerDefend) playerDefend = false;
            int damage = 0;
            switch (key) {
                case 'd': // D for defence. Simplest case.
                    playerDefend = true;
                    state = BS.enemyTurn;
                    message = "You put up your shield";
                    timer = System.currentTimeMillis()+2000;
                    return;
                case 'a': // A for attack.
                    // 99% chance of attack.
                    if(Math.random() >= 0.99f) {
                        message = "Your attack missed...";
                        state = BS.enemyTurn;
                        timer = System.currentTimeMillis()+2000;
                        return;
                    }
                    damage = player.getStrength();
                    break;
                case 'm': // Magic attack
                    if(player.getMagic() > 0 && player.hasWand()) {
                        damage = player.getMagic()/2; // Attack strength is half of Magic points.
                        player.decrementMagic(damage/2);
                        break;
                    }
                    else {
                        this.message = "Not enough MP to attack.. Try something else!";
                        timer = System.currentTimeMillis()+2000;
                        return;
                    }
                case 'h': // Heal using half of remaining magic.
                    if(player.getMagic() > 0) {
                        // Get half of magic.
                        int heal = player.getMagic() /2 ;
                        player.incrementHealth(heal);
                        player.decrementMagic(heal);
                        this.message = "Healed by "+heal+" HP!";
                        timer = System.currentTimeMillis()+2000;
                        state = BS.enemyTurn;
                        return; // Break out of this method because we didn't hit enemy.
                    }
                    else {
                        this.message = "Not enough MP to heal.. Try something else!";
                        timer = System.currentTimeMillis()+2000;
                        return; // Nothing happened so break out and let user try again.
                    }
                case 'p': // Use a potion to heal.
                    int heal = player.usePotion();
                    if(heal > 0) {
                        this.message = "Healed by " + heal + " HP!";
                        timer = System.currentTimeMillis() + 2000;
                        state = BS.enemyTurn;
                        return; // Same as above.
                    }
                    else if(heal == 0) {
                        this.message = "Full health already! Try again..";
                        timer = System.currentTimeMillis() + 2000;
                        return;
                    }
                    else {
                        this.message = "No potions left! Try again..";
                        timer = System.currentTimeMillis() + 2000;
                        return;
                    }
            }
            // Calculate the damage done - If we hit the monster anyway.
            if(enemyDefend) {
                damage = monster.getDefence() >= damage ? 0 : damage - monster.getDefence();
            }
            int nh = monster.getHealth() - damage; // Calculate monster's new health.
            nh = nh <= 0 ? 0 : nh;
            monster.setHealth(nh); // Update monster
            message = "Hit the enemy for "+damage+" damage!"; // Write a message
            if(monster.getHealth() == 0) {
                state = BS.won; // We won!
                // Add in the spoils to the user.
                player.setGold(player.getGold()+getBonusGold());
                player.setExp(player.getExp()+getBonusExp());
            }
            else {
                state = BS.enemyTurn; // Their turn now.
            }
            timer = System.currentTimeMillis()+2000;
        }

    }

    public void tick() {
        if(state == BS.enemyTurn && System.currentTimeMillis() >= timer) {
            // 50% chance of attacking.
            enemyDefend = false;
            if(Math.random() > 0.5f) {
                // 92% chance of hitting.
                state = BS.yourTurn;
                if (Math.random() > 0.92f) {
                    message = "The " + monster.name + " missed!";
                    return;
                }
                int damage = monster.getStrength();
                if (playerDefend) {
                    damage = monster.getStrength() < player.getDefence() ? 0 : monster.getStrength() - player.getDefence();
                }

                player.setHealth(player.getHealth() - damage);
                message = "Foe attacked for "+damage+" damage";
                if (player.getHealth() <= 0) {
                    player.setHealth(0);
                    state = BS.lost;
                    message = "You were defeated";
                    timer = System.currentTimeMillis() + 2000;
                }
            }
            else {
                enemyDefend = true;
                state = BS.yourTurn;
                message = "The foe put up its defences";
            }
        }
        if(state ==  BS.lost && System.currentTimeMillis() >= timer) {
            game.state = GameState.gameOver;
        }
        if(state == BS.won && System.currentTimeMillis() >= timer) {
            game.objects.monsters.remove(monster); // Slay the beast.
            game.state = GameState.battleWon;
            game.timer = System.currentTimeMillis() + 3000;
        }
    }

    public void render(PApplet p) {
        // Set the scene
        p.background(51);
        p.fill(255);
        p.textAlign(PApplet.LEFT);
        p.textSize(15);
        if(state != BS.won) {
            p.text("You encountered a "+ monster.name, 50,100);
        }
        if(state == BS.yourTurn) {
            p.text("Your turn!", 50, 340);
        }
        if(state == BS.enemyTurn) {
            p.text("Enemy turn!", 50, 340);
        }
        p.text("Actions: ", 50, 130);
        p.text("A - Attack", 80, 160);
        if(player.hasWand()) {
            p.text("M - Magic Attack", 80, 190);
            p.text("D - Defend", 80, 220);
            p.text("H - Healing spell", 80, 250);
            p.text("P - Use potion", 80, 280);
            p.text("Select an action...", 50, 310);
        }
        else {
            p.text("D - Defend", 80, 190);
            p.text("H - Healing spell", 80, 220);
            p.text("P - Use potion", 80, 250);
            p.text("Select an action...", 50, 280);
        }


        p.textAlign(PApplet.CENTER);
        p.text(message, p.width/2, 450);
        p.textAlign(PApplet.LEFT);
        // Draw MP and HP
        p.text("Your HP: "+player.getHealth()+"/"+player.getMAX_HEALTH(), 450, 100);
        p.text("Your MP: "+player.getMagic()+"/"+player.getMAX_MAGIC(), 670, 100);
        p.text("Enemy HP: "+monster.getHealth()+"/"+monster.getMAX_HEALTH(), 450, 200);

        p.stroke(0);
        p.fill(123);
        p.rect(450, 120, 206, 26); // Health bar
        p.rect(670, 120, 206, 26); // Magic bar
        p.rect(450, 220, 206, 26); // Enemy health bar

        float hpercent = (player.getHealth()+0.0f) / (player.getMAX_HEALTH()+0.0f);
        int bp = (int) (200 * hpercent);
        p.fill(278, 32, 64);
        p.rect(453, 123, bp, 20);

        // Enemy health bar
        hpercent = (monster.getHealth()+0.0f) / (monster.getMAX_HEALTH()+0.0f);
        bp = (int) (200 * hpercent);
        p.rect(453, 223, bp, 20);

        // Magic bar
        p.fill(142,210,105);
        hpercent = (player.getMagic()+0.0f) / (player.getMAX_MAGIC()+0.0f);
        bp = (int) (200 * hpercent);
        p.rect(673, 123, bp, 20);

        p.image(player.img, 100, 400, 100, 100);
        p.image(monster.img, p.width-100-64, 400, 100, 100);

    }

    public int getBonusExp() {
        return monster.getStrength();
    }

    public int getBonusGold() {
        return monster.getStrength()+monster.getDefence();
    }
}
