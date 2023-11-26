package ar.utn.dds.Incidencia.models.entities;

import ar.utn.dds.Incidencia.Repositorios.RepoCodigoDeCatalogo;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.io.FileUtils;
import utils.ManejoCSV;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Entity
public class LoteIncidencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    byte[] csv;

    boolean finalizado ;

    @OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="loteIncendencias_id")
    private List<Incidencia> listadoDeIncidencias = new ArrayList<>();

    int cantidadIncidencias = 0;
    int cantidadIncidenciasErroneas = 0;

    public LoteIncidencias(byte[] fileBytes){
        this.csv = fileBytes;
        this.finalizado = false;
    }

    public LoteIncidencias() {}

    public boolean procesarIncidencias(RepoIncidencias repoIncidencia, RepoEstado repoEstado, RepoCodigoDeCatalogo repoCodigoDeCatalogo) throws IOException, CsvValidationException {
        File destFile = new File("src/test/tmp");
        FileUtils.copyInputStreamToFile(new ByteArrayInputStream(this.csv) , destFile);
        //TODO: Generar un overload de ManejoCSV.leerCSV(File) con InputStream para evitar crear un archivo temporal. Issue #55
        this.listadoDeIncidencias = ManejoCSV.leerCSV(destFile,repoIncidencia, repoEstado,repoCodigoDeCatalogo);
        this.cantidadIncidenciasErroneas = ManejoCSV.contarLineasErroneasCSV(destFile);
        this.setCantidadIncidencias(listadoDeIncidencias.size());
        this.setFinalizado(TRUE);
        return TRUE;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getCsv() {
        return csv;
    }

    public void setCsv(byte[] csv) {
        this.csv = csv;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public List<Incidencia> getListadoDeIncidencias() {
        return listadoDeIncidencias;
    }

    public void setListadoDeIncidencias(List<Incidencia> listadoDeIncidencias) {
        this.listadoDeIncidencias = listadoDeIncidencias;
    }

    public int getCantidadIncidencias() {
        return cantidadIncidencias;
    }

    public void setCantidadIncidencias(int cantidadIncidencias) {
        this.cantidadIncidencias= cantidadIncidencias;
    }

    public int getCantidadIncidenciasErroneas() {
        return cantidadIncidenciasErroneas;
    }

    public void setCantidadIncidenciasErroneas(int cantidadIncidenciasErroneas) {
        this.cantidadIncidenciasErroneas = cantidadIncidenciasErroneas;
    }
}
