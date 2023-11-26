package utils;

import ar.utn.dds.Incidencia.Repositorios.RepoCodigoDeCatalogo;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.models.entities.Operador;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ManejoCSV {

    static ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv("MDA_API"));

public static List<Incidencia> leerCSV(File f, RepoIncidencias repoIncidencia, RepoEstado repoEstado, RepoCodigoDeCatalogo repoCodigoDeCatalogo) throws IOException, CsvValidationException {
    BufferedReader br = null;
    String line = "";
    List<Incidencia> incidencias = new ArrayList<>();
    DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy"); // TODO no lo estamos usando
    String cvsSplitBy = ",";
    //String cvsSplitBy = "\t";
    br = new BufferedReader(new FileReader(f));
    br.readLine();
    while ((line = br.readLine()) != null) {
        String[] datos = line.split(cvsSplitBy);
        if( datos[0] == "" | datos[1]== "" | datos[2] == "" | datos[3] == "" ){
            System.err.println( "Campos obligatorios sin completar.");
        }
        else {
            System.out.println("se carga la siguiente incidencia:");
            System.out.println(String.join("-",datos));


            Incidencia incidencia = new Incidencia();
            CodigoCatalogo codigoDelCSV = repoCodigoDeCatalogo.findByNombre(datos[0]);
            if( codigoDelCSV == null){
                incidencia.setCodigoCatalogo(new CodigoCatalogo(datos[0]));
            }else {
                incidencia.setCodigoCatalogo(codigoDelCSV);
            }
            if(consultador.validarCodigoCatalogoConMDA(incidencia.codigoCatalogo.getCodigo())){
                incidencia.setFechaDeReporte(datos[1]);
                incidencia.setDescripcion(datos[2]);
                Estado estado = repoEstado.findByName(datos[3]);
                incidencia.setEstadoAsignado(estado);
                //TODO Operador no tiene su repositorio para buscarlo y guargar su instancia, no podemos guardar el texto
                //if(datos[4] != "" ){ incidencia.setOperador(datos[4]);}
                //if(datos[5] != "" ){ incidencia.setPersonaQueLoReporto(datos[5]);}
                //TODO No tenemos Fecha de cierre, ¿es algo que calculamos con el esatdo?
                //if(datos[6] != "" ){ incidencia.setFechaDeCierre(datos[6]);}
                //if(datos[7] != "" ){ incidencia.setMotivoDeRechazo(datos[7]);}
                repoIncidencia.save(incidencia);
                if (estado.getOrden() == 1 || estado.getOrden() == 2 || estado.getOrden() == 3 )
                    consultador.modificarEstadoMDA(incidencia.codigoCatalogo.getCodigo(), "true");
                incidencias.add(incidencia);
            }else {
                System.err.println( "Codigo de Catalogo invalido.");
            }

        }
    }
    return incidencias;
}

public static  int contarLineasErroneasCSV(File f) throws IOException {
    BufferedReader br = null;
    String line = "";
    String cvsSplitBy = ",";
    int incidenciasErroneas = 0;
    br = new BufferedReader(new FileReader(f));
    br.readLine();
    while ((line = br.readLine()) != null) {
        String[] datos = line.split(cvsSplitBy);
        if (datos[0] == "" | datos[1] == "" | datos[2] == "" | datos[3] == "") {
            System.err.println("Linea Erronea: " + String.join(",",datos));
            incidenciasErroneas++;
        } else if ( !consultador.validarCodigoCatalogoConMDA(datos[0] ) ) {
            System.err.println("Linea Erronea: " + String.join(",",datos));
            incidenciasErroneas++;
        }
    }

    return incidenciasErroneas;
}

    public static List<List<String>> leerCSV(String path) throws IOException, CsvValidationException {
        List<List<String>> filas = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
        try (var fr = new FileReader(path, StandardCharsets.UTF_8);
             var reader = new CSVReaderBuilder(fr).withCSVParser(parser)
                     .build()) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<String> celdas = new ArrayList<>();
                for (var e : nextLine) {
                    celdas.add(e);
                }
                filas.add(celdas);
            }
            return filas;
        }
}

    public static List<Incidencia> csvAIncidencias(String directorio){
        List<List<String>> filas = null;
        List<Incidencia> incidencias = new ArrayList<>();
        try {
            filas = ManejoCSV.leerCSV( directorio);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ListIterator<List<String>> iteratorIncidencia = filas.listIterator();
        int lineasConError = 0;
        iteratorIncidencia.next();
        while(iteratorIncidencia.hasNext()) //Levanta todas las incidencias y las carga una por una.
        {
            DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
            List<String> incidenciasListada = iteratorIncidencia.next();
            if(
                    incidenciasListada.get(0) == "" | incidenciasListada.get(1) == "" | incidenciasListada.get(2) == "" | incidenciasListada.get(3) == ""
            ){
                System.err.println( String.format("campos obligatorios sin completar en linea %d", iteratorIncidencia.nextIndex() -1) );
                lineasConError +=1;
            }
            else {

                CodigoCatalogo codCat = new CodigoCatalogo(incidenciasListada.get(0));
                LocalDate fechaRep = LocalDate.parse(incidenciasListada.get(1), formatEurope);
                String desc = incidenciasListada.get(2);
                String nombreEstado = incidenciasListada.get(3);
                Estado estado = new Estado(nombreEstado,0); // TODO esto lo tienen q hacer llamando a la DB
                Operador operador = new Operador(incidenciasListada.get(4));
                String personaRep = incidenciasListada.get(5);
                String motivo = incidenciasListada.get(6);

                Incidencia incidencia = null;
                try {
                    incidencia = new Incidencia(codCat, desc, fechaRep, estado, motivo, operador, personaRep);
                } catch (IncidenciaInvalidaException e) {
                    System.err.println("Hubo un error cargando la incidencia: formato invalido");
                }
                incidencias.add(incidencia);
            }
        }
        if(lineasConError == 1 ) {
            System.err.println(String.format("Se cargo %d linea mal", lineasConError));
        }
        if(lineasConError >1 ) {
            System.err.println(String.format("Se cargaron %d lineas mal", lineasConError));
        }
        return incidencias;
    }

}
