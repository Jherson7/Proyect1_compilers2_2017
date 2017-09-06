package ArbolAST;

import base_datos.Atributos_Obj;
import base_datos.Objetos;
import base_datos.atributos;
import base_datos.bd;
import base_datos.tabla;
import base_datos.usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jherson Sazo
 */
public class ejecutor {
 
    public bd actual=null;
    public usuario user=null;
    
    public ejecutor() {
        Control.iniciar();
    }
    
    
    public  void recorrer(Nodo raiz){
        
        switch(raiz.nombre){
            case "INICIO":
                for (Nodo r : raiz.hijos) {
                    recorrer(r);
                }
                break;
            case "USAR":
                usarBD(raiz);
                break;
            case "BASE_DATOS":
                crearDB(raiz);
                break;
            case "CREAR_USER":
                crearUsuario(raiz);
                break;
            case "TABLA":
                crearTabla(raiz);
                break;
            case "OBJECTO":
                crearObjeto(raiz);
        }
    }
    
    
    public void usarBD(Nodo raiz){
        String nombre = raiz.hijos.get(0).nombre;
        bd base = Control.retornarBase(nombre);
        
        if(base!=null){
            actual =base;
        }
        //else agregar error y enviarlo
        
    }
    
    public void crearDB(Nodo raiz){
       
        String nombre = raiz.hijos.get(0).nombre;
        String ruta="C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+nombre +"\\";//ruta de la carpeta que se va a crear
        if(!Control.existeDB(nombre)){
                bd nueva = new  bd(nombre,ruta);
                Control.agregarBD(nueva);
        }
    }
    
    public void crearUsuario(Nodo raiz){
        String nombre = raiz.hijos.get(0).nombre;
        String pass = raiz.hijos.get(1).nombre;
        if(Control.agregarUsuario(new usuario(nombre,pass))){
         //se agrego correctamente el usuario   
        }
        else{
            //no se agrego correctamente
        }
    }
    
    public void crearTabla(Nodo raiz){
        String nombre = raiz.hijos.get(0).nombre;
        //comprobar si hay tabla use
        if (actual != null) {
            if (!actual.tablas.containsKey(nombre)) {
                tabla tl = new tabla(nombre,"falta agregarle la ruta de la tabla");
                Nodo atributos = raiz.hijos.get(1);
                boolean fl = true;
                for (Nodo aux : atributos.hijos) {
                    if (agregarTributo(tl, aux)) {
                        fl = true;
                    } else {
                        fl = false;
                        break;
                    }
                }
                if (fl) {
                    actual.tablas.put(nombre, tl);//comprobar que no exista la tabla
                    leerTabla(tl);//vamos a escribir el archivo
                }
            }else
                Control.agregarError(new errores("SEMANTICO", "Ya existe la tabla: "+nombre, raiz.fila, raiz.columna));
        }else{
            Control.agregarError(new errores("SEMANTICO", "No existe seleccionada BD para insertar la tabla: "+nombre, raiz.fila, raiz.columna));
        }
    }
    
    public boolean agregarTributo(tabla tl, Nodo raiz){
        String tipo = raiz.hijos.get(0).nombre;
        String nombre = raiz.hijos.get(1).nombre;

        boolean fl = true;
        for (atributos a : tl.atributos) {
            if (a.nombre.equals(nombre)) {
                fl = false;
                break;
            }
        }

        boolean rt=true;
        atributos atr=new atributos(nombre,tipo);
        if (fl) {
            for (Nodo a : raiz.hijos.get(2).hijos) {
                if(comprobarCaracteristicas(tl, a,atr))
                    rt=true;
                else
                {rt=false;break;}
            }
            
            if(rt){
               tl.atributos.addLast(atr);
               fl=true;
            }
            else
                fl=false;
        }else
            Control.agregarError(new errores("SEMANTICO", "Ya existe la atributo: "+nombre+" , en la Tabla: "+tl.nombre, raiz.fila, raiz.columna));
        return fl;
    }
    
    public boolean comprobarCaracteristicas(tabla tl,Nodo raiz,atributos atri){
        String nombre = raiz.nombre;
        switch(nombre){
            case "FK":
                return comprobarForeingKey(raiz,tl,atri);
            case "PK":
                if(!tl.primary)
                {
                    atri.primary_key=true;
                    tl.primary=true;
                    atri.nulo=true;
                    return true;
                }else
                { Control.agregarError(new errores("SEMANTICO", "Ya existe definida primaryKey en la Tabla: "+nombre, raiz.fila, raiz.columna));return false;}
            case "AUTO_INCREMENT":
                if("INTEGER".equals(atri.tipo))
                {   atri.auto_inc=true;
                    return true;
                }
                else
                {   Control.agregarError(new errores("SEMANTICO", "No es tipo INTEGER, El atributo: "+atri.nombre+"en la tabla: "+tl.nombre, raiz.fila, raiz.columna));
                    return false;}
            case "NOT NULL":
                if (atri.nulo == false) {
                    atri.nulo = true;
                    return true;
                }
                else
                {Control.agregarError(new errores("SINTACTICO", "Dos veces el NOT NULL en tabla: "+tl.nombre, raiz.fila, raiz.columna));  return false;}
            case "NULL":
                 if (atri.nulo == false) {
                    atri.nulo = false;
                    return true;
                }
                else
                  {Control.agregarError(new errores("SEMANTICO", "ya se definio el NOT NULL en tabla: "+tl.nombre, raiz.fila, raiz.columna));  return false;}
        }
        return false;
    }

    private boolean comprobarForeingKey(Nodo raiz, tabla tl, atributos atri) {
       String nombre = raiz.hijos.get(0).nombre;
       String atributo = raiz.hijos.get(1).nombre;
       
        try {
            tabla auxt = actual.tablas.get(nombre);
            if(auxt!=null){
                for(atributos atr:auxt.atributos){
                    if(atr.nombre.equals(atributo))
                    {atri.foreing_key=true; return true;}
                }
            }
        } catch (Exception e) {
            //no existe la tabla para hacer referencia
            return false;
        }
       return false;
    }

    private void crearObjeto(Nodo raiz) {
        String nombre = raiz.hijos.get(0).nombre;
        if(actual!=null){
            if(!actual.Objetos.containsKey(nombre)){
                Objetos obj = new Objetos(nombre);
                boolean fl=true;
                for(Nodo r:raiz.hijos.get(1).hijos){
                    if(!agregarAtributoObj(obj, r)){
                        fl=false;break;
                    }
                }
                
                if(fl)
                    actual.Objetos.put(nombre,obj);
            }
        }
    }
    
    public boolean agregarAtributoObj(Objetos atr,Nodo raiz){
        String nombre=raiz.hijos.get(1).nombre;
        if(!atr.atributos.containsKey(nombre)){
            atr.atributos.put(nombre,new Atributos_Obj(raiz.hijos.get(0).nombre, nombre));
            return true;
        }
        return false;
    }

    public void leerTabla(tabla tl){
        try {
            FileReader fr = null;
            String texto="";
            File maestro = new File(actual.direccion+actual.nombre+".usac");//el archivo registrodb_jj
            fr = new FileReader (maestro);
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null)
                texto+=linea;
            fr.close();
            escribirTabla(tl, texto);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ejecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ejecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void escribirTabla(tabla tl, String text){
          
        String cont="";
        
        cont+="<Tabla>\n\t"
                + "<nombre>"+ tl.nombre+"</nombre>\n"
                + "<path>"+actual.direccion+"TABLAS\\"+tl.nombre+".usac</path>\n"
                + "<rows>\n";
        for(atributos atr:tl.atributos){
            cont+="<"+atr.tipo+">"+atr.nombre+" ";
                    if(atr.primary_key||atr.auto_inc||atr.foreing_key||atr.nulo){
                         cont+="<crs>";
                        if(atr.primary_key)
                             cont+="PK ";
                        if(atr.foreing_key)
                             cont+="FK ";//AREGLAR A QUE TABLA HACER REFERENCIA
                        if(atr.auto_inc)
                             cont+="INC ";
                        if(atr.nulo)
                             cont+="NO NULO ";
                        else
                            cont+="NULO";
                        cont+="</crs>";
                    }
                       
                    cont+= "</"+atr.tipo+">\n";
        }
        cont+="</rows>\n";
        cont+="</Tabla>\n";
        
        try {
            FileWriter fichero = new FileWriter(actual.direccion+actual.nombre+".usac");
            // 
            fichero.write(text+cont);
            fichero.close();
            
            fichero = new FileWriter(actual.direccion+"\\TABLAS\\"+tl.nombre+".usac");//creamos el archivo de la tabla
            // 
            fichero.write("");
            fichero.close();//cerramos el escritor
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //actualizarBD(cont);//modificamos el xml
    }
    
    public void actualizarBD(String cont){
        //File directorio = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+base.nombre);
        String texto="";
        boolean fl=false;
        try {
            FileReader fr = null;
            
            File maestro = new File("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+actual.nombre+"\\"+actual.nombre+".usac");
            fr = new FileReader (maestro);
            BufferedReader br = new BufferedReader(fr);
            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null)
                texto+=linea;
            fr.close();
            fl=true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ejecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ejecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(fl){
             try {
            FileWriter fichero = new FileWriter("C:\\Users\\Jherson Sazo\\Documents\\COMPI2\\BASES\\"+actual.nombre+"\\"+actual.nombre+".usac");
            fichero.write(texto+cont);//creamos el archivo de la tabla
            fichero.close();//cerramos el escritor
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }

}
