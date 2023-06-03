package ar.utn.dds.Incidencia.Controllers;
import ar.utn.dds.Incidencia.CodigoCatalogo;
import ar.utn.dds.Incidencia.Estados;
import ar.utn.dds.Incidencia.Incidencia;
import ar.utn.dds.Incidencia.RepoIncidencias;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.openapi.*;


public class IncidenciasController {
    static RepoIncidencias repo;
    public IncidenciasController( RepoIncidencias repo){
        IncidenciasController.repo = repo;
    }
    @OpenApi(
            summary = "Obtener todas las incidencias",
            path = "/incidencia",
            methods = HttpMethod.GET,
            tags = {"Incidencia"}
    )
    public static void getAll(Context ctx){
        ctx.json(repo.listReciente());
    }
    @OpenApi(
            summary = "Crear una incidencia",
            path = "/incidencia",
            methods = HttpMethod.POST,
            tags = {"Incidencia"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Incidencia.class)})
    )
    public static void create(Context ctx){
        ctx.json(repo.createIncidencia(ctx.bodyAsClass(Incidencia.class)));
    }
    @OpenApi(
            summary = "Editar una incidencia",
            path = "/incidencia/{id}",
            methods = HttpMethod.PUT,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID de la incidencia")},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = Incidencia.class)})
    )
    public static void edit(Context ctx) throws JsonProcessingException {
        ctx.json(repo.editIncidencia(ctx.pathParam("id"), ctx.body()));
    }
    @OpenApi(
            summary = "Obtener una incidencia en específico",
            path = "/incidencia/{id}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID de la incidencia")}
    )

    public static void getOne(Context ctx){
        ctx.json(repo.findByLugar(new CodigoCatalogo(ctx.pathParam("id"))));
    }
    @OpenApi(
            summary = "Eliminar una incidencia",
            path = "/incidencia/{id}",
            methods = HttpMethod.DELETE,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID de la incidencia a eliminar")}
    )
    public static void delete(Context ctx){
        ctx.json(repo.deleteIncidencia(ctx.pathParam("id")));
    }
    @OpenApi(
            summary = "Obtener incidencia por lugar",
            path = "/incidencia/lugar/{lugar}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "lugar", description = "El lugar que se quiere consultar")}
    )
    public static void getByLugar(Context ctx){
        ctx.json(repo.findByLugar(new CodigoCatalogo(ctx.pathParam("lugar"))));
    }
    @OpenApi(
            summary = "Obtener un listado de incidencias según las más recientemente cargadas",
            path = "/listMasRecientes",
            methods = HttpMethod.GET,
            tags = {"Incidencia"}
    )
    public static void getMasRecientes(Context ctx){
        if(ctx.queryParam("estado") == "" ){
            ctx.json(repo.listReciente());
        }else{
            String estado = ctx.queryParam("estado");
            ctx.json(repo.listReciente(Estados.valueOf(estado.toUpperCase())));
        }
    }
    @OpenApi(
            summary = "Obtener un listado de incidencias según su estado",
            path = "/estado/{estado}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El estado de la incidencia a buscar")}
    )
    public static void getFindByEstado(Context ctx){
        ctx.json(repo.findByEstado(Estados.valueOf(ctx.pathParam("estado"))));
    }
    @OpenApi(
            summary = "Obtener un listado de incidencias ordenada por la más antigua",
            path = "/listViejas",
            methods = HttpMethod.GET,
            tags = {"Incidencia"}
    )
    public static void getViejas(Context ctx){
        if(ctx.queryParam("estado") == "" ){
            ctx.json(repo.listViejo());
        }else{
            String estado = ctx.queryParam("estado");
            ctx.json(repo.listViejo(Estados.valueOf(estado.toUpperCase())));
        }
    }

}
