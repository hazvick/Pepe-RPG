import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends GameObject{

    private BufferedImage slime;

    public Crate(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        slime = ss.grabImage(6, 2, 32 ,32);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(slime, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
