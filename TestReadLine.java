/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestReadLine;

import java.io.IOException;

/**
 *
 * @author alexg
 */
public class TestReadLine {
    public static void main(String[] args) {
        // Crear un lector de flujo de entrada basado en el teclado
        InputStreamReader input = new InputStreamReader(System.in);
        EditableBufferedReader reader = new EditableBufferedReader(input);
        
        int character = 0; // Variable para almacenar el carácter leído
        // Leer un solo carácter desde el terminal
        character = reader.read();

        // Mostrar el carácter leído por pantalla
        System.out.println("\nEl carácter leído es: " + character);
    }
}

    
    

