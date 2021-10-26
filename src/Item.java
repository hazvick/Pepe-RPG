import java.awt.*;
import java.awt.image.BufferedImage;

public class Item extends GameObject {

    private BufferedImage item;

    public Item(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        item = ss.grabImage(6, 1, 32 ,32);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(item, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
