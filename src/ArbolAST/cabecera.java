/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArbolAST;

/**
 *
 * @author Jherson Sazo
 */
public class cabecera {
    public String tabla;
    public String nombre;
    public boolean atri;
    public String n_atri;

    public cabecera(String tl,String nombre, boolean atri) {
        this.tabla=tl;
        this.nombre = nombre;
        this.atri = atri;
    }

    public cabecera(String tabla, String nombre, String n_atri, boolean atri) {
        this.tabla = tabla;
        this.nombre = nombre;
        this.atri = atri;
        this.n_atri = n_atri;
    }
    
    
}
