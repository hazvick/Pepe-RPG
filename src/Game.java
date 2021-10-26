
/*-----------------------------------------------------------------//
//                                                                 //
//         Assignment in Advanced Java Development                 //
//                                                                 //
//                 @author Mathias Koerth                          //
//                                                                 //
//-----------------------------------------------------------------*/

/**
 * GAME FEATURES
 *  - WASD Movement
 *  - Fire on Mouse Coordinates/Clicks
 *  - Enemy "AI" with Movement
 *  - Collision
 *  - Threading/Buffering/Rendering
 *  - Health, Damage and Ammo
 *  - Graphics (skins) as sprite sheet
 *  - Death Screen
 *  - And much more!
 */

/**
 * NEXT UPDATE
 *  - Add Main Menu
 *  - Add Splash Screen
 *  - Add Advanced AI
 *  - Add Boss AI
 *  - Add Music + Sound Effects
 *  - Add Pause Button
 *  - Add Stats and Effects
 *  - Add More Items and Item Buffs
 */

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;                                                                    //autogenerated serialUID to prevent warning

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private SpriteSheet ss;

    private BufferedImage level = null;
    private BufferedImage sprite_sheet = null;
    private BufferedImage floor = null;


    public int ammo = 60;
    public int damage = 10;
    public int hp = 100;


    public Game() {
        new Window(1000, 563, "Pepe Shooter", this);                                             //sets dimensions and title
        start();                                                                                                        //starts game

        handler = new Handler();
        camera = new Camera(0, 0);
        this.addKeyListener(new KeyInput(handler));

        BufferedImageLoader loader = new BufferedImageLoader();                                                         //BufferedImage
        level = loader.loadImage("/finalmap.png");                                                                 //load map
        sprite_sheet = loader.loadImage("/spriteSheet1.png");       //bitmap?                                      //load sprite sheet

        ss = new SpriteSheet(sprite_sheet);

        floor = ss.grabImage(4, 2, 32, 32);                                                        //image grab

        this.addMouseListener(new MouseInput(handler, camera, this, ss));

        loadLevel(level);

    }

    private void start() {
        isRunning = true;
        thread = new Thread(this); //runs the run() method
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try { //can fail, so a try catch is needed
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {                                                                                                 //gameloop inspired by Minecraft
        this.requestFocus();                                                                                            //developer Notch
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    public void tick() {                                                                                                //updates everything in the game

        for (int i = 0; i < handler.object.size(); i++) {
            if(handler.object.get(i).getId() == ID.Player) {
                camera.tick(handler.object.get(i));
            }
        }

        handler.tick();
    }

    public void render() {                                                                                              //renders everything in the game
        BufferStrategy bs = this.getBufferStrategy();                                                                   //preloads frames behind the game winow, so its already loaded
        if(bs == null) {                                                                                                //creates bs with 3 args
            this.createBufferStrategy(3);                                                                     //preloads 3 frames behind game window
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        /////////////////////////////////

        g2d.translate(-camera.getX(), -camera.getY());

        for (int xx = 0; xx < 30*72; xx+=32) {                                                                          //+= 32 because of the width+height of the sprite
            for (int yy = 0; yy < 30*72; yy+=32) {
                g.drawImage(floor, xx, yy, null);
            }
        }

        handler.render(g);

        g2d.translate(camera.getX(), camera.getY());


                                                                                                                        //health bar
        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.green);
        g.fillRect(5, 5, hp*2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);

        g.setFont(new Font("Century", Font.BOLD, 16));

                                                                                                                        //spits
        g.setColor(Color.white);
        g.drawString("Spits left: " + ammo, 8, 52);

                                                                                                                        //damage multiplier
        g.setColor(Color.white);
        g.drawString("Damage: " + damage, 8, 68);

                                                                                                                        //checks if dead
        if (hp <= 0) {
            g.setColor(Color.black);
            g.fillRect(0, 0, 1000, 563);
            g.setFont(new Font("Century", Font.BOLD, 48));
            g.setColor(Color.white);
            g.drawString("DEAD", 425, 270);
        }

        /////////////////////////////////
        g.dispose();
        bs.show();
    }

    //Loading level
    private void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {                                                                                //checks RGB and places block dep. ID
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255) {
                    handler.addObject(new Block(xx*32, yy*32, ID.Block, ss));
                }

                if (blue == 255 && green == 0) {
                    handler.addObject(new Pepe(xx*32, yy*32, ID.Player, handler, this, ss));
                }

                if (green == 255 && blue == 0) {
                    handler.addObject(new Enemy(xx*32, yy*32, ID.Enemy, handler, ss));
                }

                if (green == 255 && blue == 255) {
                    handler.addObject(new Crate(xx*32, yy*32, ID.Crate, ss));
                }

                if (red == 50 && green == 150 && blue == 100) {
                    handler.addObject(new Item(xx*32, yy*32, ID.Item, ss));
                }
            }
        }
    }


    public static void main(String args[]) {
        new Game();
    }

}
