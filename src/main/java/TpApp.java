import ar.utn.dds.Incidencia.Incidencia;
import ar.utn.dds.Incidencia.RepoIncidencias;
import utils.ManejoCSV;
import utils.ManejoDataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TpApp {

    public static void main(String[]args){

        if(args.length == 0){
            System.err.println("Ingrese un path como parametro.");
            System.exit(1);
        }

        Path pathIncidencias = Paths.get(args[0]);
        if(!Files.exists(pathIncidencias)){
            System.err.println("El archivo indicado no existe");
            System.exit(1);
        }
        RepoIncidencias repositorio = new RepoIncidencias();
        List<Incidencia> incidencias = ManejoDataset.setearDatos();
        incidencias.stream().forEach( incidencia -> repositorio.save(incidencia));
        System.out.println(String.format("Se cargaron %d incidencias", repositorio.count()));




    }
}
