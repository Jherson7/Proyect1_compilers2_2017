package ArbolAST;

import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class Nodo {
    public LinkedList<Nodo> hijos;
    public String nombre;
    public int fila,columna;

    public Nodo(String nombre, int fila, int columna) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
    }
    
    public Nodo(String nombre, int fila, int columna,String hijo1) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.hijos.addLast(new Nodo(hijo1,fila,columna));
    }
    
    public Nodo(String nombre,Nodo hijo1) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = hijo1.fila;
        this.columna = hijo1.columna;
        this.hijos.addLast(hijo1);
    }
    
     public Nodo(String nombre, int fila, int columna,String hijo1,String hijo2) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.hijos.addLast(new Nodo(hijo1,fila,columna));
        this.hijos.addLast(new Nodo(hijo2,fila,columna));
    }
    
    
}
