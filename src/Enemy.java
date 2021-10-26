import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject{

    private Handler handler;
    Random r = new Random();
    int choose = 0;
    int hp = 20;
    Game game;

    private BufferedImage enemy_sprite;

    public Enemy(int x, int y, ID id, Handler handler, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;

        enemy_sprite = ss.grabImage(4, 1, 32, 32);                                                 //grabs sprite from sprite sheet
    }

    public void tick() {
        x += velX;
        y += velY;

        choose = r.nextInt(10);

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Block) {
                if (getBoundsBig().intersects(tempObject.getBounds())) {                                                //makes AI not go trough walls
                    x += (velX*1) * -1;
                    y += (velY*1) * -1;
                    velX *= -1;
                    velY *= -1;
                } else if (choose == 0) {
                    velX = (r.nextInt(4 - -4) + -4);
                    velY = (r.nextInt(4 - -4) + -4);
                }
            }
            if (tempObject.getId() == ID.Spit) {                                                                        //collision with spit, loses health
                if (getBounds().intersects(tempObject.getBounds())){
                    hp -= 20;
                    handler.removeObject(tempObject);
                }
            }
        }

        if (hp <= 0) handler.removeObject(this);                                                              //removes object if health <= 0
    }

    public void render(Graphics g) {
        g.drawImage(enemy_sprite, x, y, null);
    }

    public Rectangle getBounds() {                                                                                      //collision
        return new Rectangle(x, y, 32, 32);
    }

    public Rectangle getBoundsBig() {                                                                                   //larger collision, better when used
        return new Rectangle(x-16, y-16, 64, 64);                                                     //on AI to detect objects earlier
    }                                                                                                                   //thus preventing glitching
}
