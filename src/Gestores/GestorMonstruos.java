package Gestores;

import Beans.Entes.Ente;
import Beans.Entes.Monster;
import Beans.Entes.Player;
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

    public void update(Player player, PVector anchorDist){
        this.target = player.getPosicion();
        //if(validarPosicionTarget())
       // this.target = obtenerPosicionTableroTarget(target);
        //this.posicionAnteriorTarget = this.target;

        this.anchorDist = anchorDist;
        updateMobs(player);
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

    private void updateMobs(Player player){
        for(int i = 0; i < mobs.size(); i++){
            Ente ente = mobs.get(i);
            ente.setAnchorDist(anchorDist);
            ente.setTargetFinal(target);
            ente.update();

            if (PVector.dist(ente.getPosicion(), this.target)<=player.getRadio()/2 + ente.getRadio()/2) {
               // player.reducirVida();//TODO Mejorar en base al daño del monstruo
            }
            if(player.espada.isUsed){
                System.out.println(ente.getPosicion() + "ENTE");
                //System.out.println(player.espada.getPosicionAtq() + "ARMA");
                if (PVector.dist(ente.getPosicion(), player.espada.getPosicionAtq())<=player.espada.getRadioAtaque()/2 + ente.getRadio()/2) {
                    ente.setDie(true);
                    mobs.remove(i);
                    System.out.println("MUERTO");
                }
            }


        }

    }

    private void paintMobs(PGraphics graphics){
        for(int i = 0; i < mobs.size(); i++){
            mobs.get(i).paint(graphics);
        }
    }
}
