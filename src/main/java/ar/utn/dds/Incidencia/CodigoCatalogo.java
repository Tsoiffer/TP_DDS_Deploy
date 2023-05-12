package ar.utn.dds.Incidencia;

import java.util.Objects;

public class CodigoCatalogo{
    String codigo;
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public CodigoCatalogo (String codigo){
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return  "codigo='" + codigo + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodigoCatalogo that = (CodigoCatalogo) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}