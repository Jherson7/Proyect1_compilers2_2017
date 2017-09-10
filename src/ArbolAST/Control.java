/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArbolAST;

import base_datos.bd;
import base_datos.usuario;
import gramatica.archivo_maestro.ParseException;
import gramatica.archivo_maestro.master_sintax;
import gramatica.db.registro_db;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import levantarBD.graficador;
import levantarBD.levantarBase;

/**
 *
 * @author Jherson Sazo
 */
public class Control {
   private static HashMap<String,bd> bases;
   private static HashMap<String,usuario> usuarios;
   private static LinkedList<errores> lista_error;
   public static boolean levante=true;
   
   
   public static void iniciar(){
       if(bases==null)
           bases=new HashMap<>();
       if(usuarios== null)
           usuarios= new HashMap<>();
       if(lista_error==null)
           lista_error=new LinkedList<>();
       //if(levante)   descomentarla para levantar las bases de datos desde los xml
        // levantarBases();
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
           String nueva ="\n<DB>\n\t<nombre>"
                   +base.nombre +"</nombre>\n\t"
                   +"<path> C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre +"\\"+base.nombre+".usac</path>"
                   + "\n</DB>";
           
           String texto= retornarArchivoMaestro();
           texto+=nueva;
      
       bases.put(base.nombre, base);
       //escribirEnMaestro(texto,base); descomentarla para escribir en xml
   }
   
   private static String retornarArchivoMaestro(){
      String texto="";
       try {
           FileReader fr = null;
           
           File maestro = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\maestro.xml");
           fr = new FileReader (maestro);
           BufferedReader br = new BufferedReader(fr);
           // Lectura del fichero
           String linea;
           while((linea=br.readLine())!=null)
               texto+=linea;
       } catch (FileNotFoundException ex) {
           Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
           return "error";
       } catch (IOException ex) {
           Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
           return "error";
       }
       return texto;
   }
   
   private static void escribirEnMaestro(String texto,bd base){
        try{
           FileWriter fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\maestro.xml");
           fichero.write(texto + "\r\n");//actualizamos la base de datos
           fichero.close();//cerramos el escritor

           crearDirectorios(base);

       } catch (IOException ex) {
           ex.printStackTrace();
       }
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

    private static void crearDirectorios(bd base) {
       File directorio = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre);
       directorio.mkdirs();
       
       directorio = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre+"\\TABLAS");
       directorio.mkdirs();//CREO LA CARPETA DE TABLAS
       
       try {
           //
           FileWriter fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\" + base.nombre + "\\" + base.nombre + ".usac");
           fichero.write(""
                   + "<Object> <path>\n"
                   + "\t"
                   + "C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\" + base.nombre + "\\objetos.usac"
                   + "\n\t</path>"
                   + "\n\t</Object>\n"
                   + "<Procedure> <path>\n"
                   + "\t"
                   + "C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\" + base.nombre + "\\metodos.usac"
                   + "\n\t</path>"
                   + "\n\t</Procedure>\n");
           fichero.close();
//           fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre +"\\tabla.usac");
//           fichero.write("");
//           fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre +"\\procedimientos.usac");
//           fichero.write("");
//           fichero.close();
           fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre +"\\metodos.usac");
           fichero.write("");
           fichero.close();
       } catch (IOException ex) {
           Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
       }
      
       //File directorio = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre +"\\"+base.nombre+".usac");
    }

    private static void levantarBases() {
        Nodo raiz;
        String texto = retornarArchivoMaestro();
        System.out.println(texto);
//        texto="<DB>		<nombre>prueba</nombre>	"
//                + "<path> C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\prueba\\prueba.usac"
//                + "</DB>";
        StringReader lectura = new StringReader(texto);
        master_sintax ej=null;
        ej = new master_sintax(lectura);
        try {
            raiz = ej.Inicio();
            graficador gr= new graficador(raiz);
            levantarBase.upBase(raiz);
        } catch (ParseException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
       
                
    }

}
