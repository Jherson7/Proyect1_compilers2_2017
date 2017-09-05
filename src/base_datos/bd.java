/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_datos;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class bd {
    public String nombre;
    public String direccion;
    public HashMap<String,usuario> users;
    public HashMap<String,tabla> tablas;
    public HashMap<String,procedimientos> procs;
    public HashMap<String,funciones> func;
    public HashMap<String,Objetos> Objetos;

    public bd(String nombre,String dir) {
        this.nombre=nombre;
        this.users= new HashMap<>();
        this.tablas= new HashMap<>();
        this.func= new HashMap<>();
        this.procs= new HashMap<>();
        this.Objetos = new HashMap<>();
        this.direccion     =dir;
    }
    
    
}
