package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.Controllers.IncidenciasController;
import io.javalin.Javalin;
import utils.ManejoDataset;
import io.javalin.openapi.plugin.OpenApiConfiguration;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class IncidenciasAPI {

    public static void main(String[] args){

        Integer port = Integer.parseInt( System.getProperty("port", "8080"));
        RepoIncidencias repoIncidencias = new RepoIncidencias();
        ManejoDataset.setearDatos().stream().forEach(unaIncidencia -> repoIncidencias.save(unaIncidencia));
        IncidenciasController incidenciasController = new IncidenciasController(repoIncidencias);

        Javalin.create(
                config -> {
                    OpenApiConfiguration openApiConfiguration = new OpenApiConfiguration();
                    openApiConfiguration.getInfo().setTitle("TP-DDS Grupo 5");
                    config.plugins.register(new OpenApiPlugin(openApiConfiguration));
                    config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));
                    config.plugins.register(new ReDocPlugin(new ReDocConfiguration()));
                }
        ).routes(() -> {
            path("incidencia", () -> {
                get(IncidenciasController::getAll); //TODO ver como retornar el json
                post(IncidenciasController::create); //TODO probarlo
                path("listMasRecientes", () -> {
                    get(IncidenciasController::getMasRecientes);
                });
                path("listViejas", () -> {
                    get(IncidenciasController::getViejas); //TODO (TRP): Mejor path, o meter todo en get /incidencia usando body
                    //TODO ver rutas para mas recientes o mas viejas o por estado
                });
                path("{id}", () -> {
                    put(IncidenciasController::edit); //TODO
                    get(IncidenciasController::getOne); //TODO ver como pasamos el ID
                    delete(IncidenciasController::delete);
                });
                path("lugar/{lugar}", () -> {
                    get(IncidenciasController::getByLugar);
                });

                path("estado/{estado}", () -> {
                    get(IncidenciasController::getFindByEstado);
                });
            });
        }).start(port);
    }

}
