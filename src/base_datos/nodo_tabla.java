
package base_datos;

/**
 *
 * @author Jherson Sazo
 */
public class nodo_tabla {
    public String tipo;//tipo para mientras va a ser nombre_atributo
    public Object valor;
    public boolean pk,fk,auto_inc,nulo;
    
    
    public nodo_tabla() {
    }

    public nodo_tabla(String tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.pk=this.fk=this.auto_inc=this.nulo=false;
    }
    
    
    
}
