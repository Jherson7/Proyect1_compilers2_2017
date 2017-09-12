package base_datos;

import ArbolAST.Nodo;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class procedimientos {
    
    public String nombre;
    //voy a utilizar el Atributos_Obj como los parametros del procedimiento
    public LinkedList<Atributos_Obj>parametros;
    public Nodo sentencias;
    public Nodo params;

    public procedimientos(String nombre) {
        this.nombre=nombre;
        this.parametros=new LinkedList<>();
        this.sentencias=null;
        this.params=null;
        
    }
    
}
