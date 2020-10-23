package Gestores;

import Beans.comunes.Casilla;
import Herramientas.Constante;
import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.io.File;
import java.util.ArrayList;

public class GestorMapeado {

    private final String TYPE_CAPA_SPRITES_COLISIONS = "Colisiones";
    private int patronWidth;
    private int patronHeight;
    private int width;
    private int height;
    private String nombre;
    private JSONObject contenidoJSON;
    private ArrayList<int[]> capasIdSprites;
    //private Rectangle[] capaColisiones;
    private int[][] capaCollisions;

    //TODO EVITAR CARGAR MAPEADO AL ARRANCAR LA APLICACION

    public GestorMapeado() {
        cargarJSON();
    }

    private void cargarJSON() {
        String filePath = new File("").getAbsolutePath();
        File file = new File(filePath + "/src/Resources/Json/Mapa_1.json");
        contenidoJSON = PApplet.loadJSONObject(file);
        patronWidth = contenidoJSON.getInt("width");
        patronHeight = contenidoJSON.getInt("height");
        nombre = contenidoJSON.getString("nameMap");
        calcularTamanoMapa();
        cargarCapas();
    }

    private void calcularTamanoMapa() {
        width = patronWidth * Constante.RESCALADO_SPRITE_WIDTH;
        height = patronHeight * Constante.RESCALADO_SPRITE_HEIGHT;
    }

    private void cargarCapas() {
        final String CAPA_SPRITES = "tilelayer";
        final String CAPA_OBJETOS = "objectgroup";
        JSONArray capas = this.contenidoJSON.getJSONArray("layers");
        capasIdSprites = new ArrayList<>();

        for (int i = 0; i < capas.size(); i++) {
            JSONObject datosCapa = capas.getJSONObject(i);
            String tipo = datosCapa.getString("type");

            switch (tipo) {
                case CAPA_SPRITES:
                    guardarCapasTerreno(datosCapa);
                    guardarCapaColisiones(datosCapa);
                    break;
                case CAPA_OBJETOS:
                    //guardarCapaColisiones(datosCapa);
                    //TODO CAPA DE PUNTOS EVENTOS O PUERTAS O CAMBIOS DE ZONAS
                    break;
                default:
                    System.out.println("Error: No se encontro la capa JSON.");
                    break;

            }
        }
    }

    private void guardarCapasTerreno(JSONObject datosCapa) {
        if(datosCapa.getString("name").equalsIgnoreCase(TYPE_CAPA_SPRITES_COLISIONS)) return;
        JSONArray spirtesId = datosCapa.getJSONArray("data");

        int[] spritesCapas = new int[spirtesId.size()];
        for (int j = 0; j < spirtesId.size(); j++) {
            spritesCapas[j] = PApplet.parseInt(spirtesId.get(j).toString());
        }
        capasIdSprites.add(spritesCapas);
    }

    /*private void guardarCapaColisiones(JSONObject datosCapa) {
        JSONArray colisiones = datosCapa.getJSONArray("objects");
        capaColisiones = new Rectangle[colisiones.size()];

        for(int i = 0; i < colisiones.size(); i++){
            JSONObject jsonObject = (JSONObject) colisiones.get(i);
            int x = (int) Math.round(jsonObject.getDouble("x")) * Constante.RESCALADO;
            int y = (int) Math.round(jsonObject.getDouble("y")) * Constante.RESCALADO;
            int ancho = (int) Math.round(jsonObject.getDouble("width")) * Constante.RESCALADO;
            int alto = (int) Math.round(jsonObject.getDouble("height")) * Constante.RESCALADO;

            Rectangle rectangulo = new Rectangle(x, y, ancho, alto);
            capaColisiones[i] = rectangulo;
        }
    }*/

    private void guardarCapaColisiones(JSONObject datosCapa){
        if(datosCapa.getString("name").equalsIgnoreCase(TYPE_CAPA_SPRITES_COLISIONS)){
            JSONArray collisionsId = datosCapa.getJSONArray("data");
            capaCollisions = new int[patronWidth][patronHeight];
            int id = 0;
            for(int x = 0; x < patronWidth; x++){
                for(int y = 0; y < patronHeight; y++){
                    int idCollision = PApplet.parseInt(collisionsId.get(id).toString());
                    if(idCollision != 0) idCollision = 1;
                    capaCollisions[x][y] = idCollision;
                    id++;
                }
            }

            if(Constante.MODE_DEBUG)debugCargaMapaColisiones();
        }
    }
    private void debugCargaMapaColisiones(){
        System.out.println(Constante.ANSI_BLUE + "DEBUG MAP COLLISION");
        for(int i = 0; i < capaCollisions.length; i++){
            for(int h = 0; h < capaCollisions[i].length; h++){
                int value = capaCollisions[i][h];
                if(value != 0){
                    System.out.print(Constante.ANSI_RED + value);
                }else{
                    System.out.print(Constante.ANSI_GREEN + value);
                }
            }
            System.out.println("");
        }

        System.out.println(Constante.ANSI_RESET);
    }

    public void cargarTerreno(PGraphics graphics, ArrayList<PImage> tiles, PVector anchoDirs) {
        int id;
        int x;
        int y;

        for (int[] listaIdSprites : capasIdSprites) {
            id = 0;
            x = 0;
            y = 0;

            for (int f = 0; f < patronWidth; f++) {
                for (int c = 0; c < patronHeight; c++) {
                    int idSprite = listaIdSprites[id] - 1;
                    id++;

                    if (idSprite < 0) {
                        x += Constante.RESCALADO_SPRITE_HEIGHT;
                        continue;
                    }
                    graphics.image(tiles.get(idSprite), x + anchoDirs.x, y + anchoDirs.y);
                    x += Constante.RESCALADO_SPRITE_HEIGHT;
                }
                x = 0;
                y += Constante.RESCALADO_SPRITE_WIDTH;
            }
        }
    }

    /*public void debugColisionesTerreno(PGraphics graphics, PVector anchoDirs){
        if(!Constante.MODE_DEBUG) return;
        if(capaColisiones == null) return;
        for(int i = 0; i < capaColisiones.length; i++){
            Rectangle rectangle = capaColisiones[i];
            graphics.fill(Constante.ROJO.getRGB(), 100f);
            graphics.strokeWeight(1);
            graphics.stroke(Constante.ROJO.getRGB());
            graphics.pushMatrix();
            graphics.translate(rectangle.x+anchoDirs.x, rectangle.y+anchoDirs.y);
            graphics.rect(0, 0, rectangle.width, rectangle.height);
            graphics.popMatrix();
        }
    }*/

    public void debugObstaculosTerreno(PGraphics graphics, PVector anchoDirs){
        if(!Constante.MODE_DEBUG) return;

        for(int i = 0; i < capaCollisions.length; i++){
            for(int h = 0; h < capaCollisions[i].length; h++){
                int box = capaCollisions[i][h];
                if(box == 1){
                    int x = h * Constante.RESCALADO_SPRITE_WIDTH;
                    int y = i * Constante.RESCALADO_SPRITE_HEIGHT;
                    graphics.fill(Constante.ROJO.getRGB(), 100f);
                    graphics.strokeWeight(1);
                    graphics.stroke(Constante.ROJO.getRGB());
                    graphics.pushMatrix();
                    graphics.translate(x + anchoDirs.x, y + anchoDirs.y);
                    graphics.rect(0, 0, Constante.RESCALADO_SPRITE_WIDTH, Constante.RESCALADO_SPRITE_HEIGHT);
                    graphics.rect(0, 0, 1, 1);
                    graphics.popMatrix();
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /*public Rectangle[] getCapaColisiones() {
        return capaColisiones;
    }*/

    public int[][] getCapaCollisions() {
        return capaCollisions;
    }

    public int getPatronWidth() {
        return patronWidth;
    }

    public int getPatronHeight() {
        return patronHeight;
    }
}
