package ar.utn.dds.Incidencia.Controllers.WebApp;

import ar.utn.dds.Incidencia.DTOs.MedidaDeAccesibilidadDTO;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.*;

import java.io.IOException;
import io.javalin.rendering.*;
import utils.MQUtils;
import utils.ManejoConsultaApiMDA;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeController {

    public HomeController(){
    }
    @OpenApi(
            summary = "pantalla de inicio",
            operationId = "pantallaDeHome",
            path = "/",
            methods = HttpMethod.GET,
            tags = {"Home"},
            responses = {
                    @OpenApiResponse(status = "200")
            }
    )
    public static void pantallaDeHome(Context ctx)  { // Ahora el homeController muestra el listado de MDA Inaccesibles.
        Map<String, Object> model = new HashMap<>();
        ManejoConsultaApiMDA proxyMDA = new ManejoConsultaApiMDA(System.getenv().get("MDA_API"));

        String response = proxyMDA.filtrarMDAInaccesibles();

        List<MedidaDeAccesibilidadDTO> medidas = ManejoConsultaApiMDA.fromJsonArray(response);


        model.put("mda", medidas);

        ctx.render("new_home.hbs", model);
    }
}
