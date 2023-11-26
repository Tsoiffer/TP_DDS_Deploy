package ar.utn.dds.Incidencia.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String nombre;
    Integer orden;

    protected Estado(){
        super();
    }

    public Estado (String nombre, Integer orden){
        this.nombre = nombre;
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {return orden;}

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public long getId() {
        return id;
    }

    public Boolean validarCambioEstado(Estado nuevoEstado){
        return (this.orden + 1 == nuevoEstado.getOrden() &&  this.orden != 4) ||
                (this.orden == 1 && nuevoEstado.getOrden() == 5);
    }
    /*
    public Boolean validarCambioEstado(Estado nuevoEstado){
        return (this.orden + 1 == nuevoEstado.getOrden() &&  this.nombre != "SOLUCIONADO") ||
                (this.nombre == "ASIGNADO" && nuevoEstado.getNombre() == "DESESTIMADO");
    }
    */

}
