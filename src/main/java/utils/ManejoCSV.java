package utils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import ar.utn.dds.Incidencia.CodigoCatalogo;
import ar.utn.dds.Incidencia.Estados;
import ar.utn.dds.Incidencia.Incidencia;
import ar.utn.dds.Incidencia.Operador;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

public class ManejoCSV {

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

    }}
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
                Estados estados = Estados.valueOf(incidenciasListada.get(3));
                Operador operador = new Operador(incidenciasListada.get(4));
                String personaRep = incidenciasListada.get(5);
                String motivo = incidenciasListada.get(6);

                Incidencia incidencia = null;
                try {
                    incidencia = new Incidencia(codCat, desc, fechaRep, estados, motivo, operador, personaRep);
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
