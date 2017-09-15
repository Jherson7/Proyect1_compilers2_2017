package consultas;

import ArbolAST.Nodo;
import ArbolAST.cabecera;
import ArbolAST.variable;
import base_datos.Objetos;
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
    
    public static  LinkedList<registro_tabla> productoCartesiano(LinkedList<tabla> tablas){
       LinkedList<registro_tabla> lista = new LinkedList<>();
       LinkedList<registro_tabla> ultima = new LinkedList<>();
       int cont =1;
       
       //
       lista=(LinkedList)tablas.getFirst().registros.clone();
       if(tablas.size()>1){
         prueba3(tablas, lista, ultima, cont, tablas.getFirst().registros);
         
       }
       else
           ultima=tablas.getFirst().registros;
        int a=0;
        for(registro_tabla t:ultima){
            for(nodo_tabla d: t.registro)
                System.out.print("| "+d.valor);
            System.out.println("| "+a);a++;
        } 
        System.out.println("----------------------*************-----------------------");
       return ultima;
      
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
    
    private static void prueba3(LinkedList<tabla> tablas, LinkedList<registro_tabla> lista,
            LinkedList<registro_tabla> ultima,int cont,LinkedList<registro_tabla> anterior){
    
        if(cont+1==tablas.size()){
            //estamos en la ultima tabla
            for(registro_tabla x:tablas.getLast().registros){

                    for (registro_tabla y : lista) {

                        registro_tabla z = new registro_tabla();

                        z.registro = (LinkedList) y.registro.clone();
                        for (nodo_tabla w : x.registro) {
                            //prueba codigo nuevo
                            
                            z.registro.addLast(w);
                        }
                        ultima.addLast(z);
                    }
            }
        }else{
            for( ; cont<tablas.size()-1; cont++){
                tabla v= tablas.get(cont);
                //crear un array de linkedlist de cada tabla "anterior" con la nueva
                LinkedList<LinkedList<registro_tabla>> lista_aux= new LinkedList<>();
                
                for(registro_tabla x: anterior){//ver como jalar la anterior anterio seria v
                    //tiene que ir la linkedlist de x
                    LinkedList<registro_tabla> lista_x = new LinkedList<>();
                    for(registro_tabla y: v.registros){
                        
                        registro_tabla z = new registro_tabla();

                        z.registro = (LinkedList) x.registro.clone();
                        for (nodo_tabla w : y.registro) {
                            z.registro.addLast(w);
                        }
                        lista_x.addLast(z);
                    }
                    //lista_aux.add(lista_x);
                    prueba3(tablas, lista_x, ultima, cont+1, v.registros);
                }
            }
        }
    }
    
    public static  LinkedList<registro_tabla> retornarConCampos(LinkedList<registro_tabla>lista,Nodo atributos){
        
        //LinkedList<String> cabeceras=new LinkedList<>();
        
        Nodo atri = atributos.hijos.get(0);
        if(!atri.nombre.equals("ID_ATR")){
            return retornarConCampos2(lista, atributos);
        }
        /*
        for(Nodo h:atributos.hijos)
            cabeceras.add(h.nombre);
        */
         LinkedList<cabecera> cabeceras=new LinkedList<>();//la tabla funciona cuando son mas de dos tablas
                                                            //porque se necesita saber de que tabla es                          
        
        for(Nodo h:atributos.hijos)
        {
            String id = h.hijos.get(0).nombre;
            
            if(h.hijos.size()==1)
            {
                cabeceras.addLast(new cabecera(id,id , false));
            }else{
                Nodo atr = h.hijos.get(1);
                cabeceras.addLast(new cabecera(id, id, atr.hijos.get(0).nombre, true));
            }
            
            
            
        }
        
        LinkedList<registro_tabla> result = new LinkedList<>();
        
        int a=0;
        for(registro_tabla x:lista){
            a=0;
            registro_tabla aux = new registro_tabla();
            
            for(cabecera c:cabeceras){
                
               for(nodo_tabla y:x.registro){
                   if(c.atri){
                          if(y.tipo.equals(c.nombre)){
                              Objetos nuevo = (Objetos)y.valor;
                              variable v = nuevo.atributos.get(c.n_atri);
                              nodo_tabla nt = new nodo_tabla(c.nombre, c.n_atri, v.valor, c.tabla);
                              aux.registro.addLast(nt);break;
                          }
                   }else{
                       if(y.nombre.equals(cabeceras.get(a).nombre)){
                         aux.registro.addLast(y);break;}
                      } 
                   }
               }
            result.add(aux);
        }
        return result;
    }
    
    
    private static LinkedList<registro_tabla> retornarConCampos2(LinkedList<registro_tabla>lista,Nodo atributos){
        
        LinkedList<cabecera> cabeceras=new LinkedList<>();
        
        for(Nodo h:atributos.hijos)
        {
            String id = h.hijos.get(0).nombre;
            Nodo atr = h.hijos.get(1);
            if(atr.hijos.size()==1)
            {
                cabeceras.addLast(new cabecera(id, atr.hijos.get(0).nombre, false));
            }else{
                cabeceras.addLast(new cabecera(id, atr.hijos.get(0).nombre, atr.hijos.get(1).nombre,true));
            }
        }
        
        
        
        
        LinkedList<registro_tabla> result = new LinkedList<>();
        
        int a=0;
        for(registro_tabla x:lista){
            a=0;
            registro_tabla aux = new registro_tabla();
            
            for(cabecera c:cabeceras){
                
               for(nodo_tabla y:x.registro){
                   if(c.atri){
                      if(c.tabla.equals(y.tabla)){
                          if(y.tipo.equals(c.nombre)){
                              Objetos nuevo = (Objetos)y.valor;
                              variable v = nuevo.atributos.get(c.n_atri);
                              nodo_tabla nt = new nodo_tabla(c.nombre, c.n_atri, v.valor, c.tabla);
                              aux.registro.addLast(nt);break;
                          }
                      } 
                   }
                   else if(c.tabla.equals(y.tabla)){
                      if(y.nombre.equals(cabeceras.get(a).nombre)){//primero comparar si es la tabla //despues atributo//despues objeto
                         aux.registro.addLast(y);break;}
                      } 
                   }
               a++;
            }
            result.add(aux);
        }
        return result;
    }
    
}
