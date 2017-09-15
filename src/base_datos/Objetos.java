package base_datos;

import ArbolAST.variable;
import java.util.HashMap;

/**
 *
 * @author Jherson Sazo
 */
public class Objetos implements Cloneable{
    public String nombre;
    public HashMap<String,usuario> users;
    public HashMap<String,variable> atributos;

    public Objetos(String nombre, HashMap<String, usuario> users, HashMap<String, variable> atributos) {
        this.nombre = nombre;
        this.users = users;
        this.atributos = atributos;
    }

    public Objetos(String nombre) {
        this.nombre = nombre;
        this.users = new HashMap<>();
        this.atributos =new HashMap<>();
    }
    /*
        @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }*/
    
     @Override
    public Objetos clone() {
        final Objetos clone;
        try {
            clone = (Objetos) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException("superclass messed up", ex);
        }
        clone.users = (HashMap<String, usuario>) this.users.clone();
        clone.atributos= (HashMap<String, variable>) this.atributos.clone();
        
        return clone;
    }
    
    
}
