package Beans.Entes;

import Herramientas.Constante;
import Herramientas.Global;
import processing.core.PVector;

public class KingSlime extends Ente {

    public KingSlime(PVector posicion, int[][] tablero, PVector targetFinal, final int filasMapa, final int columnasMapa){
        this.id = 1;
        this.posicion = new PVector(posicion.x, posicion.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 1f * Constante.RESCALADO;
        this.radio = 140f;
        this.tablero = tablero;
        this.targetFinal = targetFinal;
        this.filasMapa = filasMapa;
        this.columnasMapa = columnasMapa;
        this.sprite = Global.spriteKingSlime;
        this.sprite.resize(Constante.RESCALADO_SPRITE_WIDTH*2, Constante.RESCALADO_SPRITE_HEIGHT*2);
        initIA();
    }

    @Override
    protected void initIA() {
        this.activeIA = false;
    }
}