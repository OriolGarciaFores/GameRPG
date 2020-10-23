package Gestores;

import java.awt.event.KeyEvent;

import static processing.core.PConstants.*;

public class GestorControles {
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;
    public boolean enter = false;
    public boolean space = false;

    public boolean shotUp = false;
    public boolean shotDown = false;
    public boolean shotLeft = false;
    public boolean shotRight = false;
    public boolean activeShield = false;
    public boolean activeMultiShot = false;

    public boolean isRun = false;

    public void keyDown(int p_key){

        switch(p_key){
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_ENTER:
                enter = true;
                break;
            case UP:
                shotUp = true;
                break;
            case DOWN:
                shotDown = true;
                break;
            case LEFT:
                shotLeft = true;
                break;
            case RIGHT:
                shotRight = true;
                break;
            case KeyEvent.VK_SPACE:
                space = true;
                break;
            case KeyEvent.VK_Q:
                activeShield = true;
                break;
            case KeyEvent.VK_E:
                activeMultiShot = true;
                break;
        }
    }

    public void keyUp(int p_key){
        switch(p_key){
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_ENTER:
                enter = false;
                break;
            case UP:
                shotUp = false;
                break;
            case DOWN:
                shotDown = false;
                break;
            case LEFT:
                shotLeft = false;
                break;
            case RIGHT:
                shotRight = false;
                break;
            case KeyEvent.VK_SPACE:
                space = false;
                break;
            case KeyEvent.VK_Q:
                activeShield = false;
                break;
            case KeyEvent.VK_E:
                activeMultiShot = false;
                break;
        }
    }
}
