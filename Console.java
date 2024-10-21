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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Console implements PropertyChangeListener {
    private Line line;

    public Console(Line line) {
        this.line = line;
        this.line.addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int seq = (int) evt.getNewValue();
        switch (seq) {
            case Shortcuts.R:  // Mover el cursor a la derecha
                System.out.print(Shortcuts.MOVE_R);
                break;
            case Shortcuts.L:  // Mover el cursor a la izquierda
                System.out.print(Shortcuts.MOVE_L);
                break;
            case Shortcuts.DEL: // Acción de borrar
                System.out.print(Shortcuts.SUPRIMIR);
                break;
            case Shortcuts.HOME: // Mover el cursor al inicio
                System.out.print(Shortcuts.ESC + "[" + line.getCursorPos()+ "D");
                break;
            case Shortcuts.END: // Mover el cursor al final
                int moveRight = line.getBufferLength() - line.getCursorPos();
                System.out.print(Shortcuts.ESC + "[" + moveRight + "C");
                break;
            case Shortcuts.SUPR: // Suprimir un carácter
                System.out.print(Shortcuts.SUPRIMIR);
                break;
            case Shortcuts.INS: // Cambiar a modo de sobreescritura
                System.out.print(Shortcuts.OVERWRITE);
                break;
            case Shortcuts.NOINS: // Cambiar a modo de inserción
                System.out.print(Shortcuts.WRITE);
                break;
            default:
                // Sin acción para valores no reconocidos
                break;
        }
    }
}

