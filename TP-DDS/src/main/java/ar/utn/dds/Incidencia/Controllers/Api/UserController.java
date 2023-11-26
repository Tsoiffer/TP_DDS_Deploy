package ar.utn.dds.Incidencia.Controllers.Api;

import utils.ManejoUsuarios;
import utils.User;

public class UserController {
    public static boolean autenticar(String username, String pass) {
        if (username == null || pass == null)
        {
            return false;
        }
        User user = ManejoUsuarios.getUsuarioPorNombre(username);
        if (user == null){
            return false;
        }
        return pass.equals(user.password);
    }
}
