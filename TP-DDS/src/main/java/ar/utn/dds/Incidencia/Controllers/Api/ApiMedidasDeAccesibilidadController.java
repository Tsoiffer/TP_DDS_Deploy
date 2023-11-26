package ar.utn.dds.Incidencia.Controllers.Api;

import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.Repositorios.RepoLoteIncidencias;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiParam;
import utils.MQUtils;
import utils.ManejoConsultaApiMDA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ApiMedidasDeAccesibilidadController {
    static EntityManagerFactory entityManagerFactory;
    static MQUtils publisher ;
    public ApiMedidasDeAccesibilidadController(EntityManagerFactory emFactory, MQUtils mqutils){
        this.entityManagerFactory = emFactory;
        this.publisher = mqutils;
    }

    @OpenApi(
            summary = "Obtener las medidas de accesibilidad que se encuentran inaccesibles",
            path = "/medidasInaccesibles",
            methods = HttpMethod.GET,
            tags = {"MedidasInaccesibles"}
    )
    public static void getAllInaccesibles(Context ctx){
        ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv().get("MDA_API")); //TODO Usar variable de entorno
        ctx.json(consultador.filtrarMDAInaccesibles());
    }

    @OpenApi(
            summary = "Obtener las medidas de accesibilidad que se encuentran inaccesibles, filtrando por linea y estacion",
            path = "medidasInaccesibles/linea/{linea}/estacion/{estacion}", //TODO revisar
            methods = HttpMethod.GET,
            tags = {"MedidasInaccesibles"},
            pathParams = {@OpenApiParam(name = "linea", description = "La linea sobre la que se quiere conocer sus medidas de accesibilidad inaccesibles"),
                          @OpenApiParam(name = "estacion", description = "La estacion de una linea sobre la que se quiere conocer sus medidas de accesibilidad inaccesibles")}
    )
    public static void getByLugar(Context ctx){
        ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv().get("MDA_API")); //TODO Usar variable de entorno
        String linea = ctx.pathParam("linea");
        String estacion = ctx.pathParam("estacion");
        ctx.json(consultador.filtrarMDA(linea, estacion));
    }


}
