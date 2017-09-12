package ArbolAST;

import base_datos.Atributos_Obj;
import base_datos.Objetos;
import base_datos.atributos;
import base_datos.bd;
import base_datos.nodo_tabla;
import base_datos.procedimientos;
import base_datos.tabla;
import base_datos.usuario;
import base_datos.registro_tabla;
import consultas.consulta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jherson Sazo
 */
public class ejecutor {
 
    public bd actual=null;
    public usuario user=null;
    public HashMap<String,variable> lista_actual;
    public LinkedList< HashMap<String,variable>> ambito;
    
    public ejecutor() {
        ambito = new LinkedList<>();
        Control.iniciar();
        aumentarAmbito();//ambito global
    }
    
    public void aumentarAmbito(){
        
        ambito.addFirst(new HashMap<>());
        lista_actual=ambito.getFirst();
    }
    
    public void disminuirAmbito(){
        ambito.removeFirst();
        lista_actual=ambito.getFirst();
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
                break;
            case "PROCEDIMIENTO":
                crearProcedimiento(raiz);
                break;
            case "CALL_FUN":
                ejecutarLLamada(raiz);
                break;
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
                    //leerTabla(tl);//vamos a escribir el archivo
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
            case "UNICO":
                atri.unique=true;
                return true;
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

    private void crearProcedimiento(Nodo raiz) {
        String nombre = raiz.hijos.get(0).nombre;
        
        if(actual!=null){
            if(!actual.procs.containsKey(nombre)){
                procedimientos pr = new procedimientos(nombre);
                if(raiz.hijos.size()==3){
                    pr.params=raiz.hijos.get(1);
                    pr.sentencias=raiz.hijos.get(2);
                    //vienen parametros
                }else{
                    //no trae parametros
                    pr.sentencias=raiz.hijos.get(1);
                }
                actual.procs.put(nombre,pr);
            }
            else
                Control.agregarError(new errores("SEMANTICO", "YA EXISTE PROCEDIMIENTO: "+nombre, raiz.fila, raiz.columna));
        }
    }

    private Object ejecutarLLamada(Nodo raiz) {
        String nombre=raiz.hijos.get(0).nombre;
        if(raiz.hijos.size()==2){
            
        }else{
            procedimientos pr = actual.procs.get(nombre);
            //para probar aqui 
            sentenciasUSQL(pr.sentencias);
        }
        return null;
    }
    
    private void sentenciasUSQL(Nodo raiz){
        switch(raiz.nombre){
            case "SENTENCIAS":
                for(Nodo r:raiz.hijos){
                    sentenciasUSQL(r);
                }
                break;
            case "INSERTAR":
                ejecutarInsertar(raiz);
                break;
            case "INSERTAR_ESPECIAL":
                insertarEspecial(raiz);
                break;
            case "SELECCIONAR_*":
                seleccionarTodo(raiz);
                break;
            case "SELECCIONAR":
                seleccionarEspecial(raiz);
                break;
                    
        }
    }

    private void ejecutarInsertar(Nodo raiz) {
        String nombre = raiz.hijos.get(0).nombre;
        Nodo atri = raiz.hijos.get(1);

        tabla aux = null;
        if (actual.tablas.containsKey(nombre)) {//PRIMERO BUSCAR LA TABLA EN LA BASE DE DATOS ACTUAL
            aux = actual.tablas.get(nombre);//obtenemos la tabla

            if (aux.atributos.size() == atri.hijos.size()) {//COMPARAR EL NUMERO DE ATRIBUTOS QUE SE ESTAN INSERTANDO
                //VALORES NULOS/NO NULOS
                Nodo atr;
                int a = 0;
                boolean fl = true;
                LinkedList<atributos> valores = new LinkedList<>();
                for (atributos r : aux.atributos) { //comparar si son del tipo que fue asignado
                    atr = atri.hijos.get(a);
                    Object res = evaluarEXPRESION(atr);
                    if (res != null) {//si el valor de retorno no fue nolo
                        res = castear(r.tipo, res); //CASTEO DE LOS VALORES
                        if (res != null) {//se castea al valor que fue definido, null no son del mismo tipo
                            atributos nuevo = new atributos(r.nombre, r.tipo,res);
                            
                            nuevo.auto_inc=r.auto_inc;//copiamos las caracteristicas originales
                            nuevo.foreing_key=r.foreing_key;
                            nuevo.primary_key=r.primary_key;
                            nuevo.unique=r.unique;
                            valores.addLast(nuevo);
                        } else {
                            fl = false;
                            break;
                        }
                    } else {
                        fl = false;
                        break;
                    }
                    a++;
                }
                if (fl) {
                    //comparaciones para insertar en la tabla, primary_key, foreigen key.. etc;
                    ingresarRegistroTabla(aux, valores);
                }
                //EJECUTAR NODO EXP
            }

        }
    }
    
    private void ingresarRegistroTabla(tabla aux, LinkedList<atributos> valores) {
        boolean fl=true;
        registro_tabla nuevo = new registro_tabla();
        
        for(int a=0;a<valores.size();a++){
            atributos atr=valores.get(a);
            nodo_tabla nodo= new nodo_tabla(atr.nombre, atr.valor);
            if(aux.atributos.get(a).primary_key){
             if(buscarPrimary(aux.registros, valores.get(a))){
                    //no existe este registro
                    nodo.auto_inc=atr.auto_inc;
                    nodo.pk=true;
                }
            }else if(aux.atributos.get(a).unique){
                if(buscarUnique(aux.registros, valores.get(a)))
                    nodo.unique=true;
                else{
                    Control.agregarError(new errores("SEMANTICO","YA EXISTE REGISTRO CON RESTRICCION UNIQUE: "+nodo.valor,0,0));
                    return;
                }
            }
            else if(aux.atributos.get(a).foreing_key){
                //buscar la tabla a la que hace referencia
                nodo.fk=true;
                //ver si tambien tengo que poner aqui a que tabla hace referencia
            }
            nuevo.registro.add(nodo);//el nodo cumplio con todos los registros
        }
            aux.registros.add(nuevo);//SE AGREGA EL REGISTRO
    } 
     
    private boolean buscarPrimary(LinkedList<registro_tabla> registros, atributos primary){
         
         if(primary!=null){
             for(registro_tabla rg: registros){//vamos a recorrer todos los registros de la tabla
                 for(nodo_tabla nt:rg.registro){//vamos a recorrer todos los atributos del registro
                     if(nt.pk){
                         if(nt.valor.equals(primary.valor))//revisar si es primary.valor o solo primary
                             return false;
                         else
                             break;
                     }
                 }
             }
         }
         return true;
     }
    
    private boolean buscarUnique(LinkedList<registro_tabla> registros, atributos unique){
         
         if(unique!=null){
             for(registro_tabla rg: registros){//vamos a recorrer todos los registros de la tabla
                 for(nodo_tabla nt:rg.registro){//vamos a recorrer todos los atributos del registro
                     if(nt.tipo.equals(unique.nombre)){
                         if(nt.valor.equals(unique.valor))
                             return false;
                         else
                             break;
                     }
                 }
             }
         }
         return true;
     }

    
    //vamos a levantar el nodo EXP
    private Object evaluarEXPRESION(Nodo nodo)
        {
            //---------------------> Si tiene 3 hijos
            if (nodo.hijos.size()==3)
            {
                String operador = nodo.hijos.get(1).nombre;
                switch (operador)
                {
                    case "||":  return evaluarOR(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "&&":  return evaluarAND(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "==":  return evaluarIGUAL(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "!=":  return evaluarDIFERENTE(nodo.hijos.get(0), nodo.hijos.get(2));
                    case ">=":  return evaluarMAYORIGUAL(nodo.hijos.get(0), nodo.hijos.get(2));
                    case ">":   return evaluarMAYOR(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "<=":  return evaluarMENORIGUAL(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "<":   return evaluarMENOR(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "+":   return evaluarMAS(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "-":   return evaluarMENOS(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "*":   return evaluarPOR(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "/":   return evaluarDIVIDIR(nodo.hijos.get(0), nodo.hijos.get(2));
                    case "^":   return evaluarPOTENCIA(nodo.hijos.get(0), nodo.hijos.get(2));
                    default:    break;
                }
            }

            //---------------------> Si tiene 2 hijos
            /*if (nodo.hijos.size()==2)
            {
                if (nodo.hijos.get(0).nombre.equals("!"))
                    return evaluarNOT(nodo.hijos.get(1));

                if (nodo.nombre.Equals("CALLMET"))
                {
                    ejecutarLLamadasMetodos(nodo);
                    return retorn;
                }
            }*/

            //---------------------> Si tiene 1 hijo
            if (nodo.hijos.size()==1)
            {
                String termino = nodo.nombre;
                switch (termino)
                {
                    //case "EXP":     return evaluarEXPRESION(nodo.hijos.get(0));
                    case "id":      return evaluarID(nodo.hijos.get(0));
                    case "ID_ATR":  return evaluarID(nodo.hijos.get(0));
                    case "NOT":     return evaluarNOT(nodo.hijos.get(0));
                    case "NUM":     return evaluarNUMERO(nodo.hijos.get(0));
                    case "TEXT":    return nodo.hijos.get(0).nombre.replaceAll("\"", "").replaceAll("'","");
                    //case "CALL_FUN": return evaluarEXPRESION(nodo.hijos.get(0));
                    case "FALSE":   return false;
                    case "TRUE":    return true;
                    default:        break;
                }
            }
            //---------------------> Retorno por defecto
            return 1;
        }

    private Object evaluarNUMERO(Nodo val) {//ver si tengo que manejar solo un tipo de valor
        //if (val.nombre.contains(".")|| val.nombre.length()>10)
                return Double.parseDouble(val.nombre);
            //else
               // return Integer.parseInt(val.nombre);
    }

    private Object evaluarMAS(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der); 
        
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        if(val1==-1 || val2==-1){
            Control.agregarError(new errores("SEMANTICO","No se pueden sumar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        if(val1==1 || val2==1){
            return (String)uno + (String)dos;
        }else if( val1==4 || val2==4){
            Control.agregarError(new errores("SEMANTICO","No se pueden sumar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        else if( val1==2 || val2==2){
            try {
                Double x = (Double)castear("double", uno);
                Double y = (Double)castear("double", dos);
                return x+y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden sumar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else if( val1==3 || val2==3){
            try {
                int x = (int)castear("integer", uno);
                int y = (int)castear("integer", dos);
                return x+y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden sumar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else{
            try {
                return (boolean)uno || (boolean)dos;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden sumar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
    }

    private Object evaluarMENOS(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der); 
        
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        if(val1==-1 || val2==-1){
            Control.agregarError(new errores("SEMANTICO","No se pueden restar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        if(val1==1 || val2==1){
            return (String)uno + (String)dos;
        }else if( val1==4 || val2==4){
            Control.agregarError(new errores("SEMANTICO","No se pueden restar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        else if( val1==2 || val2==2){
            try {
                Double x = (Double)castear("double", uno);
                Double y = (Double)castear("double", dos);
                return x-y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden restar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else if( val1==3 || val2==3){
            try {
                int x = (int)castear("integer", uno);
                int y = (int)castear("integer", dos);
                return x+y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden restar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else{
                Control.agregarError(new errores("SEMANTICO","No se pueden restar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            
        }
    }

    private Object evaluarPOR(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der); 
        
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        if(val1==-1 || val2==-1){
            Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        if(val1==1 || val2==1){
            if(val1==4 || val2==4)
                return (String)uno + (String)dos;
            else{
              Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
              return null;  
            }
        }else if( val1==4 || val2==4){
            Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        else if( val1==2 || val2==2){
            try {
                Double x = (Double)castear("double", uno);
                Double y = (Double)castear("double", dos);
                return x*y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else if( val1==3 || val2==3){
            try {
                int x = (int)castear("integer", uno);
                int y = (int)castear("integer", dos);
                return x*y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else{
            try {
                return (boolean)uno && (boolean)dos;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden multiplicar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
    }

    private Object evaluarDIVIDIR(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der); 
        
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        if(val1==-1 || val2==-1){
            Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        if(val1==1 || val2==1){
            if(val1==4 || val2==4)
                return (String)uno + (String)dos;
            else{
              Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
              return null;  
            }
        }else if( val1==4 || val2==4){
            Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        else if( val1==2 || val2==2){
            try {
                Double x = (Double)castear("double", uno);
                Double y = (Double)castear("double", dos);
                return x*y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else if( val1==3 || val2==3){
            try {
                int x = (int)castear("integer", uno);
                int y = (int)castear("integer", dos);
                return x*y;
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else{
                Control.agregarError(new errores("SEMANTICO","No se pueden dividir los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
        }
    }

    private Object evaluarPOTENCIA(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der); 
        
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        if(val1==-1 || val2==-1){
            Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        if(val1==1 || val2==1){
            if(val1==4 || val2==4)
                return (String)uno + (String)dos;
            else{
              Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
              return null;  
            }
        }else if( val1==4 || val2==4){
            Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
            return null;
        }
        else if( val1==2 || val2==2){
            try {
                Double x = (Double)castear("double", uno);
                Double y = (Double)castear("double", dos);
                return Math.pow(x, y);
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else if( val1==3 || val2==3){
            try {
                int x = (int)castear("integer", uno);
                int y = (int)castear("integer", dos);
                return Math.pow(x, y);
            } catch (Exception e) {
                Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
            }
        }
        else{
                Control.agregarError(new errores("SEMANTICO","No se pueden potenciar los tipos: "+uno+","+dos,izq.fila,izq.columna));
                return null;
        }
    }

    
    /************************** seccion insertado especial ***********/
    
    private void insertarEspecial(Nodo raiz) {
        String nombre = raiz.hijos.get(0).nombre;
        Nodo atri=raiz.hijos.get(1);
        Nodo valores= raiz.hijos.get(2);
        
        LinkedList<atributos>requeridos;
        LinkedList<atributos>faltantes;
        
        tabla aux = null;
        if (actual.tablas.containsKey(nombre)) {//PRIMERO BUSCAR LA TABLA EN LA BASE DE DATOS ACTUAL
            aux = actual.tablas.get(nombre);
            requeridos=new LinkedList<>();
            faltantes=new LinkedList<>();
            
            if(atri.hijos.size()==valores.hijos.size()){
            //como comparar los atributos que necesito insertar etc...
            int cont=0;
            String n_atr = "";
            
            for(atributos a:aux.atributos) 
                faltantes.addLast(a); //sacar la lista de atributos faltantes
            
            for(Nodo r: atri.hijos){
                n_atr=r.hijos.get(0).nombre;
               for(atributos a:faltantes){
                   if(a.nombre.equals(n_atr))
                   {requeridos.addLast(a); faltantes.remove(a);break;}//sacar la lista de atributos requeridos
               }
            }
        }
            System.out.println("Jherson");
        
        //comparar los faltantes
        //que comparar?
        //si acepta valores nulos o es auto_increment
        boolean fl=true;
        for(atributos r:faltantes){
         if(r.auto_inc)   
             fl=true;
         else if(r.nulo)
            {fl=false;break;}
        }
            
        //comparamos si los valores faltantes fueron aceptados
         LinkedList<atributos> a_insert = new LinkedList<>();
        if(fl){
            //vamos a castear los valores a insertar
           int a=0;
           for(Nodo r:valores.hijos) {
               Object res = evaluarEXPRESION(r);
               atributos req  = requeridos.get(a);
                    if (res != null) {//si el valor de retorno no fue nolo
                        res = castear(req.tipo, res); //CASTEO DE LOS VALORES
                        if (res != null) {//se castea al valor que fue definido, null no son del mismo tipo
                            atributos nuevo = new atributos(req.nombre, req.tipo,res);
                            nuevo.auto_inc=req.auto_inc;//copiamos las caracteristicas originales
                            nuevo.foreing_key=req.foreing_key;
                            nuevo.primary_key=req.primary_key;
                            nuevo.unique=req.unique;
                            a_insert.addLast(nuevo);//ver en que momento ingreso el auto_increment
                        } else {
                            Control.agregarError(new errores("SEMANTICO","Error en el tipo de dato a insertar: "+res,raiz.fila,raiz.columna));;
                            return;
                        }
                    } else {
                       Control.agregarError(new errores("SEMANTICO","Error en obtencion de atributo cerca de: ",raiz.fila,raiz.columna));;
                       return;
                   } 
                    a++;
            }
        }
        
        
        //vamos a setearle los valores nulos o autoincrementables
        requeridos.clear();
            for (atributos h : faltantes) {
                if (h.auto_inc) {//ver si actualizo en la tabla tambien las cabeceras
                    atributos nuevo = new atributos(h.nombre, h.tipo, h.valor_increment);
                    nuevo.auto_inc = true;//copiamos las caracteristicas originales
                    nuevo.unique = h.unique;
                    requeridos.addLast(nuevo);
                    h.valor_increment++;
                }else{
                    atributos nuevo = new atributos(h.nombre, h.tipo, null);// mi clase null falta crearla
                    requeridos.addLast(nuevo);
                }
            }
        
        LinkedList<atributos> valores_insertar=new LinkedList<>();
        for(atributos a:aux.atributos){//ponemos en orden los atributos a insertar
            boolean fp=true;
            for(atributos h:a_insert){
                if(a.nombre.equals(h.nombre))
                {valores_insertar.addLast(h);fp=false; a_insert.remove(h); break;}
            }
            if(fp)
            for(atributos h:faltantes){
                if(a.nombre.equals(h.nombre))
                {valores_insertar.addLast(h);fp=false; faltantes.remove(h); break;}
            }
            if(fp)
            {Control.agregarError(new errores("SEMANTICO","Ocurrio un gran error en insertar especial",raiz.fila,raiz.columna)); return;}
        }
        
        ingresarRegistroTabla(aux,valores_insertar);
        }else
            Control.agregarError(new errores("SEMANTICO","Numero de parametros incorrectos: "+nombre,raiz.fila,raiz.columna));
    }

    private void seleccionarTodo(Nodo raiz) {
        //cargar las tablas que fueron seleccionadas
        Nodo tabla=raiz.hijos.get(0);
        
        LinkedList<tabla>cartesiano = new LinkedList<>();
        for(Nodo t:tabla.hijos){
            if(actual.tablas.containsKey(t.nombre)){
                tabla aux= actual.tablas.get(t.nombre);
                cartesiano.addLast(aux);
            }else{
                Control.agregarError(new errores("SEMANTICO", "NO existe tabla para realizar el select: "+t.nombre, t.fila, t.columna));
                return;
            }
        }
        consulta.productoCartesiano(cartesiano);
    }

    private void seleccionarEspecial(Nodo raiz) {
      //tengo los campos que requiero, pero primero debo hacer el cartesiano
      Nodo atributos=raiz.hijos.get(0);//nodo con los atributos requeridas
      Nodo tabla=raiz.hijos.get(1);//nodo con las tablas requeridas
        
        LinkedList<tabla> cartesiano = new LinkedList<>();
        for (Nodo t : tabla.hijos) {
            if (actual.tablas.containsKey(t.nombre)) {
                tabla aux = actual.tablas.get(t.nombre);
                cartesiano.addLast(aux);
            } else {
                Control.agregarError(new errores("SEMANTICO", "NO existe tabla para realizar el select: " + t.nombre, t.fila, t.columna));
                return;
            }
        }
        
        LinkedList<registro_tabla> resultado =consulta.productoCartesiano(cartesiano);
       
        LinkedList<registro_tabla> auxer = new LinkedList<>();
        if(raiz.hijos.size()==3){
            Nodo donde = raiz.hijos.get(2);
            //debo ejecutar donde
            //hacer un for por cada registro de la tabla
            //meter a la tabla de simbolos las variables actuales.....
            for(registro_tabla x:resultado){
                //llenar registro de nueva tabla
                aumentarAmbito();
                llenarVariables(x);
                Object res = evaluarEXPRESION(donde);
                try {
                    if((boolean)res){
                        auxer.addLast(x);//se cumplio la condicion :D
                    }
                } catch (Exception e) {
                    System.out.println("La expresion no se puede evaluar: "+e.getMessage());
                    return;
                }
                //vaciar registros de la tabla
            }
            //mandar a ejecutar nodo cond
            disminuirAmbito();
            resultado=auxer;
            //sacar las variables actuales
        }
        
        resultado= consulta.retornarConCampos(resultado, atributos);
        
        int a=0;
        for(registro_tabla t:resultado){
            for(nodo_tabla d: t.registro)
                System.out.print("| "+d.valor);
            System.out.println("| "+a);a++;
        } 
        
    }
    
    private Object castear(String tipo,Object valor){
        switch(tipo.toUpperCase()){
            case "DATE" :
                try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(String.valueOf(valor)); 
                    return date;
                } catch (ParseException e) {
                    System.out.println("No es de tipo date"+ e.getMessage());
                    return null;
                }
            case "DATETIME":
                try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = format.parse(String.valueOf(valor)); 
                    return date;
                } catch (ParseException e) {
                    System.out.println("No es de tipo date"+ e.getMessage());
                    return null;
                }
                
            case "TEXT":
                return String.valueOf(valor);
            case "INTEGER":
                try {
                     Double tmp=Double.parseDouble(valor.toString());
                     return tmp.intValue();
                } catch (NumberFormatException e) {
                    System.out.println("No se puede convertir a integer"+ e.getMessage());
                    return null;
                }
                case "DOUBLE":
                try {
                     return Double.parseDouble(String.valueOf(valor));
                } catch (NumberFormatException e) {
                    System.out.println("No se puede convertir a double"+ e.getMessage());
                    return null;
                }
        }
        return null;
    }

    private void llenarVariables(registro_tabla x) {
         for(nodo_tabla y : x.registro){
             variable var = new variable(y.tipo, y.tipo, y.valor);
             if(!lista_actual.containsKey(y.tipo)){
                 lista_actual.put(y.tipo, var);
             }
         }
    }

    private Object evaluarOR(Nodo izq, Nodo der) {
           try
            {
                boolean val1, val2;
                val1 = (boolean)(evaluarEXPRESION(izq));
                val2 = (boolean)(evaluarEXPRESION(der));
                return val1 || val2;
            }
            catch (Exception e)
            {
                System.out.println("Error al evaluar OR");
                return null;
            }
    }

    private Object evaluarAND(Nodo izq, Nodo der) {
        try
            {
                boolean val1, val2;
                val1 = (boolean)(evaluarEXPRESION(izq));
                val2 = (boolean)(evaluarEXPRESION(der));
                return val1 || val2;
            }
            catch (Exception e)
            {
                System.out.println("Error al evaluar and");
                return null;
            }
    }

    private Object evaluarIGUAL(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der);
        try {
            return uno.equals(dos);
        } catch (Exception e) {
            return null;
        }
    }

    private Object evaluarDIFERENTE(Nodo izq, Nodo der) {
        Object uno = evaluarEXPRESION(izq);
        Object dos = evaluarEXPRESION(der);
        try {
            return !uno.equals(dos);
        } catch (Exception e) {
            return null;
        }
    }

    private Object evaluarID(Nodo izq) {
        String nombre = izq.nombre;
        for(HashMap<String,variable> aux:ambito){
            if(aux.containsKey(nombre))
            {
                variable v = aux.get(nombre);
                return v.valor;
            }
        }
        return null;
    }

    private Object evaluarNOT(Nodo izq) {
        try {
            boolean val1 =(boolean)evaluarEXPRESION(izq);
            return val1;
        } catch (Exception e) {
            System.out.println("Error al evaluar NOT");
            return null;
        }
    }

    private Object evaluarMAYORIGUAL(Nodo izq, Nodo der) {
        Object uno = (evaluarEXPRESION(izq));
        Object dos = (evaluarEXPRESION(der));
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        
        if (val1 == val2) {
            switch (val1) {
                case 1:
                    val1=retornarAssci((String)uno);
                    val2=retornarAssci((String)dos);
                    return val1>= val2;
                case 2:
                    return (double)uno >= (double)dos;
                case 3:
                    return (int)uno >= (int)dos;
                case 4:
                    return Date.parse(String.valueOf( uno)) >= Date.parse(String.valueOf( dos));
                default:
                    return null;
            }
            
        }else if(val1==2|| val1==3||val2==2|| val2==3){
            uno = castear("double", uno);
            dos = castear("double", dos);
            try {
                return (double)uno >= (double)dos;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Control.agregarError(new errores("SEMANTICO", "No se puede comparar >= con los tipos: "+uno+","+dos, izq.fila, der.columna));
        return null;
    }

    private Object evaluarMAYOR(Nodo izq, Nodo der) {
        Object uno = (evaluarEXPRESION(izq));
        Object dos = (evaluarEXPRESION(der));
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        
        if (val1 == val2) {
            switch (val1) {
                case 1:
                    val1=retornarAssci((String)uno);
                    val2=retornarAssci((String)dos);
                    return val1> val2;
                case 2:
                    return (double)uno > (double)dos;
                case 3:
                    return (int)uno > (int)dos;
                case 4:
                    return Date.parse(String.valueOf( uno)) > Date.parse(String.valueOf( dos));
                default:
                    return null;
            }
            
        }else if(val1==2|| val1==3||val2==2|| val2==3){
            uno = castear("double", uno);
            dos = castear("double", dos);
            try {
                return (double)uno > (double)dos;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Control.agregarError(new errores("SEMANTICO", "No se puede comparar > con los tipos: "+uno+","+dos, izq.fila, der.columna));
        return null;
    }
    
    private Object evaluarMENOR(Nodo izq, Nodo der) {
        Object uno = (evaluarEXPRESION(izq));
        Object dos = (evaluarEXPRESION(der));
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        
        if (val1 == val2) {
            switch (val1) {
                case 1:
                    val1=retornarAssci((String)uno);
                    val2=retornarAssci((String)dos);
                    return val1< val2;
                case 2:
                    return (double)uno < (double)dos;
                case 3:
                    return (int)uno < (int)dos;
                case 4:
                    return Date.parse(String.valueOf( uno)) >= Date.parse(String.valueOf( dos));
                default:
                    return null;
            }
            
        }else if(val1==2|| val1==3||val2==2|| val2==3){
            uno = castear("double", uno);
            dos = castear("double", dos);
            try {
                return (double)uno < (double)dos;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Control.agregarError(new errores("SEMANTICO", "No se puede comparar < con los tipos: "+uno+","+dos, izq.fila, der.columna));
        return null;
    }
    
    private Object evaluarMENORIGUAL(Nodo izq, Nodo der) {
         Object uno = (evaluarEXPRESION(izq));
        Object dos = (evaluarEXPRESION(der));
        int val1 = retornarTipo(uno);
        int val2 = retornarTipo(dos);
        
        if (val1 == val2) {
            switch (val1) {
                case 1:
                    val1=retornarAssci((String)uno);
                    val2=retornarAssci((String)dos);
                    return val1<= val2;
                case 2:
                    return (double)uno <= (double)dos;
                case 3:
                    return (int)uno <= (int)dos;
                case 4:
                    return Date.parse(String.valueOf( uno)) <= Date.parse(String.valueOf( dos));
                default:
                    return null;
            }
            
        }else if(val1==2|| val1==3||val2==2|| val2==3){
            uno = castear("double", uno);
            dos = castear("double", dos);
            try {
                return (double)uno <= (double)dos;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Control.agregarError(new errores("SEMANTICO", "No se puede comparar <=con los tipos: "+uno+","+dos, izq.fila, der.columna));
        return null;
    }

    private int retornarAssci(String cad) {
        int res =0;
        for (int x=0;x<cad.length();x++)
            res+= cad.codePointAt(x);
        return res;
    }
     
    private int retornarTipo(Object a){
        if(a instanceof String)
            return 1;
        else if (a instanceof Double)
            return 2;
        else if (a instanceof Integer)
            return 3;
        else if (a instanceof Date)
            return 4;
        else if (a instanceof Boolean)
            return 5;
        
        return -1;
    }
    
    

}//fin clase ejecutor


