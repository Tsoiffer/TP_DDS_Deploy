package ar.utn.dds.Incidencia;
import java.time.LocalDateTime;

public class Estado {
    String fechaAsignacion;  //con este parametro mostraremos el avance de la incidencia
    Estados estadoActual;
    public Estado(Estados estado) {
        this.fechaAsignacion = LocalDateTime.now().toString();
        this.estadoActual = estado;
    }

    public Estado(){

    }

    public Boolean validarCambioEstado(Estados nuevoEstado){
        return (this.estadoActual.ordinal() + 1 == nuevoEstado.ordinal() &&  this.estadoActual != Estados.SOLUCIONADO) ||
                (this.estadoActual == Estados.ASIGNADO && nuevoEstado == Estados.DESESTIMADO);
    }

    @Override
    public String toString() {
        return "Estado{" +
                "fechaAsignacion=" + fechaAsignacion +
                ", estadoActual=" + estadoActual +
                '}';
    }
}
