package levantarBD;

import ArbolAST.Control;
import ArbolAST.Nodo;
import ArbolAST.ejecutor;
import base_datos.atributos;
import base_datos.bd;
import base_datos.tabla;
import gramatica.db.registro_db;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jherson Sazo
 */
public class levantarBase {

    public levantarBase(Nodo raiz) {
      upBase(raiz);
    }

    public static void upBase(Nodo raiz){
        switch(raiz.nombre){
            case "maestro":
                for(Nodo r: raiz.hijos)
                    upBase(r);
            break;
            case"DB":
                crearDB(raiz);
                break;
        }
    }
    
    private static void crearDB(Nodo raiz){
        String nombre = raiz.hijos.get(0).nombre;
        String path = raiz.hijos.get(1).nombre;
        
        bd nueva = new bd(nombre, path);
        //obtenemos contenido el archivo registroDB
        String db=retornarContenidoArchivo(path);
        //parseamos el archivo registroDB
        Nodo registro=retornarRegistroDB(db);
        if(registro!=null){
            recorrerReg(registro, nueva);
        
        }
    }
    
    
    private static String retornarContenidoArchivo(String ruta){
       String texto="";
        
       try {
           FileReader fr = null;
           
           File maestro = new File(ruta);
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

    private static Nodo retornarRegistroDB(String texto){
        Nodo raiz = null;
        StringReader lectura = new StringReader(texto);
        registro_db ej = null;
        ej = new registro_db(lectura);
        try {
            raiz = ej.Inicio();
        } catch (gramatica.db.ParseException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "ERROR Al parsear", "ERROR", 0);
        }
        return raiz;
    }

    private static void recorrerReg(Nodo raiz, bd base) {
        switch (raiz.nombre) {
            case "registro_db":
                for (Nodo r : raiz.hijos) {
                    recorrerReg(r,base);
                }
                break;
            case "PROC":
                //crearDB(raiz);
                break;
            case "OBJETO":
                //crearDB(raiz);
                break;
            case "Tabla":
                agregarTabla(raiz, base);
                break;
        }
    }
    
    private static void agregarTabla(Nodo raiz,bd base){
        String nombre = raiz.hijos.get(0).nombre;
        String path = raiz.hijos.get(1).nombre;
        
        
        
        tabla nueva = new tabla(nombre,path);
        
        Nodo atributos=raiz.hijos.get(2);
        
        
        for(Nodo r:atributos.hijos){
            String tipo = r.hijos.get(0).nombre.replace("<", "").replace(">", "");
            String nom = r.hijos.get(1).nombre;
            
            atributos atr=new atributos(nom, tipo);
            
           if(r.hijos.size()==3){ 
            for(Nodo s: r.hijos.get(2).hijos){
                switch(s.nombre){
                    case"NOT_NULL":
                        atr.nulo=true;
                        break;
                    case"PK":
                        atr.primary_key=true;
                        break;
                    case"INC":
                        atr.auto_inc=true;
                        break;
                    case"FK"://EDITAR LA FK
                        atr.foreing_key=true;
                        break;
                }
            }
         }
           nueva.atributos.addLast(atr);
       }
        if(!base.tablas.containsKey(nombre)){
            base.tablas.put(nombre,nueva);
        }//marcar error
    }
}


