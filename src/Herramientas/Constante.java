package Herramientas;

import Gestores.GestorControles;
import processing.core.PImage;

import java.awt.*;

public class Constante {

    public static final float MAX_FPS = 60;
    public static final GestorControles KEYBOARD = new GestorControles();
    public static final int SPRITE_WIDTH = 16;
    public static final int SPRITE_HEIGHT = 16;
    public static final int RESCALADO = 2;
    public static final int RESCALADO_SPRITE_WIDTH = Constante.SPRITE_WIDTH * Constante.RESCALADO;
    public static final int RESCALADO_SPRITE_HEIGHT = Constante.SPRITE_HEIGHT * Constante.RESCALADO;
    public static final boolean MODE_DEBUG = false;
    public static final boolean CIERRE_ESC_DESHABILITADO = true;
    public static final Color BLANCO = new Color(0xFFFFFF);
    public static final Color ROJO = new Color(0xFF3831);

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
}
