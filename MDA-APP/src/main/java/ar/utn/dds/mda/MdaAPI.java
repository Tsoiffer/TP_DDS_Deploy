package ar.utn.dds.mda;

import ar.utn.dds.mda.Controllers.MedioDeAccesibilidadController;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.openapi.plugin.OpenApiConfiguration;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.post;


@OpenAPIDefinition(info = @Info(
        title = "TP-DDS Grupo 5 - MDA-API",
        version = "0.0",
        description = "My APP",
        license = @License(name = "Apache 2.0", url = "http://foo.bar"),
        contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
),
        tags = {
                @Tag(name = "medioDeAccesibilidad", description = "desc 1", externalDocs = @ExternalDocumentation(description = "docs desc")),
        })
public class MdaAPI {

    public static EntityManagerFactory entityManagerFactory;


    public static void main(String[] args) throws IOException, TimeoutException {

        startEntityManagerFactory();

        Integer port = Integer.parseInt( System.getProperty("port", "8081"));

        MedioDeAccesibilidadController medioDeAccesibilidadController = new MedioDeAccesibilidadController(entityManagerFactory);

        Javalin.create(
                config -> {
                    OpenApiConfiguration openApiConfiguration = new OpenApiConfiguration();
                    openApiConfiguration.getInfo().setTitle("TP-DDS Grupo 5");
                    config.plugins.register(new OpenApiPlugin(openApiConfiguration));
                    config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));
                    config.plugins.register(new ReDocPlugin(new ReDocConfiguration()));
                    Logger logger = LoggerFactory.getLogger(MdaAPI.class);
                    logger.info("Welcome to SLF4J logging");
                }
        ).routes(() -> {
            path("medioDeAccesibilidad", () -> {
                get(medioDeAccesibilidadController::getAllMDA);
                path("inaccesibles", () -> {
                    get(medioDeAccesibilidadController::getInaccesible);
                });
                path("{id}", () -> {
                    get(medioDeAccesibilidadController::getOne);
                    put(medioDeAccesibilidadController::update);
                });
            });
        }).start(port);


    }


    public static void startEntityManagerFactory() {
// https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<String, Object>();
        String[] keys = new String[] { "javax.persistence.jdbc.url", "javax.persistence.jdbc.user",
                "javax.persistence.jdbc.password", "javax.persistence.jdbc.driver", "hibernate.hbm2ddl.auto",
                "hibernate.connection.pool_size", "hibernate.show_sql" };
        for (String key : keys) {
            if (env.containsKey(key)) {
                String value = env.get(key);
                configOverrides.put(key, value);
            }
        }
        entityManagerFactory = Persistence.createEntityManagerFactory("db", configOverrides);
    }

}
