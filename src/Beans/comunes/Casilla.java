package Beans.comunes;

public class Casilla {
    private int posicionX;
    private int posicionY;

    //private int isColisionable;
    private int f;
    private int g;
    private int h;
    private Casilla padre;

    public Casilla(Casilla padre, int posicionX, int posicionY, int filas) {
        this.padre = padre;
        this.h = posicionX + (posicionY * filas);
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.f = 0;
        this.g = 0;
    }

    public Casilla(int posicionX, int posicionY){
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Casilla getPadre() {
        return padre;
    }

    public void setPadre(Casilla padre) {
        this.padre = padre;
    }
}
