/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_datos;

/**
 *
 * @author Jherson Sazo
 */
public class atributos {
    public String nombre;
    public String tipo;
    public boolean nulo;
    public boolean auto_inc;
    public boolean primary_key;
    public boolean foreing_key;
    

    public atributos(String nombre, String tipo, boolean nulo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nulo = nulo;
    }
    
    public atributos(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nulo = this.foreing_key=this.primary_key =false;
    }
    
    
}
