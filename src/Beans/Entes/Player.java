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
    private final float MAX_SPEED_DASH = 8 * Constante.RESCALADO;
    private final int MAX_TIME_ACTIVE_DASH = 8;
    private final int MAX_COLDOWN_DASH = 120;

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
    public Espada espada;

    private boolean dashActivo;
    private boolean dashColdawn;
    private int timeActiveDash;
    private int timeColdawnDash;

    public Player(final int x, final int y) {
        posicion = new PVector(x, y);
        speed = new PVector(0, 0);
        acc = new PVector(0, 0);
        health = MAX_HEALTH;
        this.radio = 16f*Constante.RESCALADO;
        loadSprites();
        resizeSprites();
        spriteActual = sprites.get(0);
        espada = new Espada();
    }

    private void loadSprites(){
        int filas = 4;
        int columnas = 4;

        sprites = Utils.loadSprites(filas, columnas, Constante.SPRITE_WIDTH, Constante.SPRITE_HEIGHT*2, Global.spritesPlayer);
    }

    private void resizeSprites(){
        sprites = Utils.resizeSprites(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT*2, sprites);
    }

    public void update() {
        direction();
        updateArma();
        move();
        ejecutarDash();
        coldownDash();
    }

    public void paint(PGraphics graphics) {
        cambiarAnimacion();
        animationMove();
        graphics.pushMatrix();
        graphics.translate(anchor.x, anchor.y);
        ordenCapa(graphics);
        graphics.popMatrix();
        debug(graphics, anchor);
    }

    private void ordenCapa(PGraphics graphics){
        if(espada.isCapaInferior()){
            espada.paint(graphics);
            body(graphics);
        }else{
            body(graphics);
            espada.paint(graphics);
        }
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
            espada.debug(graphics, anchor);
        }
    }

    private void direction() {
        PVector direction = new PVector(0, 0);

        if (Constante.KEYBOARD.up) {
            if(!colisionArriba)direction.add(new PVector(0, -1));
        }
        if (Constante.KEYBOARD.down) {
            if(!colisionAbajo)direction.add(new PVector(0, 1));
        }
        if (Constante.KEYBOARD.left) {
            if(!colisionIzquierda)direction.add(new PVector(-1, 0));
        }
        if (Constante.KEYBOARD.right) {
            if(!colisionDerecha)direction.add(new PVector(1, 0));
        }

        directionSprite();

        if(direction.x != 0 || direction.y != 0) isMoving = true;
        else isMoving = false;

        this.acc = direction;
    }

    private void directionSprite(){
        float angleRadius = PApplet.atan2(Global.mouse.y - anchor.y, Global.mouse.x - anchor.x);
        float angle = (180 / PApplet.PI) * angleRadius;
        angle = (angle < 0) ? angle + 360 : angle;

        orientacion(angle);
    }

    private void orientacion(float angle){
        PVector direction = new PVector(0, 0);

        if(angle >= 67.5 && angle < 112.5)
        {
            direction = new PVector(0,1);
        }
        else if (angle >= 112.5 && angle < 157.5)
        {
            direction = new PVector(-1,1);
        }
        else if (angle >= 157.5 && angle < 202.5)
        {
            direction = new PVector(-1, 0);
        }
        else if (angle >= 202.5 && angle < 247.5)
        {
            direction = new PVector(-1, -1);
        }
        else if (angle >= 247.5 && angle < 292.5)
        {
            direction = new PVector(0, -1);
        }
        else if (angle >= 292.5 && angle < 337.5)
        {
            direction = new PVector(1, -1);
        }
        else if (angle >= 337.5 || angle < 22.5)
        {
            direction = new PVector(1, 0);
        }
        else if (angle >= 22.5 && angle < 67.5)
        {
            direction = new PVector(1, 1);
        }

        getOrientation(direction);
    }

    private void getOrientation(PVector movement){
        if (movement.x == 0 && movement.y == 1)
        {
            direccionMovimiento = ABAJO;
        }
        else if (movement.x == 1 && movement.y == 0)
        {
            direccionMovimiento = DERECHA;
        }
        else if (movement.x == 0 && movement.y == -1)
        {
            direccionMovimiento = ARRIBA;
        }
        else if (movement.x == -1 && movement.y == 0)
        {
            direccionMovimiento = IZQUIERDA;
        }
        else if (movement.x == -1 && movement.y == 1)
        {
            direccionMovimiento = ABAJO;
        }
        else if (movement.x == 1 && movement.y == 1)
        {
            direccionMovimiento = ABAJO;
        }
        else if (movement.x == -1 && movement.y == -1)
        {
            direccionMovimiento = ARRIBA;
        }
        else if (movement.x == 1 && movement.y == -1)
        {
            direccionMovimiento = ARRIBA;
        }
    }

    private void updateArma(){
            espada.setDireccionMovimiento(direccionMovimiento);
            espada.update(this.posicion);
    }

    private void move() {
        PVector posicionAnterior;

        if (this.acc.x == 0 && this.acc.y == 0) {
            speed.mult(0f);
        } else {
            if(dashActivo){
                speed.mult(MAX_SPEED_DASH);
                speed.limit(MAX_SPEED_DASH);
            }
            else speed.limit(MAX_SPEED);
        }
        posicionAnterior = new PVector(posicion.x, posicion.y);
        speed.add(acc);
        posicion.add(speed);
        colisionesMapa(posicionAnterior);
    }

    //TODO - METODOS GENERICOS HABILIDAD ?
    private void ejecutarDash(){
        if(dashActivo)contadorDash();
        if(Constante.KEYBOARD.space && !dashActivo && !dashColdawn){
            dashActivo = true;
        }
    }

    private void coldownDash(){
        if(dashColdawn && MAX_COLDOWN_DASH > timeColdawnDash){
            timeColdawnDash++;
        }else{
            dashColdawn = false;
            timeColdawnDash = 0;
        }
    }

    private void contadorDash(){
        if(MAX_TIME_ACTIVE_DASH > timeActiveDash){
            timeActiveDash++;
        }else{
            dashColdawn = true;
            dashActivo = false;
            timeActiveDash = 0;
        }
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

    public void reducirVida() {
        if ((this.health - 1) <= 0) {
            this.health = 0;
        } else {
            this.health -= 1;
        }
        if (this.health == 0) {
            //finalScore = this.score;
            Global.over = true;
        }
    }

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
