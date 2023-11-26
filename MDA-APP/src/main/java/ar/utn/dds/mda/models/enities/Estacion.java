package ar.utn.dds.mda.models.enities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String nombreDeLaEstacion;

    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "lineaDeTransporte_id")
    LineaDeTransporte lineaDeTransporte;

    @OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="estacion_id")
    List<Estacion> combinacion;

    public Estacion(String nombreDeLaEstacion, LineaDeTransporte lineaDeTransporte, List<Estacion> combinacion) {
        this.nombreDeLaEstacion = nombreDeLaEstacion;
        this.lineaDeTransporte = lineaDeTransporte;
        this.combinacion = combinacion;
    }
    public Estacion(String nombreDeLaEstacion, LineaDeTransporte lineaDeTransporte) {
        this.nombreDeLaEstacion = nombreDeLaEstacion;
        this.lineaDeTransporte = lineaDeTransporte;
        this.combinacion = null;
    }

    public Estacion() {
    }

    public String getNombreDeLaEstacion() {
        return nombreDeLaEstacion;
    }

    public void setNombreDeLaEstacion(String nombreDeLaEstacion) {
        this.nombreDeLaEstacion = nombreDeLaEstacion;
    }

    public LineaDeTransporte getLineaDeTransporte() {
        return lineaDeTransporte;
    }

    public void setLineaDeTransporte(LineaDeTransporte lineaDeTransporte) {
        this.lineaDeTransporte = lineaDeTransporte;
    }

    public List<Estacion> getCombinacion() {
        return combinacion;
    }

    public void setCombinacion(List<Estacion> combinacion) {
        this.combinacion = combinacion;
    }

    public long getId() {
        return id;
    }
}
