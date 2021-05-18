package Beans.Entes;

import Gestores.GestorInteligenciaArtificial;
import Herramientas.Constante;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Ente {
    protected int id;
    protected PVector posicion = new PVector(0, 0);
    protected PVector speed = new PVector(0,0);
    protected PVector acc = new PVector(0,0);
    protected PVector target;
    protected PVector anchorDist;
    protected  PVector targetFinal;
    private PVector targetFinalAnt;
    protected float maxSpeed = 0;
    protected float radio = 32f;
    protected float radioVision = 120f;
    protected int maxHealth = 1;
    protected int health = maxHealth;
    protected boolean isEnemy = false;
    protected boolean isDie = false;
    protected boolean isMovil = true;
    protected int[][] tablero;
    protected GestorInteligenciaArtificial gestorInteligenciaArtificial;
    private ArrayList<Point> camino;
    protected int filasMapa;
    protected int columnasMapa;
    private boolean haCambiadoTarget;
    private boolean activeIA = true;

    //TODO TEMPORAL
    private boolean activo = true;

   protected abstract void initIA();

    public void paint(PGraphics graphics){

    }

    abstract void body(PGraphics graphics);

    public void update(){
        if(isMovil) {
            if(gestorInteligenciaArtificial != null && activeIA){
                validarCambioTarget();
                if (haCambiadoTarget) calcularRuta();
                actualizarTarget();
                if(activo){
                    this.gestorInteligenciaArtificial.debugCargaMapaColisionesConCamino();
                    activo = false;
                }
            }
            if(validarTarget()){
                move();
            }
        }
    }

    //TODO EJECUTAR AL MOVERSE EL PLAYER
    private void calcularRuta(){
        this.gestorInteligenciaArtificial.init(this.posicion, this.targetFinal);
        this.gestorInteligenciaArtificial.buscarRuta();
        camino = this.gestorInteligenciaArtificial.getCamino();

       // this.gestorInteligenciaArtificial.debugCargaMapaColisionesConCamino();
        if(camino.size() != 0) obtenerTarget();
    }

    private void actualizarTarget(){
        if((getPosicionRedondeada().x == this.target.x && getPosicionRedondeada().y == this.target.y) && camino.size() != 0){
          obtenerTarget();
        }
    }
//TODO REVISAR
    private void obtenerTarget(){
        Point punto = camino.get(0);

        //if(camino.size() > 1)  punto = camino.get(1);
        //else punto = camino.get(0);

        camino.remove(0);
        this.target = new PVector((punto.x * Constante.RESCALADO_SPRITE_WIDTH), (punto.y * Constante.RESCALADO_SPRITE_HEIGHT));
    }

    private void validarCambioTarget(){
        //System.out.println("Actual "+this.targetFinal.x);
        //if(targetFinalAnt != null)System.out.println("ANTIGUO" + this.targetFinalAnt.x);
        if(targetFinalAnt == null || (targetFinalAnt.x != targetFinal.x || targetFinalAnt.y != targetFinal.y)){
            targetFinalAnt = new PVector(targetFinal.x, targetFinal.y);
            haCambiadoTarget = true;
        }else haCambiadoTarget = false;
    }

    private boolean validarTarget(){
        boolean haPasado = false;

        if(this.target != null){
            float casillaTargetX = this.target.x / Constante.RESCALADO_SPRITE_WIDTH;
            float casillaTragetY = this.target.y / Constante.RESCALADO_SPRITE_HEIGHT;
            float casillaPosicionX = this.posicion.x / Constante.RESCALADO_SPRITE_WIDTH;
            float casillaPosicionY = this.posicion.y / Constante.RESCALADO_SPRITE_HEIGHT;

            if(casillaTargetX != casillaPosicionX || casillaTragetY != casillaPosicionY) haPasado = true;
        }

        return haPasado;
    }

    private void move() {
        //calAcc();
        //calSpeed();
        calPos();
        this.acc = new PVector();
    }

    private void calAcc() {
        PVector findTarget = PVector.sub(this.target, this.posicion);
        findTarget.normalize();
        findTarget.mult(Constante.RESCALADO);
        this.acc.add(findTarget);
    }

    private void calSpeed() {
        this.speed.add(this.acc);
        this.speed.limit(this.maxSpeed);
    }

    private void calPos() {
        this.speed = new PVector(0,0);

        if(this.posicion.x < this.target.x){
            this.speed.add(new PVector(maxSpeed, 0));
        }

        if(this.posicion.x > this.target.x){
            this.speed.add(new PVector(-maxSpeed, 0));
        }

        if(this.posicion.y < this.target.y){
            this.speed.add(new PVector(0, maxSpeed));
        }

        if(this.posicion.y > this.target.y){
            this.speed.add(new PVector(0, -maxSpeed));
        }


        this.posicion.add(this.speed);
    }

    protected void debugCamino(PGraphics graphics, PVector anchoDirs){
        if(!activeIA) return;
        for (Point punto : camino) {
            int x = punto.x * Constante.RESCALADO_SPRITE_WIDTH;
            int y = punto.y * Constante.RESCALADO_SPRITE_HEIGHT;

            graphics.fill(Constante.BLANCO.getRGB(), 100f);
            graphics.strokeWeight(1);
            graphics.stroke(Constante.BLANCO.getRGB());
            graphics.pushMatrix();
            graphics.translate(x + anchoDirs.x, y + anchoDirs.y);
            graphics.rect(0, 0, Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT);
            graphics.rect(0, 0, 1, 1);
            graphics.popMatrix();

        }
    }

    public void setAnchorDist(PVector anchorDist){
        this.anchorDist = anchorDist;
    }

    public void setTarget(PVector target) {
        this.target = target;
    }

    public void setTargetFinal(PVector targetFinal) {
        if(activeIA)
        this.targetFinal = targetFinal;
        else
            this.target = targetFinal;
    }

    public PVector getPosicionRedondeada() {
        return new PVector(PApplet.round(this.posicion.x), PApplet.round(this.posicion.y));
    }

    public PVector getTargetPosicionRedondeada() {
        return new PVector(PApplet.round(this.target.x), PApplet.round(this.target.y));
    }

    public float getRadio() {
        return radio;
    }

    public PVector getPosicion() {
        return posicion;
    }

    public void setDie(boolean die) {
        isDie = die;
    }
}
