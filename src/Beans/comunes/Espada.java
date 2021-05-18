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
    private PVector posicionAtq;
    private float radioAtaque = Constante.RESCALADO_SPRITE_WIDTH;
    private int idSpriteActual = 0;
    private int direccionMovimiento;
    private boolean capaInferior;
    public boolean isUsed;

    public Espada() {
        loadSprites();
        resizeSprites();
        spriteActual = sprites.get(0);
    }

    private void loadSprites(){
        int filas = 1;
        int columnas = 4;
        final int WIDTH_SPRITE = Constante.SPRITE_WIDTH * 4;
        final int HEIGHT_SPRITE = Constante.SPRITE_WIDTH * 4;

        sprites = Utils.loadSprites(filas, columnas, WIDTH_SPRITE, HEIGHT_SPRITE, Global.sword);
    }

    private void resizeSprites(){
        final int WIDTH_SPRITE = 24 * Constante.RESCALADO;
        final int HEIGHT_SPRITE = 24 * Constante.RESCALADO;

        sprites = Utils.resizeSprites(WIDTH_SPRITE, HEIGHT_SPRITE, sprites);
    }

    public void update(PVector posicion) {
        calcularRadioAtaque(posicion);
    }

    private void calcularRadioAtaque(PVector posicion){
        if(!Constante.KEYBOARD.activeAtq){
            posicionAtq = new PVector(posicion.x, posicion.y);
            isUsed = false;
            return;
        }
        isUsed = true;
        posicionAtq = new PVector(posicion.x, posicion.y);
        animacionActiva = true;
        calcularDireccion(posicionAtq);
    }

    private void calcularDireccion(PVector posicion){
        switch (direccionMovimiento){
            case ARRIBA:
                posicion.add(new PVector(0, - (Constante.RESCALADO_SPRITE_HEIGHT / 2)));
                break;
            case ABAJO:
                posicion.add(new PVector(0, (Constante.RESCALADO_SPRITE_HEIGHT / 2)));
                break;
            case IZQUIERDA:
                posicion.add(new PVector( - (Constante.RESCALADO_SPRITE_WIDTH / 2), 0));
                break;
            case DERECHA:
                posicion.add(new PVector((Constante.RESCALADO_SPRITE_WIDTH / 2), 0));
                break;
            default:
                break;
        }
    }

    public void paint(PGraphics graphics) {
        if(animacionActiva){
            graphics.pushMatrix();
            calcularPositionAtaque(graphics);
            graphics.image(spriteActual, 8, 8);
            animacionAtaque();
            graphics.popMatrix();
        }
    }

    private void animacionAtaque(){
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
                capaInferior = true;
                graphics.rotate(PApplet.radians(-45f));
                graphics.scale(1.0f, -1.0f);
                break;
            case ABAJO:
                capaInferior = false;
                graphics.rotate(PApplet.radians(90f));
                graphics.scale(1.0f, -1.0f);
                break;
            case IZQUIERDA:
                capaInferior = false;
                graphics.scale(-1.0f, 1.0f);
                break;
            case DERECHA:
                capaInferior = false;
                graphics.scale(1.0f, 1.0f);
                break;
            default:
                break;
        }
    }

    public void debug(PGraphics graphics, PVector anchor){
        if(Constante.MODE_DEBUG) {
            if (isUsed){
                PVector p = new PVector(posicionAtq.x, posicionAtq.y);
                p.add(new PVector((anchor.x - posicionAtq.x), (anchor.y - posicionAtq.y)));

                calcularDireccion(p);

                Utils.debugAreaCirculo(radioAtaque, graphics, p, Constante.ROJO.getRGB());
            }
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

    public void setDireccionMovimiento(int direccionMovimiento) {
        this.direccionMovimiento = direccionMovimiento;
    }

    public boolean isCapaInferior() {
        return capaInferior;
    }

    public float getRadioAtaque() {
        return radioAtaque;
    }
}
