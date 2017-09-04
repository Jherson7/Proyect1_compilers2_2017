package ArbolAST;

import base_datos.atributos;
import base_datos.bd;
import base_datos.tabla;
import base_datos.usuario;

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
        if(!Control.existeDB(nombre)){
           bd nueva = new  bd(nombre);
           Control.agregarBD(nueva);
        }
        //else
        //error ya existe base de datos
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
                tabla tl = new tabla(nombre);
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
                return comprobarForeingKey(raiz);
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

    private boolean comprobarForeingKey(Nodo raiz) {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void crearObjeto(Nodo raiz) {
        String nombre = raiz.hijos.get(0).nombre;
        if(actual!=null){
            //if(actual.)
        }
    }
}
