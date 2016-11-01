package dungeon.dungeon.level;

import java.util.List;

/**
 * Created by Ls99
 *
 * Node for Space partition tree.
 */
public class SPNode {
    public int x1, y1, x2, y2, width, height;
    public SPNode parent;

    public SPNode left = null;
    public SPNode right = null;

    private final int MIN_LEAF_SIZE = 8; // 32 tiles wide map, 15 tiles high.
    private Room r = null;


    public SPNode(int x1, int y1, int x2, int y2, SPNode parent) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.width = x2 - x1 + 1;
        this.height = y2 - y1 + 1;
        this.parent = parent;
    }

    public boolean split() {
        // Nothing to be done.
        if (left != null || right != null)
            return false;

        /*
         * Split based on size of room.
         * If wider than long, split vertically, else split horizontally.
         */
        boolean splitH = Math.random() > 0.5;

        if (width > height && (float)(width) / (float)(height) >= 1.25) splitH = false;
        else if (height > width && (float)(height) / (float)(width) >= 1.25) splitH = true;

        int max = (splitH ? height : width) - MIN_LEAF_SIZE; // determine the maximum height or width
        if (max <= MIN_LEAF_SIZE)
            return false; // Leaf node too small to split

        int split = (int) (MIN_LEAF_SIZE + Math.random() * (max-MIN_LEAF_SIZE)); // Pick location.

        // Create children.
        if (splitH) {
            left = new SPNode(x1, y1, x2, y1 + split-1, this);
            right = new SPNode(x1, y1 + split, x2, y2, this);
        }
        else {
            left = new SPNode(x1, y1, x1 + split -1, y2, this);
            right = new SPNode(x1 + split, y1, x2, y2, this);
        }
        return true; // split successful!
    }

    public Room createRoom() {
        int minRoom = MIN_LEAF_SIZE - 2;

        // Leaf node so create randomly sized room. Min size determined by partition size.
        int roomWidth = (int)(minRoom + Math.random() * (width-1-minRoom));
        int roomHeight = (int)(minRoom + Math.random() * (height-1-minRoom));
        int roomX = (int)(x1 + Math.random() * (x2 - x1 - roomWidth -1));
        int roomY = (int)(y1 + Math.random() * (y2 - y1 - roomHeight -1));
        r =  new Room(roomX, roomY, roomWidth, roomHeight);
        return r;
    }

}
