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
public class errores {
    public String tipo;
    public String descripcion;
    public int linea;
    public int col;

    public errores(String tipo, String descripcion, int linea, int col) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.col = col;
    }
    
    public errores(int tipo, String descripcion,Nodo raiz) {
       switch(tipo){
           case 1:
               this.tipo="SEMANTICO";
               this.descripcion=descripcion;
               this.linea = raiz.fila;
               this.col=raiz.columna;
       }
    }
    
}
