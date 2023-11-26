package utils;

import java.io.IOException;

public class ejemploConsumirApiMDAborrar {
    public static void main(String[] args) throws IOException {
        ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv("MDA_API")); //Usar variable de entorno
        System.out.println("-- GET: TODOS LOS MDA --");
        consultador.getAllMDA();
        System.out.println("-- GET: FILTRO LOS INACCESIBLES  --");
        consultador.filtrarMDA("A","LIMA");
        System.out.println("-- GET: TODOS LOS MDA INACCESIBBLES--");
        consultador.filtrarMDAInaccesibles();
        System.out.println("-- GET: UN MDA. Luego hago PUT--");
        consultador.validarCodigoCatalogoConMDA("SAA-001");
        System.out.println("-- PUT: MODIFICO UN ESTADO A INACCESIBLE--");
        consultador.modificarEstadoMDA("SAA-001", "falso");
        System.out.println("-- GET: UN MDA. Despues del PUT--");
        consultador.validarCodigoCatalogoConMDA("SAA-001");
    }

}
