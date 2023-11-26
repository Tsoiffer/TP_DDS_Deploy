package ar.utn.dds.Incidencia.Controllers.WebApp;

import ar.utn.dds.Incidencia.DTOs.IncidenciaPostDTO;
import ar.utn.dds.Incidencia.DTOs.IncidenciaPutDTO;
import ar.utn.dds.Incidencia.DTOs.MedidaDeAccesibilidadDTO;
import ar.utn.dds.Incidencia.DTOs.ResponseMessageDTO;
import ar.utn.dds.Incidencia.Repositorios.RepoCodigoDeCatalogo;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.Repositorios.RepoLoteIncidencias;
import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.models.entities.LoteIncidencias;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import io.javalin.http.NotFoundResponse;
import utils.MQUtils;
import io.javalin.openapi.*;
import utils.ManejoConsultaApiMDA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class IncidenciasController {
    static EntityManagerFactory entityManagerFactory;
    static MQUtils publisher ;

    static ManejoConsultaApiMDA consultador = new ManejoConsultaApiMDA(System.getenv("MDA_API"));
    public IncidenciasController(EntityManagerFactory emFactory, MQUtils mqutils){
        this.entityManagerFactory = emFactory;
        this.publisher = mqutils;
    }

    @OpenApi(
            summary = "Obtener todas las incidencias",
            path = "/incidencia",
            methods = HttpMethod.GET,
            tags = {"Incidencia"}
    )
    public static void getAll(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        RepoIncidencias repo = new RepoIncidencias(entityManager);
        //ctx.json(repo.listTodo()); Commented: Esto solo retonaba el json en la req.
        HashMap<String, Object> model = new HashMap<>();
        String req_results = new ManejoConsultaApiMDA(System.getenv("MDA_API")).getAllMDA();
        List<MedidaDeAccesibilidadDTO> mda = ManejoConsultaApiMDA.fromJsonArray(req_results);
        model.put("incidencias", repo.listTodo());
        model.put("mda", mda);
        ctx.render("listado_incidencias.hbs", model);
        entityManager.close();
    }

    @OpenApi(
            summary = "Crear una incidencia",
            path = "/incidencia",
            methods = HttpMethod.POST,
            tags = {"Incidencia"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = IncidenciaPostDTO.class)})
    )
    public static void create(Context ctx){
        if (ctx.status().equals(HttpStatus.UNAUTHORIZED)){
            ctx.json(new ResponseMessageDTO("1", "Debe loguearse para realizar esta acción."));
            return;
        }
        if (ctx.sessionAttribute("currentUser").equals("UsuarioOperador")){
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new ResponseMessageDTO("1", "El usuario no está autorizado a realizar esta acción."));
            return;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        RepoEstado repoEstado = new RepoEstado(entityManager);
        RepoCodigoDeCatalogo repoCodigoDeCatalogo = new RepoCodigoDeCatalogo(entityManager);
        Incidencia incidencia = new Incidencia();
        if(ctx.formParam("CodigoCatalogo") == "" || ctx.formParam("CodigoCatalogo") == null ){
            System.out.println(new CodigoCatalogo(ctx.bodyAsClass(IncidenciaPostDTO.class).getCodigoCatalogo()));
            CodigoCatalogo codigoDeLaRequest = repoCodigoDeCatalogo.findByNombre(ctx.bodyAsClass(IncidenciaPostDTO.class).getCodigoCatalogo());
            if( codigoDeLaRequest == null){
                incidencia.setCodigoCatalogo(new CodigoCatalogo(ctx.bodyAsClass(IncidenciaPostDTO.class).getCodigoCatalogo()));
            }else {
                incidencia.setCodigoCatalogo(codigoDeLaRequest);
            }
            System.out.println(ctx.bodyAsClass(IncidenciaPostDTO.class).getDescripcion());
            incidencia.setDescripcion(ctx.bodyAsClass(IncidenciaPostDTO.class).getDescripcion());
            System.out.println(ctx.bodyAsClass(IncidenciaPostDTO.class).getFechaDeReporte());
            incidencia.setFechaDeReporte(ctx.bodyAsClass(IncidenciaPostDTO.class).getFechaDeReporte());
            System.out.println(incidencia.getFechaDeReporte());
            incidencia.setEstadoAsignado(repoEstado.findByOrden(0));

        }else {
            System.out.println(ctx.formParam("CodigoCatalogo"));
            CodigoCatalogo codigoDeLaRequest = repoCodigoDeCatalogo.findByNombre(ctx.formParam("CodigoCatalogo"));
            if( codigoDeLaRequest == null){
                incidencia.setCodigoCatalogo( new CodigoCatalogo(ctx.formParam("CodigoCatalogo")) );
            }else {
                incidencia.setCodigoCatalogo(codigoDeLaRequest);
            }
            System.out.println(ctx.formParam("Descripcion"));
            incidencia.setDescripcion(ctx.formParam("Descripcion"));
            System.out.println(ctx.formParam("FechaDeReporte"));
            incidencia.setFechaDeReporte(ctx.formParam("FechaDeReporte"));
            incidencia.setEstadoAsignado(repoEstado.findByOrden(0));
        }
        if(consultador.validarCodigoCatalogoConMDA(incidencia.codigoCatalogo.getCodigo())){
            if (repo.createIncidencia(incidencia) == true)
                ctx.json(new ResponseMessageDTO("0", "Se creó correctamente la nueva incidencia con ID "+ incidencia.getId() + "."));
            else
                ctx.json(new ResponseMessageDTO("1", "No se pudo crear la incidencia. Falta un campo obligatorio."));
        }
        else{
            ctx.json(new ResponseMessageDTO("1", "El codigo de catalogo no existe."));
        }
        entityManager.close();
    }

    @OpenApi(
            summary = "Crear incidencias a partir de un archivo CSV",
            path = "/incidencia/cargaMasiva",
            methods = HttpMethod.POST,
            tags = {"LoteIncendencias"}
    )
    public static void createMany(Context ctx) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        RepoLoteIncidencias repo = new RepoLoteIncidencias(entityManager);
        RepoEstado repoEstado = new RepoEstado(entityManager);
        RepoIncidencias repoIncidencias = new RepoIncidencias(entityManager);

        UploadedFile csv = ctx.uploadedFile("file");
        if (csv != null) {
            try {
                byte[] fileBytes = csv.content().readAllBytes();
                LoteIncidencias loteIncidencias = new LoteIncidencias(fileBytes);
                repo.save(loteIncidencias);
                publisher.publish( Long.toString(loteIncidencias.getId()) );
                ctx.json(new ResponseMessageDTO("0","Se finalizó la carga del archivo CSV. El ID del lote de incidencias es: " + loteIncidencias.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ctx.json(new ResponseMessageDTO("1","No se recibió el archivo CSV."));
        }
        entityManager.close();
    }

    @OpenApi(
            summary = "Editar una incidencia",
            path = "/incidencia",
            methods = HttpMethod.PUT,
            tags = {"Incidencia"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = IncidenciaPutDTO.class)})
    )
    //Asi estaba
    public static void edit(Context ctx) throws IOException {
        if (ctx.status().equals(HttpStatus.UNAUTHORIZED)){
            ctx.json(new ResponseMessageDTO("1", "Debe loguearse para realizar esta acción."));
            return;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        RepoEstado repoEstado = new RepoEstado(entityManager);
        long idIncidencia = Long.valueOf(ctx.formParam("ID"));
        int orden = Integer.valueOf(ctx.formParam("Orden"));
        String nuevaDescripcion = ctx.formParam("Descripcion");
        //Hago un getIncidencia, me traigo los datos de la incidencia solicitada.
        Incidencia incidencia = repo.getIncidencia(idIncidencia);
        //Si el transportista quiere cambiar el estado, devuelve UNAUTHORIZED.
        //Si el operador quiere cambiar la descripcion, devuelve UNAUTHORIZED.
        //Si no ocurre ninguna de las dos, pasa OK.
        if ((ctx.sessionAttribute("currentUser").equals("UsuarioTransporte") && !(incidencia.estadoAsignado.getOrden().equals(orden)))
            || (ctx.sessionAttribute("currentUser").equals("UsuarioOperador") && !(nuevaDescripcion.equals(incidencia.getDescripcion())))){
            ctx.json(new ResponseMessageDTO("1", "El usuario no está autorizado a realizar esta acción."));
            ctx.status(HttpStatus.UNAUTHORIZED);
        } else {
            Estado nuevoEstado = repoEstado.findByOrden(orden);
            if (repo.editIncidencia(incidencia, nuevoEstado, nuevaDescripcion)) {
                if (nuevoEstado.getOrden() == 1)
                    consultador.modificarEstadoMDA(incidencia.codigoCatalogo.getCodigo(), "true");
                else if ((nuevoEstado.getOrden() == 5 || nuevoEstado.getOrden() == 4) && !repo.findIncidenciasAbiertas(incidencia.codigoCatalogo))
                    consultador.modificarEstadoMDA(incidencia.codigoCatalogo.getCodigo(), "false");

                ctx.json(new ResponseMessageDTO("0", "La incidencia ha sido modificada."));
            } else {
                ctx.json(new ResponseMessageDTO("1", "La incidencia no pudo ser modificada."));
            }
        }
        entityManager.close();
    }

    @OpenApi(
            summary = "Obtener una incidencia en específico",
            path = "/incidencia/{id}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID de la incidencia")}
    )
    public static void getOne(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);

        if(repo.findById(Long.valueOf(ctx.pathParam("id")))!=null){
            ctx.json(repo.findById(Long.valueOf(ctx.pathParam("id"))));
            HashMap<String, Object> model = new HashMap<>();
            model.put("incidencia", repo.findById(Long.valueOf(ctx.pathParam("id"))));
            ctx.render("incidencia.hbs", model);
        }
        else
            ctx.json("La incidencia con id " + ctx.pathParam("id") + " no existe.");
        entityManager.close();
    }

    @OpenApi(
            summary = "Eliminar una incidencia",
            path = "/incidencia/{id}",
            methods = HttpMethod.DELETE,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "id", description = "El ID de la incidencia a eliminar")}
    )
    public static void delete(Context ctx) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        Incidencia incidencia = repo.findById( Long.valueOf(ctx.pathParam("id")) );
        if(repo.deleteIncidencia(Long.valueOf(ctx.pathParam("id")))){
            if ( !repo.findIncidenciasAbiertas(incidencia.codigoCatalogo))
                consultador.modificarEstadoMDA(incidencia.codigoCatalogo.getCodigo(), "false");
            ctx.json(new ResponseMessageDTO("0","El registro " + ctx.pathParam("id") + " se eliminó correctamente."));
        }
        else{
            ctx.json(new ResponseMessageDTO("1", "No se pudo eliminar la incidencia con código " + ctx.pathParam("id") + "."));
        }

        entityManager.close();
    }


    @OpenApi(
            summary = "Obtener un listado de incidencias según su estado",
            path = "/incidencia/estado/{estado}",
            methods = HttpMethod.GET,
            tags = {"Incidencia"},
            pathParams = {@OpenApiParam(name = "estado", description = "El estado de la incidencia a buscar")}
    )
    public static void getFindByEstado(Context ctx){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoIncidencias repo = new RepoIncidencias(entityManager);
        String nombreEstado = ctx.pathParam("estado");
        Estado estado = (Estado) entityManager.createQuery("SELECT estado FROM Estado estado WHERE nombre=:nombreEstado").setParameter("nombreEstado", nombreEstado).getSingleResult();
        ctx.json(repo.findByEstado(estado));
        entityManager.close();
    }


}
