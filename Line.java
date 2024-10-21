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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

public class Line {
    private PropertyChangeSupport changeSupport;
    private int cursorPos;
    private boolean isInsertMode;
    private StringBuffer buffer;
    public static final int MAX_CAPACITY = 32;

    public Line() {
        this.buffer = new StringBuffer(MAX_CAPACITY);
        this.cursorPos = 0;
        this.isInsertMode = false;
        this.changeSupport = new PropertyChangeSupport(this);
    }

    // Obtiene la fila actual del cursor en la terminal
    public int getCursorRow() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\033[6n");
        scanner.skip("\033\\[8;(\\d+);(\\d+)t");
        return Integer.parseInt(scanner.match().group(0));
    }

    // Permite agregar un listener que estará a la escucha de cambios
    public void addChangeListener(PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(listener);
    }

    // Elimina un listener previamente agregado
    public void removeChangeListener(PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(listener);
    }

    // Obtiene la posición actual del cursor en el buffer
    public int getCursorPos() {
        return this.cursorPos;
    }

    // Obtiene la longitud actual del buffer
    public int getBufferLength() {
        return buffer.length();
    }

    // Mueve el cursor una posición hacia la derecha, si es posible
    public void moveCursorRight() {
        if (this.cursorPos < buffer.length()) {
            this.changeSupport.firePropertyChange("moveRight", null, Shortcuts.R);
            this.cursorPos++;
        }
    }

    // Mueve el cursor una posición hacia la izquierda, si es posible
    public void moveCursorLeft() {
        if (this.cursorPos > 0) {
            this.changeSupport.firePropertyChange("moveLeft", null, Shortcuts.L);
            this.cursorPos--;
        }
    }

    // Inserta o sobrescribe un carácter en la posición actual
    public void addChar(char c) {
        if (isInsertMode && this.cursorPos < buffer.length()) {
            this.changeSupport.firePropertyChange("overwrite", null, Shortcuts.INS);
            buffer.setCharAt(this.cursorPos, c);
            System.out.print(c);
            this.cursorPos++;
        } else {
            this.changeSupport.firePropertyChange("write", null, Shortcuts.NOINS);
            buffer.insert(this.cursorPos, c);
            System.out.print(c);
            this.cursorPos++;
        }
    }

    // Borra el carácter en la posición inmediatamente anterior al cursor
    public void deleteChar() {
        if (this.cursorPos > 0) {
            buffer.deleteCharAt(cursorPos - 1);
            moveCursorLeft();
            this.changeSupport.firePropertyChange("delete", null, Shortcuts.DEL);
        }
    }

    // Mueve el cursor al inicio del buffer
    public void moveToStart() {
        this.changeSupport.firePropertyChange("moveToStart", null, Shortcuts.HOME);
        this.cursorPos = 0;
    }

    // Mueve el cursor al final del buffer
    public void moveToEnd() {
        this.changeSupport.firePropertyChange("moveToEnd", null, Shortcuts.END);
        this.cursorPos = buffer.length();
    }

    // Cambia entre modo inserción y modo sobrescritura
    public void toggleInsertMode() {
        this.isInsertMode = !this.isInsertMode;
    }

    // Borra el carácter en la posición actual
    public void suprChar() {
        if (this.cursorPos < buffer.length()) {
            buffer.deleteCharAt(cursorPos);
            this.changeSupport.firePropertyChange("delete", null, Shortcuts.SUPR);
        }
    }

    // Mueve el cursor basándose en la posición de un clic del ratón
    public void handleMouseClick(int clickX, int clickY) {
        if (clickY == this.getCursorRow()) {
            if (clickX > this.cursorPos) {
                while (clickX > this.cursorPos) {
                    moveCursorRight();
                }
            } else {
                while (clickX < this.cursorPos) {
                    moveCursorLeft();
                }
            }
        }
    }

    @Override
    public String toString() {
        moveToStart();
        return buffer.toString();
    }
}

