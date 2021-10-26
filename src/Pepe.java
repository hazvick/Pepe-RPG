import java.awt.*;
import java.awt.image.BufferedImage;

public class Pepe extends GameObject {

    Handler handler;
    Game game;

    private BufferedImage pepe_sprite;

    public Pepe(int x, int y, ID id, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, id, ss);
        this.handler = handler;
        this.game = game;

        pepe_sprite = ss.grabImage(1, 1, 32, 48);                                                  //grab sprite from sprite sheet
    }

    public void tick() {
        x += velX;
        y += velY;

        collision();

        //movement
        if (handler.isUp()) velY = -5;                                                                                  //Movement
        else if (!handler.isDown()) velY = 0;                                                                           //minimizes lag, setting velY to 0
                                                                                                                        //if down is not being pressed
        if (handler.isDown()) velY = 5;
        else if (!handler.isUp()) velY = 0;

        if (handler.isRight()) velX = 5;
        else if (!handler.isLeft()) velX = 0;

        if (handler.isLeft()) velX = -5;
        else if (!handler.isRight()) velX = 0;


    }

    private void collision() {
        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.Block) {
                if(getBounds().intersects(tempObject.getBounds())) {
                    x += velX * -1;
                    y += velY * -1;
                }
            }

            if(tempObject.getId() == ID.Crate) {
                if(getBounds().intersects(tempObject.getBounds())) {
                    game.ammo += 20;
                    handler.removeObject(tempObject);
                }
            }

            if(tempObject.getId() == ID.Enemy) {
                if(getBounds().intersects(tempObject.getBounds())) {
                    game.hp--;
                    if (game.hp <= 0) {
                        game.hp = 0;
                    }
                }
            }

            if(tempObject.getId() == ID.Item) {
                if(getBounds().intersects(tempObject.getBounds())) {
                    game.damage += 20;
                    handler.removeObject(tempObject);
                }
            }


        }
    }

    public void render(Graphics g) {
        g.drawImage(pepe_sprite, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 48);
    }
}
