package Beans.Entes;

import Gestores.GestorInteligenciaArtificial;
import Herramientas.Constante;
import Herramientas.Global;
import Herramientas.Utils;
import com.jogamp.opengl.GL;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public class Monster extends Ente {

    private Color color = new Color(0x274D55);

    public Monster(PVector posicion, int[][] tablero, PVector targetFinal, final int filasMapa, final int columnasMapa){
        this.id = 0;
        this.posicion = new PVector(posicion.x, posicion.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 1f * Constante.RESCALADO;
        this.radio = 20f;
        this.tablero = tablero;
        this.targetFinal = targetFinal;
        this.filasMapa = filasMapa;
        this.columnasMapa = columnasMapa;
        this.sprite = Global.spriteMonster;
        this.sprite.resize(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT*2);
        initIA();
    }

    @Override
    protected void initIA() {
        this.gestorInteligenciaArtificial = new GestorInteligenciaArtificial(tablero, this.posicion, this.targetFinal, this.filasMapa, this.columnasMapa);
    }
}
