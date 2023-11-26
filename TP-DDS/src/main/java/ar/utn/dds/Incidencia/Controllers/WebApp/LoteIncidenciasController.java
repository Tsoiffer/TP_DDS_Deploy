package ar.utn.dds.Incidencia.Controllers.WebApp;

import ar.utn.dds.Incidencia.DTOs.ResponseMessageDTO;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.Repositorios.RepoLoteIncidencias;
import io.javalin.http.Context;
import utils.MQUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.openapi.*;

import java.util.HashMap;


public class LoteIncidenciasController {
    static EntityManagerFactory entityManagerFactory;
    static MQUtils publisher ;
    public LoteIncidenciasController(EntityManagerFactory emFactory, MQUtils mqutils){
        this.entityManagerFactory = emFactory;
        this.publisher = mqutils;
    }

    @OpenApi(
            summary = "Obtener todas los lotes de incidencias",
            path = "/loteIncidencias",
            methods = HttpMethod.GET,
            tags = {"LoteIncidencias"}
    )
    public static void getAllLoteIncidencias(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoLoteIncidencias repo = new RepoLoteIncidencias(entityManager);
        HashMap<String, Object> model = new HashMap<>();
        model.put("lotesIncidencias", repo.all());
        ctx.render("listado_loteIncidencias.hbs", model);
        entityManager.close();
    }

    @OpenApi(
            summary = "Obtener el estado de un lote de incidencias",
            path = "/loteIncidencias/{id}",
            methods = HttpMethod.GET,
            tags = {"LoteIncidencias"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID del lote de incidencias")}
    )
    public static void findById(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoLoteIncidencias repo = new RepoLoteIncidencias(entityManager);

        if(repo.findById(Long.valueOf(ctx.pathParam("id")))!=null){
            if(repo.findById(Long.valueOf(ctx.pathParam("id"))).isFinalizado())
            {
                ctx.json(new ResponseMessageDTO("0", "El lote de incidencias con código " + ctx.pathParam("id") + " ha finalizado. Informacion adicional:"
                        + "{Incidencias creadas: " + repo.findById(Long.valueOf(ctx.pathParam("id"))).getCantidadIncidencias() + "}"
                        + "{Incidencias erroneas: " + repo.findById(Long.valueOf(ctx.pathParam("id"))).getCantidadIncidenciasErroneas() + "}"
                ));
            }
            else
            {
                ctx.json(new ResponseMessageDTO("2", "El lote de incidencias con código " + ctx.pathParam("id") + " está en progreso."));
            }
        }
        else
            ctx.json("El lote de incidencias con id " + ctx.pathParam("id") + " no existe.");

        entityManager.close();
    }
}
