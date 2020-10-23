package Gestores;

import Beans.comunes.Casilla;
import Herramientas.Constante;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
//TODO OPTIMIZAR - CLEAN CODE

public class GestorInteligenciaArtificial {
    private ArrayList<Casilla> openSet;
    private ArrayList<Casilla> closedSet;
    private ArrayList<Point> camino;
    private boolean terminado;
    private PVector posicionInicial;
    private PVector posicionFinal;
    private final int MAX_WALKABLE_TILE_NUM = 0;
    private int filas;
    private int columnas;
    private int[][] tablero;

    public GestorInteligenciaArtificial(int[][] tablero, PVector posicionInicial, PVector posicionFinal, final int filas, final int columnas){
        this.tablero = tablero;
        this.filas = filas;
        this.columnas = columnas;
        init(posicionInicial, posicionFinal);
    }



    public void init(PVector posicionInicial, PVector posicionFinal){
        openSet = new ArrayList<>();
        closedSet = new ArrayList<>();
        camino = new ArrayList<>();
        terminado = false;
        this.posicionInicial = posicionInicial;
        this.posicionFinal = posicionFinal;
        //openSet.add(coordenadasIniciales);
    }

    public ArrayList<Point> buscarRuta(){
        return calcularRuta();
    }
//TODO POCO ENTENDIBLE - CLEAN CODE
    private ArrayList<Point> calcularRuta(){
        int worldSize = filas * columnas;
        Casilla casillaInicial = new Casilla(null, PApplet.parseInt(posicionInicial.x / Constante.RESCALADO_SPRITE_WIDTH), PApplet.parseInt(posicionInicial.y / Constante.RESCALADO_SPRITE_HEIGHT), filas);
        Casilla casillaFinal = new Casilla(null, PApplet.parseInt(posicionFinal.x / Constante.RESCALADO_SPRITE_WIDTH), PApplet.parseInt(posicionFinal.y / Constante.RESCALADO_SPRITE_HEIGHT), filas);
        boolean[] aStar = new boolean[worldSize];
        ArrayList<Casilla> vecinos = new ArrayList<>();
        Casilla myNode;
        Casilla myPath;
        int max = 0, min = 0, i = 0, j = 0;

        openSet.add(casillaInicial);

        while(openSet.size() > 0){
            max = worldSize;
            min = -1;

            for(i = 0; i < openSet.size(); i++){
                Casilla node = openSet.get(i);
                if(node.getF() < max){
                    max = node.getF();
                    min = i;
                }
            }

            myNode = openSet.get(min);
            openSet.remove(min);

            if(myNode.getH() == casillaFinal.getH()){
                closedSet.add(myNode);
                myPath = closedSet.get(closedSet.size()-1);

                //BUSQUEDA DE LOS PADRES
                do{
                    camino.add(new Point(myPath.getPosicionX(), myPath.getPosicionY()));

                    if(myPath.getPadre() != null) myPath = myPath.getPadre();
                }while(myPath.getPadre() != null);

                //Eliminar los arrays
                closedSet = new ArrayList<>();
                openSet = new ArrayList<>();
                aStar = new boolean[worldSize];

                Collections.reverse(camino);
                terminado = true;

            }else{
                vecinos = obtenerVecinos(myNode.getPosicionX(), myNode.getPosicionY());
                j = vecinos.size();
                for(i = 0;  i < j; i++){
                    myPath = new Casilla(myNode, vecinos.get(i).getPosicionX(), vecinos.get(i).getPosicionY(), filas);

                    if(!aStar[myPath.getH()]){
                        Casilla vecino = vecinos.get(i);
                        int g = myNode.getG() + diagonalDistance(vecino.getPosicionX(), vecino.getPosicionY(), myNode.getPosicionX(), myNode.getPosicionY());
                        int f = myPath.getG() + diagonalDistance(vecino.getPosicionX(), vecino.getPosicionY(), casillaFinal.getPosicionX(), casillaFinal.getPosicionY());

                        myPath.setG(g);
                        myPath.setF(f);
                        openSet.add(myPath);
                        aStar[myPath.getH()] = true;
                    }
                }
                closedSet.add(myNode);
            }
        }
        return camino;
    }

    private ArrayList<Casilla> obtenerVecinos(int x,int y){
        int norte = y - 1;
        int sud = y + 1;
        int este = x + 1;
        int oeste = x - 1;
        boolean myNorte = norte > -1 && canWalkHere(x, norte);
        boolean mySur = sud < columnas && canWalkHere(x, sud);
        boolean myEste = este < filas && canWalkHere(este, y);
        boolean myOeste = oeste > -1 && canWalkHere(oeste, y);
        ArrayList<Casilla> result = new ArrayList<>();

        if(myNorte) result.add(new Casilla(x, norte));
        if(myEste) result.add(new Casilla(este, y));
        if(mySur) result.add(new Casilla(x, sud));
        if(myOeste) result.add(new Casilla(oeste, y));
        diagonalVecinos(myNorte, mySur, myEste, myOeste, norte, sud, este, oeste, result);
        return result;

    }

    private boolean canWalkHere(int x, int y){
        return
                (tablero[y][x] <= MAX_WALKABLE_TILE_NUM);
    }

    private int manhattanDistance(int actualX, int actualY, int finalX, int finalY){
        return PApplet.abs(actualX - finalX) + PApplet.abs(actualY - finalY);
    }

    private int diagonalDistance(int actualX, int actualY, int finalX, int finalY){
        return PApplet.max(PApplet.abs(actualX - finalX), PApplet.abs(actualY - finalY));
    }

    private void diagonalVecinos(boolean myNorte, boolean mySur, boolean myEste, boolean myOeste, int norte, int sud, int este, int oeste, ArrayList<Casilla> result){
        if(myNorte){
            if(myEste && canWalkHere(este, norte)) result.add(new Casilla(este, norte));
            if(myOeste && canWalkHere(oeste, norte)) result.add(new Casilla(oeste, norte));
        }
        if(mySur){
            if(myEste && canWalkHere(este, sud)) result.add(new Casilla(este, sud));
            if(myOeste && canWalkHere(oeste, sud)) result.add(new Casilla(oeste, sud));
        }
    }

    public ArrayList<Point> getCamino() {
        return camino;
    }

    public void debugCargaMapaColisionesConCamino(){
        System.out.println(Constante.ANSI_BLUE + "DEBUG MAP COLLISION");
        for(int i = 0; i < tablero.length; i++){
            for(int h = 0; h < tablero[i].length; h++){
                int value = tablero[i][h];
                Point puntoEncontrado = null;
                for(Point punto : camino){
                    if(punto.x == h && punto.y == i)  puntoEncontrado = punto;
                }

                if(puntoEncontrado != null){
                    System.out.print(Constante.ANSI_CYAN + value);
                }else{
                    if(value != 0){
                        System.out.print(Constante.ANSI_RED + value);
                    }else{
                        System.out.print(Constante.ANSI_GREEN + value);
                    }
                }




            }
            System.out.println("");
        }

        System.out.println(Constante.ANSI_RESET);
    }

    /*public void algoritmo(){

        while(!terminado){
            if(openSet.size() > 0){
                int ganador = 0;

                for(int i = 0; i < openSet.size(); i++){
                    if(openSet.get(i).getF() < openSet.get(ganador).getF()){
                        ganador = i;
                    }
                }

                Casilla casillaActual = openSet.get(ganador);

                if(casillaActual.equals(fin)){
                    Casilla temporal = casillaActual;
                    camino.add(temporal);

                    while(temporal.getPadre() != null){
                        temporal = temporal.getPadre();
                        camino.add(temporal);
                    }

                    terminado = true;
                }else{
                    borrarElementoArray(openSet, casillaActual);
                    closedSet.add(casillaActual);

                    ArrayList<Casilla> vecinos = casillaActual.getVecinos();

                    for(int i = 0; i < vecinos.size(); i++){
                        Casilla vecino = vecinos.get(i);

                        if(!closedSet.contains(vecino) && vecino.getIsColisionable() != 1){
                            int tempG = casillaActual.getG() + 1;

                            if(openSet.contains(vecino)){
                                if(tempG < vecino.getG()){
                                    vecino.setG(tempG);
                                }
                            }else{
                                vecino.setG(tempG);
                                openSet.add(vecino);
                            }

                            vecino.setH(calcularHeuristica(vecino, fin));
                            vecino.setF(vecino.getG() + vecino.getH());
                            vecino.setPadre(casillaActual);
                        }
                    }
                }
            }else{
                terminado = true;
            }
        }
    }*/

    private void borrarElementoArray(ArrayList<Casilla> lista, Casilla elemento){
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).equals(elemento)) lista.remove(i);
        }
    }

  /*  private int calcularHeuristica(Casilla vecino, Casilla fin){
        int x = vecino.getX() - fin.getX();
        int y = vecino.getY() - fin.getY();
        int dist = x+y;

        return dist;
    }*/
}
