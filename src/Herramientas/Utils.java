package Herramientas;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BASELINE;

public class Utils {

    private static final int GROSOR = 2;
    private static final int TEXT_SIZE = 18;

    public static float range(float p, float min, float max) {
        if (p<min) {
            p = min;
        }
        if (p>max) {
            p = max;
        }
        return p;
    }

    public static boolean circleRectColission(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {
        float testX = cx;
        float testY = cy;

        if (cx < rx)
        {
            testX = rx;
        }
        if (cx > rx+rw){
            testX = rx+rw;
        }
        if (cy < ry)  {
            testY = ry;
        }
        if (cy > ry+rh) {
            testY = ry+rh;
        }

        float distX = cx-testX;
        float distY = cy-testY;
        float distance = PApplet.sqrt( (distX*distX) + (distY*distY) );

        if (distance <= radius) {
            return true;
        }
        return false;
    }

    public static PVector calcularAccColisionado(float cx, float cy, float rx, float ry, float rw, float rh, PVector direccion, PVector posicionAnterior, PVector posicion){
        PVector nuevaDireccion = new PVector(posicionAnterior.x, posicionAnterior.y);
        /*
        * SIEMPRE EN COLLISION TRUE
        * TOCA BORDE BOTTOM -> PLAYER  DIRECCION: ARRIBA + IZQ O ARRIBA + DERCH
        * TOCA BORDE ARRIBA -> PLAYER DIRECCION: ABAJO + IZQ O ABAJO + DERECH
        * TOCA BORDE DERECHA -> PLAYER DIRECCION: IZQ + ARRIBA O IZQ + ABAJO
        * TOCA BORDE IZQ -> PLAYER DIRECCION: DERCH + ARRIBA O DERECH + ABAJO
         */

        //TODO PROBLEMAS DOBLE RECTANGULOS ENCIMA DE OTRO.

        if(cx < rx || cx > rx+rw){
            if(direccion.y == -1 && !(cy > ry+rh) ){
                nuevaDireccion = new PVector(posicionAnterior.x, posicion.y);
            }else if(direccion.y == 1 && !(cy < ry)){
                nuevaDireccion = new PVector(posicionAnterior.x, posicion.y);
            }
        }

        if(cy < ry || cy > ry+rh){
            if(direccion.x == -1 && !(cx > rx+rw)){
                nuevaDireccion = new PVector(posicion.x, posicionAnterior.y);
            } else if(direccion.x == 1 && !(cx < rx)){
                nuevaDireccion = new PVector(posicion.x, posicionAnterior.y);
            }
        }

        return nuevaDireccion;
    }

    public static void debugAreaCirculo(float rad, PGraphics graphics, PVector posicion) {
        graphics.pushMatrix();
        graphics.stroke(Constante.BLANCO.getRGB());
        graphics.strokeWeight(GROSOR);
        graphics.noFill();
        graphics.translate(posicion.x, posicion.y);
        graphics.ellipse(0, 0, rad, rad);
        graphics.ellipse(0, 0, 1, 1);
        graphics.popMatrix();
    }

    public static void debugAreaRectangulo(final float ancho, final float alto, PGraphics graphics, PVector posicion) {
        graphics.pushMatrix();
        graphics.stroke(Constante.ROJO.getRGB());
        graphics.strokeWeight(GROSOR);
        graphics.noFill();
        graphics.translate(posicion.x, posicion.y);
        graphics.rect(0, 0, ancho, alto);
        graphics.popMatrix();
    }

    public static void debugAreaCirculoMovil(float rad, PGraphics graphics){
        graphics.stroke(Constante.BLANCO.getRGB());
        graphics.strokeWeight(GROSOR);
        graphics.noFill();
        graphics.ellipse(0, 0, rad, rad);
    }

    public static void showFPS(boolean isShow, PGraphics graphics, PApplet pApplet) {
        if (isShow) {
            graphics.fill(Constante.ROJO.getRGB());
            graphics.textAlign(BASELINE);
            graphics.textSize(TEXT_SIZE);
            graphics.text("FPS: " + (int)pApplet.frameRate, 20, 20);
        }
    }

    public static void debugTiles(PGraphics graphics, PVector anchorDist, ArrayList<PImage> images, int widthPantalla){
        int x = 0;
        int y = 0;
        for(PImage image : images){
            graphics.image(image, x+anchorDist.x, y+anchorDist.y);
            x += Constante.RESCALADO_SPRITE_WIDTH;
            if(x >= widthPantalla){
                x = 0;
                y += Constante.RESCALADO_SPRITE_HEIGHT;
            }
        }
    }

    public static void debugValue(String typeValue, int value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugValue(String typeValue, long value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugValue(String typeValue, boolean value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugValue(String typeValue, float value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugValue(String typeValue, String value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugValue(String typeValue, float value, float posX, float posY, PGraphics graphics, int textSize) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(textSize);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugVector(String typeValue, PVector value, int posX, int posY, PGraphics graphics) {
        graphics.fill(Constante.BLANCO.getRGB());
        graphics.textSize(TEXT_SIZE);
        graphics.text(typeValue +" " + value, posX, posY);
    }

    public static void debugMemory(boolean onDebug, int posX, int posY, PGraphics graphics){
        if(onDebug){
            long dataSize = 1024L * 1024L;
            long memoryUsed;
            long totalMemory;

            Runtime runtime = Runtime.getRuntime();

            totalMemory = runtime.totalMemory();
            memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) / dataSize;
            totalMemory = totalMemory / dataSize;

            debugValue("Memory used (MB): " + memoryUsed + " Total (MB):", totalMemory, posX, posY, graphics);
        }
    }
}
