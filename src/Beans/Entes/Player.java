package Beans.Entes;

import Beans.comunes.Espada;
import Herramientas.Constante;
import Herramientas.Global;
import Herramientas.Utils;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.SourceTree;
import processing.core.*;

import java.awt.*;
import java.awt.print.Paper;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    final float MAX_SPEED = 1 * Constante.RESCALADO;
    final int MAX_HEALTH = 1;
    private final int ARRIBA = 8;
    private final int ABAJO = 0;
    private final int DERECHA = 4;
    private final int IZQUIERDA = 12;
    private  final int MAX_FPS_ANIMACION = 10;
    private final int SEPARACION = Constante.RESCALADO_SPRITE_WIDTH / 3;

    private PVector posicion, speed, acc, anchor;
    private float radio;
    private int health;
    private int mapaX;
    private int mapaY;
    private ArrayList<PImage> sprites;
    private PImage spriteActual;
    private boolean isMoving;
    private int direccionMovimiento;
    private int fpsAnimacionActual;
    private int posicionSpriteAnimado;
    private boolean isCollision;
    private boolean colisionDerecha = false;
    private boolean colisionIzquierda = false;
    private boolean colisionAbajo = false;
    private boolean colisionArriba = false;
    private int[][] obstaculosColisionables;

    //TODO WEAPON - Equipamiento ? - multiples armas
    private Espada espada;

    public Player(final int x, final int y) {
        posicion = new PVector(x, y);
        speed = new PVector(0, 0);
        acc = new PVector(0, 0);
        health = MAX_HEALTH;
        this.radio = 16f*Constante.RESCALADO;
        loadSprites();
        spriteActual = sprites.get(0);
        espada = new Espada();
    }

    //TODO CODIGO REPETIBLE EN MULTIPLES ENTIDADES
    private void loadSprites(){
        sprites = new ArrayList<>();
        int x = 0;
        int y = 0;
        int filas = 4;
        int columnas = 4;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                PImage image = Global.spritesPlayer.get(x, y, Constante.SPRITE_WIDTH, Constante.SPRITE_HEIGHT*2);
                image.resize(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT*2);
                sprites.add(image);
                x += Constante.SPRITE_WIDTH;
            }
            y += Constante.SPRITE_HEIGHT*2;
            x = 0;
        }
    }

    public void update() {
        direction();
        updateArma();
        move();
    }

    public void paint(PGraphics graphics) {
        cambiarAnimacion();
        animationMove();
        graphics.pushMatrix();
        graphics.translate(anchor.x, anchor.y);
        body(graphics);
        espada.paint(graphics);//TODO REVISAR ORDEN PAINT (CARA ARRIBA ABAJO)
        graphics.popMatrix();
        debug(graphics, anchor);
    }

    private void debug(PGraphics graphics, PVector anchor){
        if(Constante.MODE_DEBUG){
            Utils.debugVector("ACC: ", this.acc, 20, 60, graphics);
            Utils.debugVector("SPEED: ", this.speed, 20, 80, graphics);
            Utils.debugVector("POSICION: ", this.posicion, 20, 110, graphics);
            Utils.debugValue("Sprite Animado PLAYER: ", this.posicionSpriteAnimado, 20, 130, graphics);
            Utils.debugValue("Direccion Sprite PLAYER: ", this.direccionMovimiento, 20, 150, graphics);
            Utils.debugAreaCirculo(radio, graphics, anchor);
            Utils.debugValue("isCollision: ", isCollision, 20, 170, graphics);
            String ladoCol = "NINGUNO";
            if(isCollision){
                ladoCol = "";
                if(colisionIzquierda) ladoCol = "IZQUIERDA ";
                if(colisionDerecha) ladoCol += "DERECHA ";
                if(colisionAbajo) ladoCol += "ABAJO ";
                if(colisionArriba) ladoCol += "ARRIBA";
            }

            Utils.debugValue("LADOS COLISION: ", ladoCol, 20, 190, graphics);
            Utils.debugValue("POSICION MATRIZ X: ", (this.posicion.x / Constante.RESCALADO_SPRITE_WIDTH), 20, 210, graphics);
            Utils.debugValue("POSICION MATRIZ Y: ", (this.posicion.y / Constante.RESCALADO_SPRITE_HEIGHT), 20, 230, graphics);
            espada.debug(graphics);
        }
    }

    private void direction() {
        PVector direction = new PVector(0, 0);

        if (Constante.KEYBOARD.up) {
            if(!colisionArriba)direction.add(new PVector(0, -1));
            direccionMovimiento = ARRIBA;
        }
        if (Constante.KEYBOARD.down) {
            if(!colisionAbajo)direction.add(new PVector(0, 1));
            direccionMovimiento = ABAJO;
        }
        if (Constante.KEYBOARD.left) {
            if(!colisionIzquierda)direction.add(new PVector(-1, 0));
            direccionMovimiento = IZQUIERDA;
        }
        if (Constante.KEYBOARD.right) {
            if(!colisionDerecha)direction.add(new PVector(1, 0));
            direccionMovimiento = DERECHA;
        }

        if(direction.x != 0 || direction.y != 0) isMoving = true;
        else isMoving = false;

        this.acc = direction;
    }

    private void updateArma(){
            espada.setAnchor(anchor);
            espada.setDireccionMovimiento(direccionMovimiento);
            espada.update();
    }

    private void move() {
        PVector posicionAnterior;

        if (this.acc.x == 0 && this.acc.y == 0) {
            speed.mult(0f);
        } else {
            speed.limit(MAX_SPEED);
        }
        posicionAnterior = new PVector(posicion.x, posicion.y);
        speed.add(acc);
        posicion.add(speed);
        colisionesMapa(posicionAnterior);
    }

    //TODO POSIBLE METODO GENERICO MULTIPLES ENTIDADES ?
    private void cambiarAnimacion(){
        fpsAnimacionActual++;
        if(fpsAnimacionActual >= MAX_FPS_ANIMACION){
            fpsAnimacionActual = 0;
            if(posicionSpriteAnimado == 3){
                posicionSpriteAnimado = 0;
            }else{
                posicionSpriteAnimado++;
            }
        }
    }

    private void animationMove(){
        if(!isMoving){
           posicionSpriteAnimado = 0;
           fpsAnimacionActual = 0;
       }

       int sprite = posicionSpriteAnimado + direccionMovimiento;
       spriteActual = sprites.get(sprite);
    }

    private void body(PGraphics graphics) {
        graphics.image(spriteActual, 0, 0);
    }

    private void colisionesMapa(final PVector posicionAnterior){
        isCollision = false;
        validarColisionLimiteArriba();
        validarColisionLimiteAbajo();
        validarColisionLimiteDerecha();
        validarColisionLimiteIzquierda();
        corregirColisionesDiagonales(posicionAnterior);
    }

    private void validarColisionLimiteArriba() {
        int posicionMatrizIzquierdaX = PApplet.round((posicion.x - SEPARACION) / Constante.RESCALADO_SPRITE_WIDTH);
        int posicionMatrizDerechaX = PApplet.round((posicion.x + SEPARACION) / Constante.RESCALADO_SPRITE_WIDTH);
        int posicionMatrizY = PApplet.round((posicion.y - Constante.SPRITE_HEIGHT) / Constante.RESCALADO_SPRITE_HEIGHT);

        if (obstaculosColisionables[posicionMatrizY][posicionMatrizIzquierdaX] != 0 || obstaculosColisionables[posicionMatrizY][posicionMatrizDerechaX] != 0) {
            isCollision = true;
            colisionArriba = true;
        } else {
            colisionArriba = false;
        }
    }

    private void validarColisionLimiteAbajo() {
        int posicionMatrizIzquierdaX = PApplet.round((posicion.x - SEPARACION) / Constante.RESCALADO_SPRITE_WIDTH);
        int posicionMatrizDerechaX = PApplet.round((posicion.x + SEPARACION) / Constante.RESCALADO_SPRITE_WIDTH);
        int posicionMatrizY = PApplet.round((posicion.y + Constante.SPRITE_HEIGHT) / Constante.RESCALADO_SPRITE_HEIGHT);

        if (obstaculosColisionables[posicionMatrizY][posicionMatrizIzquierdaX] != 0 || obstaculosColisionables[posicionMatrizY][posicionMatrizDerechaX] != 0) {
            colisionAbajo = true;
            isCollision = true;
        } else {
            colisionAbajo = false;
        }
    }

    private void validarColisionLimiteDerecha(){
        int posicionMatrizArribaY = PApplet.round((posicion.y - SEPARACION) / Constante.RESCALADO_SPRITE_HEIGHT);
        int posicionMatrizAbajoY = PApplet.round((posicion.y + SEPARACION) / Constante.RESCALADO_SPRITE_HEIGHT);
        int posicionMatrizX = PApplet.round((posicion.x + Constante.SPRITE_HEIGHT) / Constante.RESCALADO_SPRITE_WIDTH);

        if(obstaculosColisionables[posicionMatrizArribaY][posicionMatrizX] != 0 || obstaculosColisionables[posicionMatrizAbajoY][posicionMatrizX] != 0){
            colisionDerecha = true;
            isCollision = true;
        }else{
            colisionDerecha = false;
        }
    }

    private void validarColisionLimiteIzquierda(){
        int posicionMatrizArribaY = PApplet.round((posicion.y - SEPARACION) / Constante.RESCALADO_SPRITE_HEIGHT);
        int posicionMatrizAbajoY = PApplet.round((posicion.y + SEPARACION) / Constante.RESCALADO_SPRITE_HEIGHT);
        int posicionMatrizX = PApplet.round((posicion.x - Constante.SPRITE_HEIGHT) / Constante.RESCALADO_SPRITE_WIDTH);

        if(obstaculosColisionables[posicionMatrizArribaY][posicionMatrizX] != 0 || obstaculosColisionables[posicionMatrizAbajoY][posicionMatrizX] != 0){
            colisionIzquierda = true;
            isCollision = true;
        }else{
            colisionIzquierda = false;
        }
    }

    private void corregirColisionesDiagonales(final PVector posicionAnterior){
        if(colisionDerecha && colisionIzquierda || colisionArriba && colisionAbajo) posicion = posicionAnterior;
    }

    private void collision() {
        float x = Utils.range(this.posicion.x, radio / 2, mapaX - radio / 2);
        float y = Utils.range(this.posicion.y, radio / 2, mapaY - radio / 2);

        this.posicion = new PVector(x, y);
    }

   /* private boolean validarCollisionObstaculos(){
        obstaculosCollisionados = new ArrayList<>();
        isCollision = false;
        if(obstaculos != null){
            for(int i = 0; i < obstaculos.length; i++){
                Rectangle rectangle = obstaculos[i];
                if(Utils.circleRectColission(posicion.x, posicion.y, radio / 2, rectangle.x, rectangle.y, rectangle.width, rectangle.height)){
                    isCollision = true;
                    obstaculosCollisionados.add(rectangle);
                }
            }
        }

        return isCollision;
    }*/

    public PVector getPosicion() {
        return posicion;
    }

    public float getRadio() {
        return radio;
    }

    public void setMapaX(int mapaX) {
        this.mapaX = mapaX;
    }

    public void setMapaY(int mapaY) {
        this.mapaY = mapaY;
    }

   /* public void setObstaculos(Rectangle[] obstaculos) {
        this.obstaculos = obstaculos;
    }*/

    public void setAnchor(PVector anchor) {
        this.anchor = anchor;
    }

    public void setObstaculosColisionables(int[][] obstaculosColisionables) {
        this.obstaculosColisionables = obstaculosColisionables;
    }
}
