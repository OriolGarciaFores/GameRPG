import Herramientas.Constante;
import Gestores.GestorEstados;
import Herramientas.Global;
import Herramientas.Utils;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.event.KeyEvent;

/*
**************************************************
*
*  CONTROL DE VERSIONES TAGS
*  X.0.0 ->     VERSION FINAL O CAMBIO GORDO.
*  0.X.0 -> NUEVAS IMPLEMENTACIONES O MEJORAS
*  0.0.X -> CORECCIONES.
*
**************************************************
*/

public class Main extends PApplet {

    private static final String VERSION = "PRE-ALFA-0.1.0-SNAPSHOT";
    private static final String TITLE_VERSION = "GameRPG version " + VERSION;

    private boolean isFull;
    private GestorEstados gestorEstados;

    private final int NS_POR_SEGUNDO = 1000000000;
    private final int APS_OBJETIVO = 60;
    private final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

    private long referenciaActualizacion;
    private long referenciaContador;
    private double tiempoTranscurrido;
    private double delta = 0;


    public static void main(String[] args) {
        String[] appletArgs = new String[]{"Main"};
        PApplet.main(appletArgs);
    }

    public void settings() {
        montarVentana();
    }

    public void setup() {
        println(TITLE_VERSION + " - STARTED");
        frameRate(Constante.MAX_FPS);
        loadScreen();
        loadSprites();
        gestorEstados = new GestorEstados();
        referenciaActualizacion = System.nanoTime();
        referenciaContador = System.nanoTime();
//test
    }

    public void draw() {
        //if(Constante.MAX_FPS > 60)limitarUpdates();
        //else
        updates();
        background(0);
        gestorEstados.paint(this.g);
        debugMain();
    }

    private void montarVentana() {
        if (isFull) {
            changeSizeScreenFull();
            fullScreen(P2D);
        } else {
            size(Global.width, Global.height, P2D);
        }

        noSmooth();
    }

    private void changeSizeScreenFull() {
        Global.width = displayWidth;
        Global.height = displayHeight;
    }

    private void loadScreen() {
        surface.setTitle(TITLE_VERSION);
        surface.setResizable(false);
        //TODO TEMPORAL - PANTALLA SECUNDARIA
        //surface.setLocation(displayWidth, 0);
    }

    private void loadSprites() {
        Global.spriteSheet = loadImage("Resources/Sprites/Overworld.png");
        Global.spritesPlayer = loadImage("Resources/Sprites/character.png");
        Global.spriteMonster = loadImage("Resources/Sprites/fantasmito.png");
        Global.sword = loadImage("Resources/Sprites/Espada.png");
    }

    private void updates() {
        updateSizeScreen();
        updateMouse();
        gestorEstados.changeStatus();
        gestorEstados.update(g);
    }

    private void updateMouse(){
        Global.mouse = new PVector(mouseX, mouseY);
    }

    private void updateSizeScreen(){
        if(Global.width != width || Global.height != height) {
            Global.width = width;
            Global.height = height;
        }
    }

    private void debugMain(){
        Utils.showFPS(Constante.MODE_DEBUG, this.g, this);
        Utils.debugMemory(Constante.MODE_DEBUG, 20, 40, this.g);
    }

    private void limitarUpdates() {
        final long inicioBucle = System.nanoTime();

        tiempoTranscurrido = inicioBucle - referenciaActualizacion;
        referenciaActualizacion = inicioBucle;
        delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

        while (delta >= 1) {
            updates();
            delta--;
        }

        background(0);
        gestorEstados.paint(this.g);

        if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {
            referenciaContador = System.nanoTime();
        }
    }

    public void keyReleased() {
        Constante.KEYBOARD.keyUp(keyCode);
    }

    public void keyPressed() {
        Constante.KEYBOARD.keyDown(keyCode);
        if(Constante.CIERRE_ESC_DESHABILITADO) deshabilitarCierreEsc();
    }

    private void deshabilitarCierreEsc(){
        if(keyCode == 27){
            key = 0;
        }
    }

    public void mousePressed(){
        if(mouseButton == LEFT){
            Constante.KEYBOARD.keyDown(KeyEvent.VK_1);
        }
    }

    public void mouseReleased(){
        if(mouseButton == LEFT){
            Constante.KEYBOARD.keyUp(KeyEvent.VK_1);
        }
    }
}
