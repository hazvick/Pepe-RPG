import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
Using KeyInputs to reduce lag and stutter.
 */

public class KeyInput extends KeyAdapter {

    Handler handler; //don't create a new handler, we want to access the List Object we've already created at Handler

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) { //loops through all objects until it finds player
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Player) { //player actions definitions
                if(key == KeyEvent.VK_W) handler.setUp(true);
                if(key == KeyEvent.VK_A) handler.setLeft(true);
                if(key == KeyEvent.VK_S) handler.setDown(true);
                if(key == KeyEvent.VK_D) handler.setRight(true);

            }
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) { //loops through all objects until it finds player
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Player) { //player actions definitions
                if(key == KeyEvent.VK_W) handler.setUp(false);
                if(key == KeyEvent.VK_A) handler.setLeft(false);
                if(key == KeyEvent.VK_S) handler.setDown(false);
                if(key == KeyEvent.VK_D) handler.setRight(false);

            }
        }
    }
}
