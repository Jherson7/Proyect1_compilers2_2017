/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package levantarBD;

import ArbolAST.Nodo;
import base_datos.bd;
import java.io.FileWriter;

/**
 *
 * @author Jherson Sazo
 */
public class graficador {
 
    private int contador;
    private StringBuilder graphivz;

    public graficador(Nodo raiz) {
        contador=0;
        graphivz=new StringBuilder();
        graphivz.append("digraph G {\r\n node[shape=doublecircle, style=filled, color=khaki1, fontcolor=black]; \r\n");
        Arbol_listar_enlazar(raiz, contador);
        graficador(graphivz.toString());
        graphivz.append("}");
    }
    
    
    private void Arbol_listar_enlazar(Nodo nodo, int num)
        {
            if (nodo != null)
            {
                graphivz.append("node" + num + " [ label = \"" + nodo.nombre + "\"];\r\n");
                int tam = nodo.hijos.size();
                int actual;
                for (int i = 0; i < tam; i++)
                {
                    contador = contador + 1;
                    actual = contador;
                    Arbol_listar_enlazar(nodo.hijos.get(i), contador);
                    graphivz.append("\"node" + num + "\"->\"node" + actual + "\";\r\n");
                }
            }
        }
    
    private void graficador(String texto){
        
        try{
           FileWriter fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\arbol.dot");
           fichero.write(texto + "\r\n");//actualizamos la base de datos
           fichero.close();//cerramos el escritor

       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }
    
}
