package utils;

import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManejoDataset {

    public static List<Incidencia> setearDatos(){
        List<Incidencia> dataset = new ArrayList<>();

        Estado reportado = new Estado("REPORTADO", 0);
        Estado asignado = new Estado("ASIGNADO", 1);
        Estado confirmado = new Estado("CONFIRMADO", 2);
        Estado enProgreso = new Estado("EN_PROGRESO", 3);
        Estado solucionado = new Estado("SOLUCIONADO", 4);
        Estado desestimado = new Estado("DESESTIMADO", 5);
        //Incidencias creadas temporalmente
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Primer incidente",LocalDate.of(2020,12,4), reportado));
        dataset.add(new Incidencia(new CodigoCatalogo("2345-02"),"Segundo incidente",LocalDate.of(2021,11,10), asignado));
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Tercer incidente",LocalDate.of(2020,10,11), confirmado));
        dataset.add(new Incidencia(new CodigoCatalogo("4563-88"),"Cuarto incidente",LocalDate.of(2021,5,29), enProgreso));
        dataset.add(new Incidencia(new CodigoCatalogo("5369-32"),"Quinto incidente",LocalDate.of(2022,7,26), reportado));
        dataset.add(new Incidencia(new CodigoCatalogo("6789-12"),"Sexto incidente",LocalDate.of(2020,10,6), enProgreso));
        dataset.add(new Incidencia(new CodigoCatalogo("7589-14"),"Septimo incidente",LocalDate.of(2021,12,21), asignado));
        dataset.add(new Incidencia(new CodigoCatalogo("5369-32"),"Octavo incidente",LocalDate.of(2022,8,8), desestimado));
        dataset.add(new Incidencia(new CodigoCatalogo("1234-00"),"Noveno incidente",LocalDate.of(2023,4,10), reportado));
        dataset.add(new Incidencia(new CodigoCatalogo("1015-25"),"Decimo incidente",LocalDate.of(2019,12,14), solucionado));

        return dataset;
    }

}
