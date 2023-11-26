package ar.utn.dds.Incidencia.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CodigoCatalogo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    public CodigoCatalogo (){
        super();
    }

    @Override
    public boolean equals(Object o) { //TODO ver si es necesario
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodigoCatalogo that = (CodigoCatalogo) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    } //TODO ver si es necesario

    public long getId() {
        return id;
    }


}