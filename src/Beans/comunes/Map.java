package Beans.comunes;

import Gestores.GestorMapeado;
import Herramientas.Constante;
import Herramientas.Global;
import processing.core.*;

import java.awt.*;
import java.util.ArrayList;

public class Map {

    private PVector anchorDist, anchor;
    private int mapaX;
    private int mapaY;
    private float radioPlayer;
    public ArrayList<PImage> sprite = new ArrayList<PImage>();
    private GestorMapeado mapeado;

    public Map() {
        this.mapeado = new GestorMapeado();
        mapaX = this.mapeado.getWidth();
        mapaY = this.mapeado.getHeight();
        loadTiles();
        //findAnchor(posicionPlayer);
    }

    public void update(PVector posicion) {
        findAnchor(posicion);
    }

    public void paint(PGraphics graphics) {
        //bordes(graphics);
        //graphics.imageMode(PConstants.CENTER);
        mapeado.cargarTerreno(graphics, sprite, anchorDist);
        //mapeado.debugColisionesTerreno(graphics, anchorDist);
        mapeado.debugObstaculosTerreno(graphics, anchorDist);
    }

    private void bordes(PGraphics graphics) {
        graphics.stroke(255);
        graphics.strokeWeight(1f);
        //LINES: PUNTO A -> B.
        graphics.line(0 + anchorDist.x, 0 + anchorDist.y, mapaX + anchorDist.x, 0 + anchorDist.y);//ARRIBA
        graphics.line(0 + anchorDist.x, 0 + anchorDist.y, 0 + anchorDist.x, mapaY + anchorDist.y);//IZQ
        graphics.line(0 + anchorDist.x, mapaY + anchorDist.y, mapaX + anchorDist.x, mapaY + anchorDist.y);//ABAJO
        graphics.line(mapaX + anchorDist.x, 0 + anchorDist.y, mapaX + anchorDist.x, mapaY + anchorDist.y);//DERECHA
    }

    //TODO POSIBLE METODO REPETIBLE
    private void loadTiles() {
        int x = 0;
        int y = 0;
        int filas = calcularFilas();
        int columnas = calcularColumnas();

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                PImage image = Global.spriteSheet.get(x, y, Constante.SPRITE_WIDTH, Constante.SPRITE_HEIGHT);
                image.resize(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT);
                sprite.add(image);
                x += Constante.SPRITE_WIDTH;
            }
            y += Constante.SPRITE_HEIGHT;
            x = 0;
        }
    }

    private int calcularFilas() {
        return Global.spriteSheet.height / Constante.SPRITE_HEIGHT;
    }

    private int calcularColumnas() {
        return Global.spriteSheet.width / Constante.SPRITE_WIDTH;
    }

    private void findAnchor(PVector posicionPlayer) {
        float ax = PApplet.map(posicionPlayer.x, radioPlayer / 2, mapaX - radioPlayer / 2, 0.6f * 0.5f * Global.width, Global.width * (1 - 0.6f * 0.5f));
        float ay = PApplet.map(posicionPlayer.y, radioPlayer / 2, mapaY - radioPlayer / 2, 0.6f * 0.5f * Global.height, Global.height * (1 - 0.6f * 0.5f));
        anchor = new PVector(ax, ay);
        anchorDist = new PVector(ax - posicionPlayer.x, ay - posicionPlayer.y);
    }

    public PVector getAnchor() {
        return anchor;
    }

    public PVector getAnchorDist() {
        return anchorDist;
    }

    public int getMapaX() {
        return mapaX;
    }

    public int getMapaY() {
        return mapaY;
    }

    /*public Rectangle[] getObstaculosMapa(){
        return mapeado.getCapaColisiones();
    }*/

    public void setRadioPlayer(float radioPlayer) {
        this.radioPlayer = radioPlayer;
    }

    public int getMapaCentroX(){
        return mapaX/2;
    }

    public int getMapaCentroY(){
        return mapaY/2;
    }

    public int[][] getObstaculosColisionablesMapa(){
        return mapeado.getCapaCollisions();
    }

    public int getFilasMapa(){
        return this.mapeado.getPatronWidth();
    }

    public int getColumnasMapa(){
        return this.mapeado.getPatronHeight();
    }
}
