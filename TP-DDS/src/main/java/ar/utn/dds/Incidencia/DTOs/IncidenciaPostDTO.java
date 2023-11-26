package ar.utn.dds.Incidencia.DTOs;

public class IncidenciaPostDTO {
    public String getCodigoCatalogo() {
        return codigoCatalogo;
    }

    public void setCodigoCatalogo(String codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaDeReporte() {
        return fechaDeReporte;
    }

    public void setFechaDeReporte(String fechaDeReporte) {
        this.fechaDeReporte = fechaDeReporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String codigoCatalogo;
    public String descripcion;
    public String fechaDeReporte;
    public String estado;

    public IncidenciaPostDTO(String codigoCatalogo, String descripcion, String fechaDeReporte)
    {
        this.codigoCatalogo = codigoCatalogo;
        this.descripcion = descripcion;
        this.fechaDeReporte = fechaDeReporte;
    }
    public IncidenciaPostDTO(){
        super();
    }
}
