import ar.utn.dds.Incidencia.*;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
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
    Estados Reportado = Estados.REPORTADO;
    Estados Asignado = Estados.ASIGNADO;
    Estados Confirmado = Estados.CONFIRMADO;
    CodigoCatalogo codigoCatalogo = new CodigoCatalogo("4231-31");
    Operador operante = new Operador("pepote");
    Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  fecha , Estados.REPORTADO );
    Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio",  fecha , Estados.ASIGNADO );
    Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio",  fecha , Estados.ASIGNADO );
    Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio", Estados.ASIGNADO,  fecha , "Alguno" , operante, "Pepe" );

    public RepoIncidenciasTest() throws IncidenciaInvalidaException {
    }

    
    @Test
    public void testFindByEstado(){
        repoIncidencias.save(incidencia1);
        repoIncidencias.save(incidencia2);
        repoIncidencias.save(incidencia3);

        assertTrue(repoIncidencias.findByEstado(Estados.ASIGNADO).size() == 2,"Se encuentran dos inscidencias Asignadas");    //TODO cambiar el contructor
        assertTrue(repoIncidencias.findByEstado(Estados.CONFIRMADO).size() == 0,"No se encontro ninguna incidencia Confirmada");
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
        Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", Estados.REPORTADO,  LocalDate.parse("23/04/2023", formatEurope) , "Alguno" , operante, "OtroFulanito" );
        Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("21/04/2023", formatEurope) , "Alguno" , operante, "Fulanita" );
        Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("20/04/2023", formatEurope) , "Alguno" , operante, "ElHijoDeFulanita" );
        Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("24/04/2023", formatEurope) , "Alguno" , operante, "ElHijoDeFulanita" );

        repo.save(incidencia1);
        repo.save(incidencia2);
        repo.save(incidencia3);
        repo.save(incidencia4); //esta incidencia es la mas reciente.

        assertEquals(3, repo.listReciente(Estados.ASIGNADO).size());
        assertEquals(incidencia4.getCodigoCatalogo().getCodigo(), repo.listReciente(Estados.ASIGNADO).get(0).getCodigoCatalogo().getCodigo());

    }
    @Test
    public void testListarIncidenciasViejasAsignadas() throws IncidenciaInvalidaException {
        RepoIncidencias repo = new RepoIncidencias();
        Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", Estados.REPORTADO,  LocalDate.parse("23/04/2023", formatEurope) , "Alguno" , operante, "OtroFulanito" );
        Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("21/04/2023", formatEurope) , "Alguno" , operante, "Fulanita" );
        Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("20/04/2023", formatEurope) , "Alguno" , operante, "ElHijoDeFulanita" );
        Incidencia incidencia4 = new Incidencia(new CodigoCatalogo("1234-55"), "Escalera subte B estacion Pasteur fuera de servicio", Estados.ASIGNADO,  LocalDate.parse("10/04/2023", formatEurope) , "Alguno" , operante, "ElHijoDeFulanita" );

        repo.save(incidencia1);
        repo.save(incidencia2);
        repo.save(incidencia3);
        repo.save(incidencia4); //esta incidencia es la mas vieja.

        assertEquals(3, repo.listViejo(Estados.ASIGNADO).size());
        assertEquals(incidencia4.getCodigoCatalogo().getCodigo(), repo.listViejo(Estados.ASIGNADO).get(0).getCodigoCatalogo().getCodigo());

    }

}