package utils;

import ar.utn.dds.Incidencia.CodigoCatalogo;
import ar.utn.dds.Incidencia.Estados;
import ar.utn.dds.Incidencia.Incidencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManejoDataset {

    public static List<Incidencia> setearDatos(){
        List<Incidencia> dataset = new ArrayList<>();

        //Incidencias creadas temporalmente
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Primer incidente",LocalDate.of(2020,12,4), Estados.REPORTADO));
        dataset.add(new Incidencia(new CodigoCatalogo("2345-02"),"Segundo incidente",LocalDate.of(2021,11,10), Estados.ASIGNADO));
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Tercer incidente",LocalDate.of(2020,10,11), Estados.CONFIRMADO));
        dataset.add(new Incidencia(new CodigoCatalogo("4563-88"),"Cuarto incidente",LocalDate.of(2021,5,29), Estados.EN_PROGRESO));
        dataset.add(new Incidencia(new CodigoCatalogo("5369-32"),"Quinto incidente",LocalDate.of(2022,7,26), Estados.REPORTADO));
        dataset.add(new Incidencia(new CodigoCatalogo("6789-12"),"Sexto incidente",LocalDate.of(2020,10,6), Estados.EN_PROGRESO));
        dataset.add(new Incidencia(new CodigoCatalogo("7589-14"),"Septimo incidente",LocalDate.of(2021,12,21), Estados.ASIGNADO));
        dataset.add(new Incidencia(new CodigoCatalogo("5369-32"),"Octavo incidente",LocalDate.of(2022,8,8), Estados.DESESTIMADO));
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Noveno incidente",LocalDate.of(2023,4,10), Estados.REPORTADO));
        dataset.add(new Incidencia(new CodigoCatalogo("1015-25"),"Decimo incidente",LocalDate.of(2019,12,14), Estados.SOLUCIONADO));

        return dataset;
    }

}
