package dungeon.dungeon.elements;

/**
 * Created by Louis on 29/10/2016.
 */
public class Item {

    private String name;
    private String stats;
    private Attribute attr;

    public Item(String name, String stats, Attribute attr) {
        this.name = name;
        this.stats = stats;
        this.attr = attr;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStats() { return stats; }

    public void setStats(String stats) { this.stats = stats; }

    public Attribute getAttr() { return attr; }

    public void setAttr(Attribute attr) { this.attr = attr; }
}
