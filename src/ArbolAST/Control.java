/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArbolAST;

import base_datos.bd;
import base_datos.usuario;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Jherson Sazo
 */
public class Control {
   private static HashMap<String,bd> bases;
   private static HashMap<String,usuario> usuarios;
   private static LinkedList<errores> lista_error;
   
   public static void iniciar(){
       if(bases==null)
           bases=new HashMap<>();
       if(usuarios== null)
           usuarios= new HashMap<>();
       if(lista_error==null)
           lista_error=new LinkedList<>();
   }
   
   public static HashMap<String,bd> retornarBases(){
       if(bases==null)
           bases=new HashMap<>();
       return bases;
   }
   
   public static boolean existeDB(String nombre){
       return bases.containsKey(nombre);
   }
   
   public static bd retornarBase(String nombre){
       return bases.get(nombre);
   }
   
   public static void agregarBD(bd base){
       bases.put(base.nombre, base);
   }
   
   public static boolean agregarUsuario(usuario usu){
       if(!usuarios.containsKey(usu.nombre)){
           usuarios.put(usu.nombre,usu);
           return true;
       }else
           return false;
   }
   
   public static void agregarError(errores err){
       lista_error.addLast(err);
   }
}
