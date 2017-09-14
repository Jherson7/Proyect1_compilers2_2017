package base_datos;

import ArbolAST.Nodo;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class procedimientos {
    
    public String nombre;
    public String tipo;
    public String retorno;
    
    //voy a utilizar el Atributos_Obj como los parametros del procedimiento
    public LinkedList<Atributos_Obj>parametros;
    public Nodo sentencias;
    public Nodo params;
    

    public procedimientos(String tipo,String nombre) {
        this.nombre=nombre;
        this.tipo=tipo;
        this.parametros=new LinkedList<>();
        this.sentencias=null;
        this.params=null;
        this.retorno="";
        
    }
    
    public procedimientos(String tipo,String nombre,String ret) {
        this.nombre=nombre;
        this.tipo=tipo;
        this.parametros=new LinkedList<>();
        this.sentencias=null;
        this.params=null;
        this.retorno=ret;
    }
    
}
