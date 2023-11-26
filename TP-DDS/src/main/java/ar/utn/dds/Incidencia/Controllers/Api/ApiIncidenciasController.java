package ar.utn.dds.Incidencia.Controllers.Api;

import ar.utn.dds.Incidencia.DTOs.IncidenciaPostDTO;
import ar.utn.dds.Incidencia.DTOs.IncidenciaPutDTO;
import ar.utn.dds.Incidencia.DTOs.ResponseMessageDTO;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.Repositorios.RepoLoteIncidencias;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.models.entities.LoteIncidencias;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import io.javalin.http.NotFoundResponse;
import utils.MQUtils;
import io.javalin.openapi.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

public class ApiIncidenciasController {

    static EntityManagerFactory entityManagerFactory;
    static MQUtils publisher ;

    public ApiIncidenciasController(EntityManagerFactory emFactory, MQUtils mqutils){
        this.entityManagerFactory = emFactory;
        this.publisher = mqutils;
    }

    @OpenApi(
            summary = "Obtener incidencia por lugar",
            path = "API/incidencia/lugar/{lugar}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "lugar", description = "El lugar que se quiere consultar, retorna JSON.")}
    )
    public static void getByLugar(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        String nombreCodigoCatalogo = ctx.pathParam("lugar");
        entityManager.getTransaction().begin();
        CodigoCatalogo codigoCatalogo = (CodigoCatalogo) entityManager.createQuery("SELECT codCatalogo FROM CodigoCatalogo codCatalogo WHERE codigo=:codigoCatalogo").setParameter("codigoCatalogo",nombreCodigoCatalogo).getSingleResult();
        entityManager.getTransaction().commit();
        ctx.json(repo.findByLugar(codigoCatalogo));
        entityManager.close();
    }

    @OpenApi(
            summary = "Obtener un listado de incidencias ordenada por la más antigua",
            path = "API/incidencia/listViejas",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            queryParams = {@OpenApiParam(name = "estado", description = "Un estado en el que puede estar la incidencia")}
    )
    public static void getViejas(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        String nombreEstado = ctx.queryParam("estado");
        if(nombreEstado == null || nombreEstado  ==  ""  ){
            ctx.json(repo.listViejo());
        }else{
            entityManager.getTransaction().begin();
            Estado estado = (Estado) entityManager.createQuery("SELECT estado FROM Estado estado WHERE nombre=:nombreEstado").setParameter("nombreEstado", nombreEstado).getSingleResult();
            entityManager.getTransaction().commit();
            ctx.json(repo.listViejo(estado));
        }
        entityManager.close();
    }

    @OpenApi(
            summary = "Obtener un listado de incidencias según las más recientemente cargadas",
            path = "API/incidencia/listMasRecientes",
            methods = HttpMethod.GET,
            tags = {"Incidencia"}
    )
    public static void getMasRecientes(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        String nombreEstado = ctx.queryParam("estado");
        if(nombreEstado == null || nombreEstado  ==  "" ){
            ctx.json(repo.listReciente());
        }else{
            if(nombreEstado == null || nombreEstado  ==  "" ) {
                ctx.json(repo.listReciente());
            }else {
                entityManager.getTransaction().begin();
                Estado estado = (Estado) entityManager.createQuery("SELECT estado FROM Estado estado WHERE nombre=:nombreEstado").setParameter("nombreEstado", nombreEstado).getSingleResult();
                entityManager.getTransaction().commit();
                ctx.json(repo.listReciente(estado));
            }
        }
        entityManager.close();
    }

}
