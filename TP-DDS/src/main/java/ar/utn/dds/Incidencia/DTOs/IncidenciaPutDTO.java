package ar.utn.dds.Incidencia.DTOs;

public class IncidenciaPutDTO {
    public String id;
    public String ordenEstado;

    public IncidenciaPutDTO() {super();}

    public IncidenciaPutDTO(String id, String orden)
    {
        this.id = id;
        this.ordenEstado = orden;
    }

    public String getOrdenEstado() {return ordenEstado;}

    public void setOrdenEstado(String orden) {
        this.ordenEstado = orden;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
