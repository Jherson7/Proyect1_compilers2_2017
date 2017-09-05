package base_datos;

import java.util.HashMap;

/**
 *
 * @author Jherson Sazo
 */
public class Objetos {
    public String nombre;
    public HashMap<String,usuario> users;
    public HashMap<String,Atributos_Obj> atributos;

    public Objetos(String nombre, HashMap<String, usuario> users, HashMap<String, Atributos_Obj> atributos) {
        this.nombre = nombre;
        this.users = users;
        this.atributos = atributos;
    }

    public Objetos(String nombre) {
        this.nombre = nombre;
        this.users = new HashMap<>();
        this.atributos =new HashMap<>();
    }
    
    
}
