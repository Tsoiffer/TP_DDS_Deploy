package ar.utn.dds.mda.Controllers;


import ar.utn.dds.mda.DTOs.MDAPutDTO;
import ar.utn.dds.mda.Repositorios.RepoMedioDeAccesibilidad;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.utn.dds.mda.models.enities.MedioDeAccesibilidad;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import io.javalin.http.NotFoundResponse;
import io.javalin.openapi.*;

public class MedioDeAccesibilidadController {

        static EntityManagerFactory entityManagerFactory;

        public MedioDeAccesibilidadController() {
        }

        public MedioDeAccesibilidadController(EntityManagerFactory emFactory) {
                this.entityManagerFactory = emFactory;
        }

        @OpenApi(
                summary = "Obtener todas los medio De Accesibilidad",
                path = "medioDeAccesibilidad",
                methods = HttpMethod.GET,
                tags = {"medioDeAccesibilidad"}
        )
        public void getAllMDA(Context ctx){
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                RepoMedioDeAccesibilidad repo = new RepoMedioDeAccesibilidad(entityManager);
                ctx.json(repo.all());
                entityManager.close();
        }

        @OpenApi(
                summary = "Obtener todos los medio De Accesibilidad Inaccesibles",
                path = "medioDeAccesibilidad/Inaccesibles",
                methods = HttpMethod.GET,
                tags = {"medioDeAccesibilidad"},
                pathParams = {@OpenApiParam(name = "estacion", description = "nombre de la estacion"),@OpenApiParam(name = "linea", description = "nombre de la linea")}
        )
        public void getInaccesible(Context ctx){
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                RepoMedioDeAccesibilidad repo = new RepoMedioDeAccesibilidad(entityManager);
                //línea y estación

                String estacion = ctx.queryParam("estacion") == null ? "" : ctx.queryParam("estacion")  ;
                String linea = ctx.queryParam("linea") == null ? "" : ctx.queryParam("linea");
                if( estacion  ==  ""  &&   linea  ==  "" ) {
                        ctx.json(repo.inaccesibles());
                }else {
                        if( estacion.length() >= 1 && linea.length() >= 1 ){
                                ctx.json(repo.findByEstacionYLinea(estacion ,linea )) ;
                        }
                        if( estacion.length() >= 1 ){
                                ctx.json(repo.findByEstacion(estacion)) ;
                        }
                        if( linea.length() >= 1 ){
                                ctx.json( repo.findByLinea(linea));
                        }

                }


                entityManager.close();
        }

        @OpenApi(
                summary = "obtiene un medio de accesibilidad",
                path = "medioDeAccesibilidad/{id}",
                methods = HttpMethod.GET,
                tags = {"medioDeAccesibilidad"}
        )
        public void getOne(Context ctx){
                String codigoDeCatalogo =  ctx.pathParam("id");
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                RepoMedioDeAccesibilidad repo = new RepoMedioDeAccesibilidad(entityManager);
                try {
                MedioDeAccesibilidad mda = repo.findByCodigoDeCatalogo(codigoDeCatalogo);
                if(mda != null){
                        ctx.json(mda);
                }else {
                        ctx.result( ctx.pathParam("id") + " no corresponde a un id valido");
                        ctx.status(404);
                }


                entityManager.close();
                }catch (Exception e) {
                        ctx.result("You are here because " + e.getMessage() );
                        ctx.status(404);
                        entityManager.close();
                        throw new RuntimeException("Error al buscar MedioDeAccesibilidad por código de catálogo", e);
                }

        }
        @OpenApi(
                summary = "cambia el estado de un medio de accesibilidad",
                path = "medioDeAccesibilidad/{id}",
                methods = HttpMethod.PUT,
                tags = {"medioDeAccesibilidad"},
                requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = MDAPutDTO.class)})
                )
        public void update(Context ctx){
                String codigoDeCatalogo =  ctx.pathParam("id");
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                RepoMedioDeAccesibilidad repo = new RepoMedioDeAccesibilidad(entityManager);
                MedioDeAccesibilidad mda = repo.findByCodigoDeCatalogo(codigoDeCatalogo);
                if(mda != null){
                String actualizacionEstado = ctx.formParam("inaccesible") == null ? "" : ctx.formParam("inaccesible") ;
                        if(actualizacionEstado.equals("true")  || actualizacionEstado.equals("false")){
                                mda.setInaccesible( Boolean.parseBoolean(actualizacionEstado) );
                                repo.save(mda);
                                ctx.json(mda);
                        }else {
                                ctx.result( ctx.formParam("inaccesible") + " no es un valor valido. solo corresponde true o false");
                                ctx.status(404);
                        }
                }else {
                        ctx.result( ctx.pathParam("id") + " no corresponde a un id valido");
                        ctx.status(404);
                }
                entityManager.close();
        }
}
