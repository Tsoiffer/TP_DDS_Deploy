package ar.utn.dds.Incidencia.models.entities;

import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "codigoCatalogo_id")
    public CodigoCatalogo codigoCatalogo;
    public String descripcion;
    @OneToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "estado_id")
    public Estado estadoAsignado;
    @OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="incidencia_id")
    private List<EstadoAsignado> listadoDeEstados = new ArrayList<>(); //se utiliza para armar la linea de tiempo
    public String fechaDeReporte;
    public String motivoDeRechazo;
    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "operador_id")
    public Operador operador;
    public String personaQueLoReporto;

    public Incidencia(){
        super();
    }

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion, LocalDate fechaDeReporte, Estado estado) {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        this.estadoAsignado = estado;
        this.listadoDeEstados.add(new EstadoAsignado(estado));
    }
    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion, String fechaDeReporte, Estado estado) {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte;
        this.estadoAsignado = estado;
        this.listadoDeEstados.add(new EstadoAsignado(estado));
    }

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion, LocalDate fechaDeReporte) {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        Estado estadoRepotado = new Estado("REPORTADO",0);
        this.estadoAsignado = estadoRepotado;
    }

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion,
                      LocalDate fechaDeReporte, Estado estado, String motivoDeRechazo,
                      Operador operador, String personaQueLoReporto) throws IncidenciaInvalidaException {
        if(codigoCatalogo == null || fechaDeReporte == null || descripcion == null || estado == null){
            throw new IncidenciaInvalidaException("Incidencia no valida, complete campos no opcionales.");

        }
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        this.motivoDeRechazo = motivoDeRechazo;
        this.operador = operador;
        this.personaQueLoReporto = personaQueLoReporto;
        this.estadoAsignado = estado;
        this.listadoDeEstados.add(new EstadoAsignado(estado));
    }

    public String getMotivoDeRechazo() {
        return motivoDeRechazo;
    }

    public void setMotivoDeRechazo(String motivoDeRechazo) {
        this.motivoDeRechazo = motivoDeRechazo;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public String getPersonaQueLoReporto() {
        return personaQueLoReporto;
    }

    public void setPersonaQueLoReporto(String personaQueLoReporto) {
        this.personaQueLoReporto = personaQueLoReporto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstadoAsignado() {
        return this.estadoAsignado;
    }

    public void setEstadoAsignado(Estado estadoAsignado) {
        this.estadoAsignado = estadoAsignado;
    }

    public String getFechaDeReporte(){
        return this.fechaDeReporte;
    }

    public void setFechaDeReporte(String fechaDeReporte) {
        this.fechaDeReporte = fechaDeReporte;
    }

    public CodigoCatalogo getCodigoCatalogo(){
        return this.codigoCatalogo;
    }

    public void setCodigoCatalogo(CodigoCatalogo codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    public List<EstadoAsignado> getListadoDeEstados() {
        return listadoDeEstados;
    }

    public void setListadoDeEstados(List<EstadoAsignado> listadoDeEstados) {
        this.listadoDeEstados = listadoDeEstados;
    }

    public long getId() {
        return id;
    }

    public void cambiarEstado(Estado nuevoEstado) {
        if(this.estadoAsignado.validarCambioEstado(nuevoEstado)){
            this.listadoDeEstados.add(new EstadoAsignado(nuevoEstado));
            this.estadoAsignado = nuevoEstado;
        }
    }
}
