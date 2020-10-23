package Gestores;

import Beans.Entes.Ente;
import Beans.Entes.Monster;
import Beans.comunes.Casilla;
import Herramientas.Constante;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class GestorMonstruos {

    ArrayList<Ente> mobs = new ArrayList<>();
    private PVector target;
    private PVector posicionAnteriorTarget;
    private PVector anchorDist;
    private int[][] tablero;
    private int filasMapa;
    private int columnasMapa;


    public GestorMonstruos(PVector target, int[][] tablero, final int filasMapa, final int columnasMapa){
        //addMonster();
        this.tablero = tablero;
        this.target = new PVector(target.x, target.y);
        this.filasMapa = filasMapa;
        this.columnasMapa = columnasMapa;
    }

    public void update(PVector target, PVector anchorDist){
        this.target = target;
        //if(validarPosicionTarget())
       // this.target = obtenerPosicionTableroTarget(target);
        //this.posicionAnteriorTarget = this.target;

        this.anchorDist = anchorDist;
        updateMobs();
    }

    private boolean validarPosicionTarget(){
        if(this.posicionAnteriorTarget != null && this.target != null && (this.posicionAnteriorTarget.x != this.target.x || this.posicionAnteriorTarget.y != this.target.y) ){
            return true;
        }else return false;
    }

    private PVector obtenerPosicionTableroTarget(PVector target){
        float x = target.x / Constante.RESCALADO_SPRITE_WIDTH;
        float y = target.y / Constante.RESCALADO_SPRITE_HEIGHT;

        return new PVector(x, y);
    }


    public void paint(PGraphics graphics){
        paintMobs(graphics);
    }

    public void addMonster(){
        Monster monster = new Monster(new PVector(200,200), this.tablero, this.target, this.filasMapa, this.columnasMapa);
        //mobs.add(monster);

       /* monster = new Monster(new PVector(120,120), this.tablero, this.target, this.filasMapa, this.columnasMapa);
        mobs.add(monster);

        monster = new Monster(new PVector(1200,1200), this.tablero, this.target, this.filasMapa, this.columnasMapa);
        mobs.add(monster);*/

        monster = new Monster(new PVector(608,608), this.tablero, this.target, this.filasMapa, this.columnasMapa);
        mobs.add(monster);

       /* monster = new Monster(new PVector(600,600), this.tablero, this.target, this.filasMapa, this.columnasMapa);
        mobs.add(monster);*/
    }

    private void updateMobs(){
        for(int i = 0; i < mobs.size(); i++){
            mobs.get(i).setAnchorDist(anchorDist);
            mobs.get(i).setTargetFinal(target);
            mobs.get(i).update();
        }
    }

    private void paintMobs(PGraphics graphics){
        for(int i = 0; i < mobs.size(); i++){
            mobs.get(i).paint(graphics);
        }
    }
}
