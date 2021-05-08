package Beans.comunes;

import Herramientas.Constante;
import Herramientas.Global;
import Herramientas.Utils;
import processing.core.*;

import java.util.ArrayList;

public class Espada {

    private final int MAX_FRAMES_ANIMATION_BASIC = 5;
    private final int ARRIBA = 8;
    private final int ABAJO = 0;
    private final int DERECHA = 4;
    private final int IZQUIERDA = 12;
    private int contadorFramesAnimationBasic = 0;
    private boolean animacionActiva;
    private float damage;
    private PImage spriteActual;
    private ArrayList<PImage> sprites;
    private PVector posicionAtq, anchor;
    private float radioAtaque = Constante.RESCALADO_SPRITE_WIDTH;
    private int idSpriteActual = 0;
    private int direccionMovimiento;

    public Espada() {
        loadSprites();
        spriteActual = sprites.get(0);
    }

    //TODO CODIGO REPETIDO
    private void loadSprites(){
        sprites = new ArrayList<>();
        int x = 0;
        int y = 0;
        int filas = 1;
        int columnas = 4;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                PImage image = Global.sword.get(x, y, Constante.SPRITE_WIDTH*4, Constante.SPRITE_HEIGHT*4);
                image.resize(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT);
                sprites.add(image);
                x += Constante.SPRITE_WIDTH*4;
            }
            y += Constante.SPRITE_HEIGHT*4;
            x = 0;
        }
    }

    public void update() {
        calcularRadioAtaque();
    }

    private void calcularRadioAtaque(){
        if(!Constante.KEYBOARD.activeAtq){
            posicionAtq = null;
            return;
        }
        posicionAtq = new PVector(anchor.x, anchor.y);
        animacionActiva = true;

        switch (direccionMovimiento){
            case ARRIBA:
                posicionAtq.add(new PVector(0, - (Constante.RESCALADO_SPRITE_HEIGHT / 2)));
                break;
            case ABAJO:
                posicionAtq.add(new PVector(0, (Constante.RESCALADO_SPRITE_HEIGHT / 2)));
                break;
            case IZQUIERDA:
                posicionAtq.add(new PVector( - (Constante.RESCALADO_SPRITE_WIDTH / 2), 0));
                break;
            case DERECHA:
                posicionAtq.add(new PVector((Constante.RESCALADO_SPRITE_WIDTH / 2), 0));
                break;
            default:
                break;
        }
    }

    public void paint(PGraphics graphics) {
        if(animacionActiva){
            calcularPositionAtaque(graphics);
            graphics.image(spriteActual, 8, 8);
            animacionAtaque();
        }
    }

    private void animacionAtaque(){
            //Constante.KEYBOARD.activeAtq = false;
                contadorFramesAnimationBasic++;
                if(contadorFramesAnimationBasic >= MAX_FRAMES_ANIMATION_BASIC){
                    contadorFramesAnimationBasic = 0;
                    if(idSpriteActual == 3){
                        idSpriteActual = 0;
                        animacionActiva = false;
                    }else{
                        idSpriteActual++;
                    }
                }

            spriteActual = sprites.get(idSpriteActual);
    }

    private void calcularPositionAtaque(PGraphics graphics){

        switch (direccionMovimiento){
            case ARRIBA:
                //graphics.scale(-1.0f, -1.0f);
                break;
            case ABAJO:
                graphics.scale(1.0f, 1.0f);
                break;
            case IZQUIERDA:
                graphics.scale(-1.0f, 1.0f);
                break;
            case DERECHA:
                graphics.scale(1.0f, 1.0f);
                break;
            default:
                break;
        }
    }

    public void debug(PGraphics graphics){
        if(Constante.MODE_DEBUG) {
            if (posicionAtq != null)
                Utils.debugAreaCirculo(radioAtaque, graphics, posicionAtq, Constante.ROJO.getRGB());
        }
    }

    public boolean isAnimacionActiva() {
        return animacionActiva;
    }

    public void setAnimacionActiva(boolean animacionActiva) {
        this.animacionActiva = animacionActiva;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public PVector getPosicionAtq() {
        return posicionAtq;
    }

    public void setPosicionAtq(PVector posicionAtq) {
        this.posicionAtq = posicionAtq;
    }

    public PVector getAnchor() {
        return anchor;
    }

    public void setAnchor(PVector anchor) {
        this.anchor = anchor;
    }

    public void setDireccionMovimiento(int direccionMovimiento) {
        this.direccionMovimiento = direccionMovimiento;
    }
}
