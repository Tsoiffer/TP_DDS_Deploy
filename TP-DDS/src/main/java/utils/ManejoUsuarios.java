package utils;

import java.util.List;
import java.util.stream.Collectors;

public class ManejoUsuarios {
    private static List<User> users = List.of(
            new User("UsuarioTransporte", "abc123"),
            new User("UsuarioOperador", "P4ssword!")
    );

    public static User getUsuarioPorNombre(String username){
        return users.stream().filter(x -> x.username.equals(username)).findFirst().orElse(null);
    }

    public Iterable<String> getAllUsernames(){
        return users.stream().map(u -> u.username).collect(Collectors.toList());
    }
}
