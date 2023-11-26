package ar.utn.dds.Incidencia.DTOs;

public class MedidaDeAccesibilidadDTO {
    public String linea;
    public String estacion;
    public String codigoDeCatalogo;

    public MedidaDeAccesibilidadDTO(){super();}

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getCodigoDeCatalogo() {
        return codigoDeCatalogo;
    }

    public void setCodigoDeCatalogo(String codigoDeCatalogo) {
        this.codigoDeCatalogo = codigoDeCatalogo;
    }
}
