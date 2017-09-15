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
    private int inicio,fin;

    public Nodo(String nombre, int fila, int columna) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.inicio=0;
        this.fin=0;
    }
    
    public Nodo(String nombre, int fila, int columna,String hijo1) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.hijos.addLast(new Nodo(hijo1,fila,columna));
        this.inicio=0;
        this.fin=0;
    }
    
    public Nodo(String nombre,Nodo hijo1) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = hijo1.fila;
        this.columna = hijo1.columna;
        this.hijos.addLast(hijo1);
        this.inicio=0;
        this.fin=0;
    }
    
     public Nodo(String nombre, int fila, int columna,String hijo1,String hijo2) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.hijos.addLast(new Nodo(hijo1,fila,columna));
        this.hijos.addLast(new Nodo(hijo2,fila,columna));
        this.inicio=0;
        this.fin=0;
    }
     public Nodo(String nombre, int fila, int columna,Nodo hijo1,String hijo2) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.hijos.addLast(hijo1);
        this.hijos.addLast(new Nodo(hijo2,fila,columna));
        this.inicio=0;
        this.fin=0;
    }
     
     
     //CONSTRUCTOR PARA EXPRESIONES
     public Nodo(String nombre,Nodo exp1,String op,Nodo exp2) {
        this.hijos = new LinkedList<>();
        this.nombre = nombre;
        this.fila = exp1.fila;
        this.columna = exp2.fila;
        this.hijos.addLast(exp1);
        this.hijos.addLast(new Nodo(op,fila,columna));
        this.hijos.addLast(exp2);
        this.inicio=0;
        this.fin=0;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }
     
    
}
