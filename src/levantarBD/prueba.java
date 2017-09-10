package levantarBD;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class prueba implements Serializable{
    String nombre;
    String apellido;
    int edad;
    LinkedList<Integer> numeros;

    public prueba(String nombre, String apellido, int edad, LinkedList<Integer> numeros) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.numeros = numeros;
    }
    
    
    
}
