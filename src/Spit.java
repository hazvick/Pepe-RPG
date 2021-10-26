import java.awt.*;

public class Spit extends GameObject {

    private Handler handler;

    public Spit(int x, int y, ID id, Handler handler, int mx, int my, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;

        //speed of the spit                                                                                             //speed
        velX = (mx - x) / 10;
        velY = (my - y) / 10;
    }

    public void tick() {                                                                                                //projectile movement
        x += velX;
        y += velY;

        try {                                                                                                           //BUGGFIX
            for (int i = 0; i < handler.object.size(); i++) {
                GameObject tempObject = handler.object.get(i);

                if (tempObject.getId() == ID.Block) {                                                                   //projectile disappears when colliding
                    if (getBounds().intersects(tempObject.getBounds())) {                                               //with object with ID.Block
                        handler.removeObject(this);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("Error");
        }
    }

    public void render(Graphics g) {                                                                                    //graphics
        g.setColor(Color.green);
        g.fillOval(x, y, 10, 10);
    }

    @Override
    public Rectangle getBounds() {                                                                                      //collision detection
        return new Rectangle(x, y, 8,8);
    }
}
