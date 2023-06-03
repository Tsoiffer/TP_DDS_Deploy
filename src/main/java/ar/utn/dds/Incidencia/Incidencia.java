package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ar.utn.dds.Incidencia.Estados.REPORTADO;

public class Incidencia {
    CodigoCatalogo codigoCatalogo;
    String descripcion;
    Estado estado;
    private List<Estado> estadosAnteriores = new ArrayList<>(); //se tutilizara para armar la linea de tiempo
    String fechaDeReporte;
    String motivoDeRechazo;
    Operador operador;
    String personaQueLoReporto;

    public Incidencia(){
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

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion, LocalDate fechaDeReporte, Estados estado) {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        this.estado = new Estado(estado);
    }

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion, LocalDate fechaDeReporte) {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        this.estado = new Estado(Estados.REPORTADO);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Incidencia(CodigoCatalogo codigoCatalogo, String descripcion,
                      LocalDate fechaDeReporte, Estados estado, String motivoDeRechazo,
                      Operador operador, String personaQueLoReporto) throws IncidenciaInvalidaException {
        if(codigoCatalogo == null || fechaDeReporte == null || descripcion == null || estado == null){
            throw new IncidenciaInvalidaException("Incidencia no valida, complete campos no opcionales");

        }
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte.toString();
        this.motivoDeRechazo = motivoDeRechazo;
        this.operador = operador;
        this.personaQueLoReporto = personaQueLoReporto;
        this.estado = new Estado(estado);
    }

    public Estados getEstado() {
        return estado.estadoActual;
    }

    public void cambiarEstado(Estados nuevoEstado) {
        if(estado.validarCambioEstado(nuevoEstado)){
            estadosAnteriores.add(this.estado);
            this.estado = new Estado(nuevoEstado);
        }
    }
    public String getFechaDeReporte(){
        return this.fechaDeReporte;
    }
    public CodigoCatalogo getCodigoCatalogo(){
        return this.codigoCatalogo;
    }

    @Override
    public String toString() {
        return "Incidencia\n" +
                "codigoCatalogo=" + codigoCatalogo + "\n" +
                "descripcion='" + descripcion + "\n" +
                "estado=" + estado + "\n" +
                "fechaDeReporte=" + fechaDeReporte +"\n" +
                "motivoDeRechazo='" + motivoDeRechazo + '\'' +"\n" +
                "operador=" + operador + "\n" +
                "personaQueLoReporto='" + personaQueLoReporto + '\'' +"\n";
    }
}
