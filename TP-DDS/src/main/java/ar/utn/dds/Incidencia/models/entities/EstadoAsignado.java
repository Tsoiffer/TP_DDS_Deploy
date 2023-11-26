package ar.utn.dds.Incidencia.models.entities;
import ar.utn.dds.Incidencia.models.entities.Estado;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EstadoAsignado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String fechaAsignacion;  //con este parametro mostraremos el avance de la incidencia
    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "estado_id")
    Estado estadoActual;


    public EstadoAsignado(Estado estado) {
        this.fechaAsignacion = LocalDateTime.now().toString();
        this.estadoActual = estado;
    }

    public EstadoAsignado(){
        super();
    }



    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public long getId() {
        return id;
    }


}
