/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestReadLine;

/**
 *
 * @author alexg
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

class EditableBufferedReader extends BufferedReader {
    private Line line;
    private int mouseButton, mouseX, mouseY;

    public EditableBufferedReader(Reader in) {
        super(in);
        line = new Line();
        this.mouseButton = 0;
        this.mouseX = 0;
        this.mouseY = 0;
    }

    // Cambiar la terminal a modo "raw"
    public void setRaw() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", "stty -echo raw </dev/tty");
            processBuilder.start();
        } catch (IOException ex) {
            System.out.println("Error: No se pudo cambiar al modo raw.");
        }
    }

    // Volver a modo "cooked"
    public void unsetRaw() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", "stty cooked echo </dev/tty");
            processBuilder.start();
        } catch (IOException ex) {
            System.out.println("Error: No se pudo cambiar a modo cooked.");
        }
    }

    // Activar el soporte del mouse
    public void activateMouse() {
        System.out.print(Shortcuts.ACTIVATE_MOUSE);
    }

    // Desactivar el soporte del mouse
    public void deactivateMouse() {
        System.out.print(Shortcuts.DEACTIVATE_MOUSE);
    }

    // Leer caracteres y manejar comandos especiales
    public int read() {
        int charCode = 0;

        try {
            int input = super.read();
            if (input != Key.ESC) {
                return (input == Key.DEL) ? Shortcuts.DEL : input;
            }

            if (super.read() == Key.BRACKET) {
                switch (input = super.read()) {
                    case Key.SUPR:
                        if (super.read() == '~') {
                            return Shortcuts.SUPR;
                        }
                        break;
                    case Key.L:
                        return Shortcuts.L;
                    case Key.R:
                        return Shortcuts.R;
                    case Key.INS:
                        if (super.read() == '~') {
                            return Shortcuts.INS;
                        }
                        break;
                    case Key.END:
                        return Shortcuts.END;
                    case Key.HOME:
                        return Shortcuts.HOME;
                    case Key.MOUSE:
                        mouseButton = super.read();
                        mouseX = super.read();
                        mouseY = super.read();
                        return Shortcuts.MOUSE;
                }
            } else if (input == 'O') {
                switch (input = super.read()) {
                    case Key.HOME:
                        return Shortcuts.HOME;
                    case Key.END:
                        return Shortcuts.END;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        }

        return charCode;
    }

    // Leer una línea y procesar los caracteres ingresados
    public String readLine() {
        setRaw();
        activateMouse();
        char currentChar;

        do {
            currentChar = (char) this.read();
            switch (currentChar) {
                case Shortcuts.DEL:
                    line.delete();
                    break;
                case Shortcuts.END:
                    line.fin();
                    break;
                case Shortcuts.HOME:
                    line.ini();
                    break;
                case Shortcuts.INS:
                    line.insertMode();
                    break;
                case Shortcuts.L:
                    line.moveCursorL();
                    break;
                case Shortcuts.R:
                    line.moveCursorR();
                    break;
                case Shortcuts.SUPR:
                    line.supr();
                    break;
                case Shortcuts.MOUSE:
                    line.mouseClick(mouseX - 32, mouseY - 32); // Coordenadas mouse ajustadas
                    break;
                default:
                    line.add(currentChar);  // Añadir el carácter a la línea
                    break;
            }
        } while (currentChar != '\r'); // Hasta que se presione "Enter"

        System.out.println("\nLínea final: " + line.toString());
        unsetRaw();
        deactivateMouse();
        return line.toString();
    }
}


