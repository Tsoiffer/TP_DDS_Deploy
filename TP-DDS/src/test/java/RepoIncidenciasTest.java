import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.models.entities.Operador;
import org.junit.jupiter.api.Test;
import utils.ManejoCSV;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepoIncidenciasTest {
    RepoIncidencias repoIncidencias = new RepoIncidencias();
    private static DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
    LocalDate fecha = LocalDate.parse("15/04/2023", formatEurope);
    Estado reportado = new Estado("REPORTADO", 0);
    Estado asignado = new Estado("ASIGNADO", 1);
    Estado confirmado = new Estado("CONFIRMADO", 2);
    CodigoCatalogo codigoCatalogo = new CodigoCatalogo("4231-31");
    Operador operante = new Operador("pepote");
    Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  fecha , reportado );
    Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio",  fecha , asignado );
    Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  fecha , asignado );
    Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio", fecha, asignado, "Alguno" , operante, "Pepe" );

    public RepoIncidenciasTest() throws IncidenciaInvalidaException {
    }

    @Test
    public void testFindByEstado(){
        repoIncidencias.save(incidencia1);
        repoIncidencias.save(incidencia2);
        repoIncidencias.save(incidencia3);

        assertTrue(repoIncidencias.findByEstado(asignado).size() == 2,"Se encuentran dos inscidencias Asignadas");    //TODO cambiar el contructor
        assertTrue(repoIncidencias.findByEstado(confirmado).size() == 0,"No se encontro ninguna incidencia Confirmada");
    }

    @Test
    public void testCargarIncidenciasBien() throws IOException {
        String path = "src/test/resources/incidencias3.csv";
        RepoIncidencias repoBien = new RepoIncidencias();
        List<Incidencia> incidencias = ManejoCSV.csvAIncidencias(path);
        incidencias.stream().forEach( incidencia -> repoBien.save(incidencia));
        assertEquals(28,repoBien.count());
    }
    @Test
    public void testCargarIncidenciasMal() throws IOException {
        String path = "src/test/resources/incidencias1.csv";
        RepoIncidencias repoMal = new RepoIncidencias();
        List<Incidencia> incidencias = ManejoCSV.csvAIncidencias(path);
        incidencias.stream().forEach( incidencia -> repoMal.save(incidencia));
        assertEquals(2,repoMal.count());
    }

    @Test
    public void testListarIncidenciasPorLugar(){
        RepoIncidencias repo = new RepoIncidencias();
        repo.save(incidencia1);
        repo.save(incidencia2);
        repo.save(incidencia3);
        repo.save(incidencia4);

        assertEquals(3, repo.findByLugar(codigoCatalogo).size());


    }
    @Test
    public void testListarIncidenciasRecientesAsignadas() throws IncidenciaInvalidaException {
        RepoIncidencias repo = new RepoIncidencias();

        Estado reportado = new Estado("REPORTADO", 0);
        Estado asignado = new Estado("ASIGNADO", 1);

        Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", LocalDate.parse("23/04/2023", formatEurope), reportado, "Alguno" , operante, "OtroFulanito" );
        Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio",  LocalDate.parse("21/04/2023", formatEurope) , asignado, "Alguno" , operante, "Fulanita" );
        Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",   LocalDate.parse("20/04/2023", formatEurope) , asignado,"Alguno" , operante, "ElHijoDeFulanita" );
        Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio",   LocalDate.parse("24/04/2023", formatEurope) , asignado,"Alguno" , operante, "ElHijoDeFulanita" );

        repo.save(incidencia1);
        repo.save(incidencia2);
        repo.save(incidencia3);
        repo.save(incidencia4); //esta incidencia es la mas reciente.

        assertEquals(3, repo.listReciente(asignado).size());
        assertEquals(incidencia4.getCodigoCatalogo().getCodigo(), repo.listReciente(asignado).get(0).getCodigoCatalogo().getCodigo());

    }
    @Test
    public void testListarIncidenciasViejasAsignadas() throws IncidenciaInvalidaException {
        RepoIncidencias repo = new RepoIncidencias();
        Estado reportado = new Estado("REPORTADO", 0);
        Estado asignado = new Estado("ASIGNADO", 1);


        Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  LocalDate.parse("23/04/2023", formatEurope), reportado , "Alguno" , operante, "OtroFulanito" );
        Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio",  LocalDate.parse("21/04/2023", formatEurope), asignado , "Alguno" , operante, "Fulanita" );
        Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  LocalDate.parse("20/04/2023", formatEurope), asignado , "Alguno" , operante, "ElHijoDeFulanita" );
        Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio",  LocalDate.parse("10/04/2023", formatEurope), asignado , "Alguno" , operante, "ElHijoDeFulanita" );

        repo.save(incidencia1);
        repo.save(incidencia2);
        repo.save(incidencia3);
        repo.save(incidencia4); //esta incidencia es la mas vieja.

        assertEquals(3, repo.listViejo(asignado).size());
        assertEquals(incidencia4.getCodigoCatalogo().getCodigo(), repo.listViejo(asignado).get(0).getCodigoCatalogo().getCodigo());

    }

}
