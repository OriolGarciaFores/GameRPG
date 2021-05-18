package Gestores;

import Beans.comunes.EstadoJuego;
import Herramientas.Global;
import processing.core.PGraphics;

public class GestorEstados {

    private final EstadoJuego[] ESTADOS;
    private final int ESTADO_DEFAULT = -1;
    private final int MAX_ESTADOS = 2;

    private int estado;
    private EstadoJuego estadoActual;

   public GestorEstados(){
       this.ESTADOS = new EstadoJuego[MAX_ESTADOS];
       this.estado = ESTADO_DEFAULT;
       initEstados();
       initEstadoActual();
   }

    private void initEstados() {
       this.ESTADOS[0] = new GestorMenu();
       this.ESTADOS[1] = new GestorJuego();
    }

    private void initEstadoActual() {
        this.estadoActual = ESTADOS[0];
    }

    public void update(PGraphics graphics) {
        this.estadoActual.update(graphics);
    }

    public void paint(PGraphics graphics){
        this.estadoActual.paint(graphics);
    }

    public void resetGestorJuego() {
      //  this.ESTADOS[1] = new GestorJuego();
    }

    //TODO REVISAR AL OVER
    public void changeStatus(){
        if(!Global.over){
            //this.estado = 1;
            this.estadoActual = ESTADOS[1];
        }else{
            this.estadoActual = ESTADOS[0];
        }
    }

    public void setEstado(final int estado) {
        this.estadoActual = ESTADOS[estado];
        this.estado = estado;
    }

    public int getEstado(){
       return this.estado;
    }
}
