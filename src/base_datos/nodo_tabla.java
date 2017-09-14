
package base_datos;

/**
 *
 * @author Jherson Sazo
 */
public class nodo_tabla {
    public String tipo;//tipo para mientras va a ser nombre_atributo
    public String nombre;
    public Object valor;
    public boolean pk,fk,auto_inc,nulo,unique;
    public String foreing;
    public String tabla;
    
    public nodo_tabla() {
    }

    public nodo_tabla(String tipo, String nombre, Object valor,String tabla) {
        this.tipo = tipo;
        this.valor = valor;
        this.nombre=nombre;
        this.pk=this.fk=this.auto_inc=this.nulo=this.unique=false;
        this.foreing="";
        this.tabla=tabla;
    }
    
    
    
}
