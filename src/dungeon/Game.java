package dungeon;

import dungeon.display.Camera;
import dungeon.elements.*;
import dungeon.elements.Character;
import dungeon.level.Level;
import dungeon.level.Tile;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

/**
 * Created by Louis on 17/10/2016.
 */
public class Game {

    PApplet p;
    Level level;
    ElementContainer objects;
    Player player;
    Camera camera;
    GameState state;
    Battle b;
    int currentLevel = 1;
    public long timer;
    Item pickup;

    public Game(PApplet p) {
        this.p = p;
        this.objects = new ElementContainer();
        this.camera = new Camera(p);
        this.player = new Player(GameScreen.player, 10, 10, 10, 20, 10);
        this.level = new Level(currentLevel, objects, player);
        //this.map = new MapElement(level);
        System.out.printf("Map size x = %d Map size y = %d \n",level.getMap().length, level.getMap()[1].length);

        // Place the player in the map.
        level.placePlayer();
        //player.position = new PVector(15,15);

        // Add player to objects needing rendered.
        objects.player = player;
        state = GameState.startGame;
    }

    public void tick() {
        switch(state) {
            case levelOver:
                if(System.currentTimeMillis() >= timer) {
                    currentLevel++;
                    objects = new ElementContainer();
                    level = new Level(currentLevel, objects, player);
                    level.placePlayer();
                    player.incrementMaxHealth(10);
                    player.incrementHealth(10);
                    player.incrementStrength(2);
                    player.incrementMaxMagic(5);
                    player.incrementMagic(5);
                    objects.player = player;
                    state = GameState.exploring;
                }
                break;
            case exploring:
                detectCollisions(); // Detect all the collisions.
                objects.integrate(p);
                break;
            case battle:
                b.tick();
                break;
            case pickup:
            case battleWon:
                if(System.currentTimeMillis() >= timer) {
                    state = GameState.exploring;
                }
                break;
            case startGame:
            case gameOver:
            case inventory:
                break;
        }
    }

    public void render() {
        switch (state) {
            case startGame:
                renderStartScreen();
                break;
            case gameOver:
                renderGameOver();
                break;
            case levelOver:
                renderNextLevel();
                break;
            case inventory:
                renderInventory();
                break;
            case battle:
                renderBattle();
                break;
            case battleWon:
                renderBattleWon();
                break;
            case pickup:
            case exploring:
                int roomX = (int) player.position.x / (Helpers.WIDTH/ 2);
                int roomY = (int) player.position.y / (Helpers.HEIGHT/ 2);

                // draw
                p.background(200.0f, 200.0f, 200.0f);
                //camera.setRoom(roomX, roomY);
                //camera.begin();
                level.render(p);
                objects.render(p);
                if (state == GameState.pickup) renderPickupBox();
                //camera.end();
                renderStatsBar();
                break;
        }
    }

    public void keyReleased() {
        if(p.key == PApplet.CODED) {
            switch(p.keyCode) {
                case PApplet.LEFT:
                    player.stopHorizontal();
                    break;
                case PApplet.RIGHT:
                    player.stopHorizontal();
                    break;
                case PApplet.UP:
                    player.stopVertical();
                    break;
                case PApplet.DOWN:
                    player.stopVertical();
                    break;
            }
        }
    }

    public void keyPressed() {
        if(p.key == PApplet.CODED) {
            if(state == GameState.exploring) {
                switch (p.keyCode) {
                    case PApplet.LEFT:
                        player.moveLeft();
                        break;
                    case PApplet.RIGHT:
                        player.moveRight();
                        break;
                    case PApplet.UP:
                        player.moveUp();
                        break;
                    case PApplet.DOWN:
                        player.moveDown();
                        break;
                }
            }
        }
        else {
            switch (p.key) {
                case '1':
                    if(state != GameState.startGame) {
                        objects = new ElementContainer();
                        level = new Level(1, objects, player);
                        level.placePlayer();
                        objects.player = player;
                        break;
                    }
                case PApplet.ENTER:
                case PApplet.RETURN:
                    if(state == GameState.startGame) state = GameState.exploring;
                    break;
                case 'i':
                    if(state == GameState.inventory) {state = GameState.exploring; break;}
                    if(state == GameState.exploring) {state = GameState.inventory; break;}
                case 'a':
                    if(state == GameState.inventory) player.usePotion();
                case 'h':
                case 'p':
                case 'm':
                case 'd':
                    if(b != null && state == GameState.battle) b.doBattle(p.key);
                    break;
            }
        }
    }

    public void detectCollisions() {
        Tile[][] map = level.getMap();
        int w = map.length;
        int h = map[1].length;

        /// Handle the player collision with walls first.
        checkCollisionAgainstTerrain(player, map, w, h, false);

        /// Now monsters and walls / Monster and player.
        for (Monster m : objects.monsters) {
            // Get position in the boolean representation of map.
            boolean seeking = m.getSeek();
            checkCollisionAgainstTerrain(m, map, w, h, seeking);
        }

        // Monster on monster - monsters can walk over items, so we will ignore this case.
        for(Monster e : objects.monsters) {
            for (int i = 0; i < objects.monsters.size(); i++) {
                if(objects.monsters.get(i) == e) continue;
                Monster t = objects.monsters.get(i);
                if(t.position.x < e.position.x+e.width && t.position.x + t.width > e.position.x &&
                        t.position.y < e.position.y + e.width &&
                        t.width + t.position.y > e.position.y) {
                    e.orientation += PApplet.PI/4;
                    t.orientation += PApplet.PI/4;
                }
            }
        }

        // Monster and player
        for (Monster m : objects.monsters) {
            if(player.position.x < m.position.x+m.width && player.position.x + player.width > m.position.x &&
                    player.position.y < m.position.y + m.width &&
                    player.width + player.position.y > m.position.y) {
                state = GameState.battle;
                b = new Battle(this, player, m);
                return;
            }
            PVector pd = new PVector(player.position.x - m.position.x, player.position.y - m.position.y);
            float angle = PApplet.atan2(pd.y, pd.x);

            if( m.orientation - PApplet.PI/4 <= angle && angle < m.orientation + PApplet.PI/4 ) {
                if(pd.mag() < 4 * Helpers.TILE) m.setSeek();
            }
            if(pd.mag() > 6 * Helpers.TILE) m.setWander();
        }

        pickup = null;
        for (Item i : objects.items) {
            if(player.position.x < i.position.x+i.width && player.position.x + player.width > i.position.x &&
                    player.position.y < i.position.y + i.width &&
                    player.width + player.position.y > i.position.y) {
                state = GameState.pickup;
                timer = System.currentTimeMillis()+ 2000;
                pickup = i;
            }
        }
        if(pickup != null) {
            // Increase player attribute by pickup amount
            switch (pickup.getAttr()) {
                case STRENGTH:
                    player.incrementStrength(pickup.getBonus());
                    player.sword = pickup;
                    break;
                case DEFENCE:
                    player.incrementDefence(pickup.getBonus());
                    player.shield = pickup;
                    break;
                case GOLD:
                    player.incrementGold(pickup.getBonus());
                    break;
                case DEXTERITY:
                    player.incrementDex(pickup.getBonus());
                    player.boots = pickup;
                    break;
                case MAGIC:
                    player.giveWand();
                    player.magicwand = pickup;
                    player.incrementMaxMagic(pickup.getBonus());
                    player.incrementMagic(pickup.getBonus());
                    break;
                case HEALTH:
                    player.potions.add(pickup);
                    break;
            }
            objects.items.remove(pickup);
        }

        // Player and exit node
        PVector dist = new PVector(player.position.x - objects.exit.position.x, player.position.y - objects.exit.position.y);

        if(dist.mag() <= player.width) {
            state = GameState.levelOver;
            timer = System.currentTimeMillis() + 3000; // Display level over for 3 seconds.
        }
    }

    private void checkCollisionAgainstTerrain(Character c, Tile[][] map, int w, int h, boolean seeking) {
        int eX = (int)Math.floor(((c.position.x + c.velocity.x ))/ Helpers.TILE);
        int eY = (int)Math.floor(((c.position.y + c.velocity.y ))/ Helpers.TILE);
        int neX = (int)Math.floor(((c.position.x + c.velocity.x + c.width))/ Helpers.TILE);
        int neY = (int)Math.floor(((c.position.y + c.velocity.y + c.width))/ Helpers.TILE);

        if(eX >= 0 && eY >= 0 && neX >= 0 && neY >= 0
                && eX < w && eY < h && neX < w && neY < h
                && (!map[eX][eY].walkable || !map[neX][neY].walkable || !map[eX][neY].walkable || !map[neX][eY].walkable)) {
            // Work out direction to get back to safety
            if(c.getClass() == Monster.class && !seeking) {
                c.orientation += PApplet.PI/4;
            }
            else {
                c.velocity.x = 0;
                c.velocity.y = 0;
            }

        }
    }

    private void renderStatsBar() {
        int barY = p.height - GameScreen.PANEL_HEIGHT; // Top of the bar
        barY += 30; // text height on bar.
        p.fill(0);
        p.textSize(12);
        p.textAlign(PApplet.LEFT);
        p.text("Level: "+level.getLevelNumber(), 20, barY);
        p.text("Gold: "+player.getGold(), 100, barY);
        p.text("Exp:"+player.getExp(), 180, barY);
        p.text("Inventory Items: "+player.inventorySize(), 260, barY);
        p.text("HP: "+player.getHealth(), 420, barY);
        p.text("Dexterity:"+player.getDex(), 500, barY);
        p.text("Strength: "+player.getStrength(), 610, barY);
        p.text("Defence: "+player.getDefence(), 720, barY );
    }

    private void renderBattleWon() {
        p.background(51);
        p.fill(255);
        p.textSize(30);
        int yp = 200;
        p.textAlign(PApplet.CENTER);
        p.text("Battle Won!", p.width/2, yp);
        p.textSize(20);
        p.text("+"+b.getBonusExp()+" Exp", p.width/2, yp + 70);
        p.text("+"+b.getBonusGold()+" Gold", p.width/2, yp + 140);
    }

    private void renderBattle() {
        b.render(p);
    }

    private void renderInventory() {
        p.background(51);
        p.fill(255);
        p.textSize(30);
        p.textAlign(PApplet.LEFT, PApplet.TOP);
        p.text("WEAPONS", 50, 60);

        List<Item> inv = player.getInventory();
        int yp = 100;
        int xp = 50;
        for (Item i : inv) {
            p.textSize(20);
            p.text(i.getName(), xp, yp);
            yp+=30;
            p.textSize(12);
            p.text(i.getStats(), xp, yp);
            yp += 20;
            p.text(i.getBonusString()+" bonus: "+i.getBonus(), xp, yp);
            yp+=45;
        }

        xp = 350;
        p.textSize(30);
        p.text("POTIONS", xp, 60);
        p.textSize(20);
        yp = 100;
        if(player.potions.size() > 0) {
            p.textSize(12);
            p.text("Press A to use potion", xp, yp);
            yp+= 30;
            p.text("Gain " + player.potions.get(0).getBonus() + " health", xp, yp);
            yp+=30;
            p.text(player.potions.size() + " left", xp, yp);
        }

        p.fill(255);
        p.textSize(15);
        xp = p.width-280;
        p.text("HP: "+player.getHealth()+"/"+ player.getMAX_HEALTH(), xp, 90);
        p.text("MP: "+player.getMagic()+"/"+player.getMAX_MAGIC(), xp, 180);

        p.stroke(0);
        p.fill(123);
        p.rect(xp, 120, 206, 26); // Health bar
        p.rect(xp, 200, 206, 26); // Magic bar

        p.image(player.img, xp, 400, 100, 100);


        float hpercent = (player.getHealth()+0.0f) / (player.getMAX_HEALTH()+0.0f);
        int bp = (int) (200 * hpercent);
        p.fill(278, 32, 64);
        p.rect(xp+3, 123, bp, 20);

        // Magic bar
        p.fill(142,210,105);
        hpercent = (player.getMagic()+0.0f) / (player.getMAX_MAGIC()+0.0f);
        bp = (int) (200 * hpercent);
        p.rect(xp+3, 203, bp, 20);
    }

    private void renderGameOver() {
        p.background(51);
        p.fill(255);
        p.textSize(40);
        p.textAlign(PApplet.CENTER);
        int yp = 200;
        p.text("Game Over!", p.width/2, yp);
        p.textSize(20);
        p.text("You got to level "+currentLevel, p.width/2, yp+70);
        p.text("Score: "+player.getScore(currentLevel), p.width/2, yp + 140);
    }

    private void renderStartScreen() {
        p.background(123,123,123);
        p.textSize(60);
        p.textAlign(PApplet.CENTER);
        p.fill(0);
        int yp = 200;
        p.text("START GAME", p.width/2, yp);
        p.textSize(40);
        p.text("Press Enter to start", p.width/2, yp+70);
    }

    private void renderNextLevel() {
        p.background(51);
        p.fill(255);
        p.textSize(40);
        int yp = 200;
        p.textAlign(PApplet.CENTER);
        p.text("Level "+level.getLevelNumber()+" complete!", p.width/2, yp);
        p.textSize(20);
        p.text("Score: "+player.getScore(currentLevel), p.width/2, yp + 140);
    }

    private void renderPickupBox() {
        if(pickup != null) {
            p.fill(51);
            p.rect(p.width / 2 - 150, p.height / 2 - 90, 300, 180);
            p.fill(255);
            p.textSize(15);
            p.textAlign(PApplet.CENTER);
            p.text("Picked up " + pickup.getName(), p.width / 2, p.height / 2 - 50);
            p.textSize(12);
            p.text(pickup.getStats(), p.width / 2, p.height / 2 - 10);
            String attr = "";
            switch (pickup.getAttr()) {
                case STRENGTH:
                    attr = "Strength";
                    break;
                case HEALTH:
                    attr = "Health";
                    break;
                case DEFENCE:
                    attr = "Defence";
                    break;
                case GOLD:
                    attr = "Gold";
                    break;
                case DEXTERITY:
                    attr = "Speed";
                    break;
                case MAGIC:
                    attr = "Magic";
                    break;
            }
            p.text("Boost " + attr + " by " + pickup.getBonus(), p.width / 2, p.height / 2 + 20);
        }
        else {
            timer = System.currentTimeMillis();
        }
    }

}
