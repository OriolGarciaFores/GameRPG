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
        Global.spriteMonster.resize(Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT*2);
        initIA();
    }

    @Override
    protected void initIA() {
        this.gestorInteligenciaArtificial = new GestorInteligenciaArtificial(tablero, this.posicion, this.targetFinal, this.filasMapa, this.columnasMapa);
    }

    @Override
    public void paint(PGraphics graphics) {
        graphics.noFill();
        graphics.strokeWeight(4);
        graphics.stroke(color.getRGB());
        graphics.pushMatrix();
        graphics.translate(posicion.x+anchorDist.x, posicion.y+anchorDist.y);
        body(graphics);
        graphics.popMatrix();
        if(Constante.MODE_DEBUG)debug(graphics);
    }

    private void debug(PGraphics graphics) {
        final int TEXT_SIZE = 13;
        Utils.debugValue("POSICION MATRIZ X: ", PApplet.round(this.posicion.x / Constante.RESCALADO_SPRITE_WIDTH), (posicion.x + anchorDist.x) - 20, (posicion.y + anchorDist.y) - 50, graphics, TEXT_SIZE);
        Utils.debugValue("POSICION MATRIZ Y: ", PApplet.round(this.posicion.y / Constante.RESCALADO_SPRITE_HEIGHT), (posicion.x + anchorDist.x) - 20, (posicion.y + anchorDist.y) - 40, graphics, TEXT_SIZE);
        Utils.debugValue("POSICION MATRIZ OBJ X: ", PApplet.round(this.target.x / Constante.RESCALADO_SPRITE_WIDTH), (posicion.x + anchorDist.x) - 20, (posicion.y + anchorDist.y) - 30, graphics, TEXT_SIZE);
        Utils.debugValue("POSICION MATRIZ OBJ Y: ", PApplet.round(this.target.y / Constante.RESCALADO_SPRITE_HEIGHT), (posicion.x + anchorDist.x) - 20, (posicion.y + anchorDist.y) - 20, graphics, TEXT_SIZE);
        debugCamino(graphics, anchorDist);
        //Utils.debugAreaCirculo(radio, graphics, new PVector(this.posicion.x+anchorDist.x, posicion.y+anchorDist.y));

    }

    @Override
    public void body(PGraphics graphics) {
        //graphics.rect(0, 0, Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT);
        graphics.image(Global.spriteMonster, 0, 0);
    }
}
