package consultas;

import base_datos.atributos;
import base_datos.nodo_tabla;
import base_datos.registro_tabla;
import base_datos.tabla;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class consulta {

    

    public consulta() {
    }
    
    public static void productoCartesiano(LinkedList<tabla> tablas){
        
        //iterar la primera tabla con todas las siguientes para obtener una tabla resultado
        //como vamos a manejar los encabezados??
        LinkedList<registro_tabla> val= retornarCartesiano(tablas);
        System.out.println("Jherson");
        for(registro_tabla r:val){
            for(nodo_tabla n:r.registro)
                System.out.print("| "+n.valor);
            System.out.println("|");
        }
        
    }
    
    private static LinkedList<registro_tabla> retornarCartesiano(LinkedList<tabla> tablas){
        if(tablas.size()==1){
            return tablas.get(0).registros;//supongamos jajaja
        }else{
            tabla uno = tablas.get(0);
            tablas.removeFirst();
            LinkedList<registro_tabla>result= new LinkedList<>();
            for(tabla aux:tablas){
                for (registro_tabla r : uno.registros) {
                    registro_tabla nuevo = new registro_tabla();
                    
                    for (nodo_tabla n : r.registro) {
                        nuevo.registro.addLast(n);
                    }
                    registro_tabla nuevo2 = new registro_tabla();
                    
                    for(nodo_tabla b:nuevo.registro)
                        nuevo2.registro.addLast(b);
                    
                    for (registro_tabla x : aux.registros) {
                        for (nodo_tabla n : x.registro) {
                            nuevo2.registro.addLast(n);
                        }
                        
                        result.add(nuevo2);//agrego los nuevos valores
                        
                        nuevo2=new registro_tabla();
                        
                        for(nodo_tabla b:nuevo.registro)
                            nuevo2.registro.addLast(b);
                    }
                    
                }
            }
            return result;
        }
    }
    
    
    
}
