/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_datos;

import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class tabla {
    
    public String nombre;
    public String ruta;
    public LinkedList<atributos> atributos;
    public boolean primary;
    public boolean auto_inc;
    public int counter;
    //insert de objetos de la tabla
    
    
    public tabla(String nom,String ruta) {
        this.nombre=nom;
        this.ruta=ruta;
        this.atributos=new LinkedList<>();
        this.primary=false;
        this.auto_inc=false;
        this.counter=1;
    }

    public tabla(String nombre, LinkedList<atributos> atributos) {
        this.nombre = nombre;
        this.atributos = atributos;
        this.primary=false;
    }
    
    
}
