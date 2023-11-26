package ar.utn.dds.mda.models.enities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class TipoDeMedidaDeAccesibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String nombre;

    public TipoDeMedidaDeAccesibilidad(String nombre) {
        this.nombre = nombre;
    }

    public TipoDeMedidaDeAccesibilidad() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }
}
