package ar.utn.dds.Incidencia.Controllers.Api;

import ar.utn.dds.Incidencia.DTOs.ResponseMessageDTO;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

    public static Handler handleLoginPost = ctx -> {
        String user = ctx.formParam("username");
        String pass = ctx.formParam("password");
        if (!UserController.autenticar(user,pass)){
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(new ResponseMessageDTO("1","Usuario o contraseña incorrectos."));
        } else {
            ctx.sessionAttribute("currentUser", user);
            ctx.json(new ResponseMessageDTO("0","Login success!"));
        }
    };

    public static Handler handleLogoutPost = ctx -> {
        ctx.sessionAttribute("currentUser", null);
        ctx.sessionAttribute("loggedOut", "true");
        ctx.json(new ResponseMessageDTO("0","Se cerró la sesión."));
    };

    public static Handler handleLoginGet = ctx -> {
        String usuarioActivo = ctx.sessionAttribute("currentUser");
        ctx.json(new ResponseMessageDTO("0","Usuario activo: " + usuarioActivo));
    };

    public static Handler ensureLogin = ctx -> {
        if (ctx.method().toString().equals("PUT") || ctx.method().toString().equals("POST") && ctx.path().startsWith("/incidencia")){
            if (ctx.sessionAttribute("currentUser") == null) {
                ctx.status(HttpStatus.UNAUTHORIZED);
            }
        }
    };
}
