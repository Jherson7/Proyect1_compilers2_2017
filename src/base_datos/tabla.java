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
    public LinkedList<atributos> atributos;
    public boolean primary;
    public boolean auto_inc;
    public int counter;
    
    
    public tabla(String nom) {
        this.nombre=nom;
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