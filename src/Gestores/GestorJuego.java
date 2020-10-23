package Gestores;

import Beans.comunes.Map;
import Beans.Entes.Player;
import Beans.comunes.EstadoJuego;
import processing.core.PConstants;
import processing.core.PGraphics;

public class GestorJuego implements EstadoJuego{

    private final Player player;
    private final Map mapa;
    private final GestorMonstruos gm;

    public GestorJuego(){
        System.out.println("Gestor Juego - INICIALIZADO");
        mapa = new Map();
        player = new Player(mapa.getMapaCentroX(), mapa.getMapaCentroY());
        gm = new GestorMonstruos(player.getPosicion(), mapa.getObstaculosColisionablesMapa(), mapa.getFilasMapa(), mapa.getColumnasMapa());
        mapa.setRadioPlayer(player.getRadio());
        player.setObstaculosColisionables(mapa.getObstaculosColisionablesMapa());
        //gm.setTablero(mapa.getTablero());
        gm.addMonster();
    }

    @Override
    public void update(PGraphics graphics) {
        updateMapa();
        updatePlayer();
        updateGestorMonstruos();
    }

    private void updateMapa(){
        mapa.update(player.getPosicion());
    }

    private void updatePlayer(){
        player.setAnchor(mapa.getAnchor());
        player.setMapaX(mapa.getMapaX());
        player.setMapaY(mapa.getMapaY());
        //player.setObstaculos(mapa.getObstaculosMapa());
        player.update();
    }

    private void updateGestorMonstruos(){
        gm.update(player.getPosicion(), mapa.getAnchorDist());
    }

    public void paint(PGraphics graphics){
        graphics.rectMode(PConstants.CENTER);
        graphics.imageMode(PConstants.CENTER);
        mapa.paint(graphics);
        gm.paint(graphics);
        player.paint(graphics);
    }
}
