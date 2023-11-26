package ar.utn.dds.Incidencia.DTOs;

public class ResponseMessageDTO {
    public ResponseMessageDTO() {
        super();
    }

    public ResponseMessageDTO(String codigo, String mensaje)
    {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String codigo;
    public String mensaje;
}
