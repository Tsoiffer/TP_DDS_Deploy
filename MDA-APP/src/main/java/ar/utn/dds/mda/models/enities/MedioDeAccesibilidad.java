package ar.utn.dds.mda.models.enities;

import javax.persistence.*;

@Entity
public class MedioDeAccesibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String codigoDeCatalogo;

    boolean inaccesible;

    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "tipoDeMedidaDeAccesibilidad_id")
    TipoDeMedidaDeAccesibilidad tipoDeMedidaDeAccesibilidad;

    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "estacion_id")
    Estacion estacion;

    public MedioDeAccesibilidad(String codigoDeCatalogo, boolean inaccesible, TipoDeMedidaDeAccesibilidad tipoDeMedidaDeAccesibilidad, Estacion estacion) {
        this.codigoDeCatalogo = codigoDeCatalogo;
        this.inaccesible = inaccesible;
        this.tipoDeMedidaDeAccesibilidad = tipoDeMedidaDeAccesibilidad;
        this.estacion = estacion;
    }

    public MedioDeAccesibilidad() {
    }

    public String getCodigoDeCatalogo() {
        return codigoDeCatalogo;
    }

    public void setCodigoDeCatalogo(String codigoDeCatalogo) {
        this.codigoDeCatalogo = codigoDeCatalogo;
    }

    public boolean isInaccesible() {
        return inaccesible;
    }

    public void setInaccesible(boolean inaccesible) {
        this.inaccesible = inaccesible;
    }

    public TipoDeMedidaDeAccesibilidad getTipoDeMedidaDeAccesibilidad() {
        return tipoDeMedidaDeAccesibilidad;
    }

    public void setTipoDeMedidaDeAccesibilidad(TipoDeMedidaDeAccesibilidad tipoDeMedidaDeAccesibilidad) {
        this.tipoDeMedidaDeAccesibilidad = tipoDeMedidaDeAccesibilidad;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public long getId() {
        return id;
    }



    public void habilitar(){
        this.inaccesible = true;
    }
    public void deshabilitar(){
        this.inaccesible = false;
    }
}
