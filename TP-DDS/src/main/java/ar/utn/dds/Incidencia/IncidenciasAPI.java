package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.Controllers.Api.ApiIncidenciasController;
import ar.utn.dds.Incidencia.Controllers.Api.ApiMedidasDeAccesibilidadController;
import ar.utn.dds.Incidencia.Controllers.Api.LoginController;
import ar.utn.dds.Incidencia.Controllers.WebApp.HomeController;
import ar.utn.dds.Incidencia.Controllers.WebApp.IncidenciasController;
import ar.utn.dds.Incidencia.Controllers.WebApp.LoteIncidenciasController;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.plugin.OpenApiConfiguration;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocConfiguration;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.rendering.JavalinRenderer;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MQUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.*;


@OpenAPIDefinition(info = @Info(
        title = "TP-DDS Grupo 5",
        version = "0.0",
        description = "My APP",
        license = @License(name = "Apache 2.0", url = "http://foo.bar"),
        contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
),
        tags = {
                @Tag(name = "Home", description = "desc 1", externalDocs = @ExternalDocumentation(description = "docs desc")),
                @Tag(name = "Incidencia", description = "desc 2", externalDocs = @ExternalDocumentation(description = "docs desc 2")),
                @Tag(name = "LoteIncidencias"),
                @Tag(name = "MedidasInaccesibles")
        })
public class IncidenciasAPI {
    public static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) throws IOException, TimeoutException {

        Map<String, String> env = System.getenv();
        MQUtils mqutils = new MQUtils(
                env.get("QUEUE_HOST"),
                env.get("QUEUE_USERNAME"),
                env.get("QUEUE_PASSWORD"),
                env.get("QUEUE_USERNAME"),
                env.get("QUEUE_NAME")
        );

        String apiEndpoint = System.getenv("MDA_API");

        //Inicializar Entorno
        mqutils.init();
        startEntityManagerFactory();
        initTemplateEngine();

        Integer port = Integer.parseInt( System.getProperty("port", "8080"));

        IncidenciasController incidenciasController = new IncidenciasController(entityManagerFactory,mqutils);
        LoteIncidenciasController loteIncidenciasController = new LoteIncidenciasController(entityManagerFactory,mqutils);
        HomeController homeController = new HomeController();
        ApiIncidenciasController apiIncidenciasController = new ApiIncidenciasController(entityManagerFactory, mqutils);
        ApiMedidasDeAccesibilidadController apimedidasDeAccesibilidadController = new ApiMedidasDeAccesibilidadController(entityManagerFactory, mqutils);
        LoginController loginController = new LoginController();

        Javalin.create(
                config -> {
                    OpenApiConfiguration openApiConfiguration = new OpenApiConfiguration();
                    openApiConfiguration.getInfo().setTitle("TP-DDS Grupo 5");
                    config.plugins.register(new OpenApiPlugin(openApiConfiguration));
                    config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));
                    config.plugins.register(new ReDocPlugin(new ReDocConfiguration()));
                    Logger logger = LoggerFactory.getLogger(IncidenciasAPI.class);
                    logger.info("Welcome to SLF4J logging");
                }
        ).routes(() -> {

            path("", () -> {
                get(HomeController::pantallaDeHome);
                path("incidencia", () -> {
                    before(LoginController.ensureLogin);
                    get(IncidenciasController::getAll);
                    put(IncidenciasController::edit);
                    post(IncidenciasController::create);
                    //path("listMasRecientes", () -> {
                      //  get(IncidenciasController::getMasRecientes); NOTE: Esto paso a estar en api/incidencia/...
                    //});
                    path("cargaMasiva", () -> {
                        post(IncidenciasController::createMany);
                    });
                    //path("listViejas", () -> {
                    //    get(IncidenciasController::getViejas); NOTE: Esto paso a estar en API/incidencia/...
                    //});
                    path("{id}", () -> {
                        get(IncidenciasController::getOne);
                        delete(IncidenciasController::delete);
                    });
                    //path("lugar/{lugar}", () -> {
                        //get(IncidenciasController::getByLugar); NOTE: Esto paso a estar en api/incidencia/...
                    //});
                    path("estado/{estado}", () -> {
                     get(IncidenciasController::getFindByEstado);
                    });
                });
                path("loteIncidencias", () -> {
                    get(LoteIncidenciasController::getAllLoteIncidencias);
                    path("{id}", () -> {
                        get(LoteIncidenciasController::findById);
                    });
                });
                path("medidaInaccesible", () -> {
                    get(ApiMedidasDeAccesibilidadController::getAllInaccesibles);
                    path("linea/{linea}/estacion/{estacion}", () -> {
                        get(ApiMedidasDeAccesibilidadController::getByLugar);
                    });
                });

                //API
                path("API", ()->{
                    path("incidencia", ()->{
                        path("lugar/{lugar}", ()->{
                            get(ApiIncidenciasController::getByLugar);
                        });
                        path("listMasRecientes", ()->{
                            get(ApiIncidenciasController::getMasRecientes);
                        });
                        path("listViejas", ()->{
                           get(ApiIncidenciasController::getViejas);
                        });
                    });
                });
                post("/login", LoginController.handleLoginPost);
                post("/logout", LoginController.handleLogoutPost);
                get("/login", LoginController.handleLoginGet);

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

    private static void initTemplateEngine() {
        JavalinRenderer.register(
                (path, model, context) -> { // Función que renderiza el template
                    Handlebars handlebars = new Handlebars();
                    Template template = null;
                    try {
                        template = handlebars.compile("templates/" + path.replace(".hbs", ""));
                        return template.apply(model);
                    } catch (IOException e) {
                        //
                        e.printStackTrace();
                        context.status(HttpStatus.NOT_FOUND);
                        return "No se encuentra la página indicada...";
                    }
                }, ".hbs" // Extensión del archivo de template
        );
    }

    private static Consumer<JavalinConfig> config() {
        return config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
            });
        };
    }

}
