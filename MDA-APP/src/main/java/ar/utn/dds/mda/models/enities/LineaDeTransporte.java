package ar.utn.dds.mda.models.enities;

import javax.persistence.*;

@Entity

public class LineaDeTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String nombre;

    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "tipoMedioDeTransporte_id")
    TipoMedioDeTransporte tipoMedioDeTransporte;

    public LineaDeTransporte(String nombre, TipoMedioDeTransporte tipoMedioDeTransporte) {
        this.nombre = nombre;
        this.tipoMedioDeTransporte = tipoMedioDeTransporte;
    }

    public LineaDeTransporte() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoMedioDeTransporte getTipoMedioDeTransporte() {
        return tipoMedioDeTransporte;
    }

    public void setTipoMedioDeTransporte(TipoMedioDeTransporte tipoMedioDeTransporte) {
        this.tipoMedioDeTransporte = tipoMedioDeTransporte;
    }
    public long getId() {
        return id;
    }
}
