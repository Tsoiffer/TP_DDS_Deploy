import ar.utn.dds.Incidencia.models.entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceIT {
    static EntityManagerFactory entityManagerFactory ;
    EntityManager entityManager ;

    @BeforeAll
    public static void setUpClass() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("incidenciasDB");
    }
    @BeforeEach
    public void setup() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }
    @Test
    public void testConectar() {
// vacío, para ver que levante el ORM
    }
    @Test
    public void testGuardarYRecuperarEstado() throws Exception { //TODO test trivial, despues borrar
        Estado estado1 = new Estado("ASIGNADO", 1);
        entityManager.getTransaction().begin();
        entityManager.persist(estado1);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        Estado estado2 = entityManager.find(Estado.class,1L);

        assertEquals(estado1.getNombre(), estado2.getNombre()); // también puede redefinir el equals
    }
    @Test
    public void testGuardarYRecuperarOperador() throws Exception { //TODO test trivial, despues borrar
        Operador operador1 = new Operador("Pepe");
        entityManager.getTransaction().begin();
        entityManager.persist(operador1);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        Operador operador2 = entityManager.find(Operador.class,1L);

        assertEquals(operador1.getNombre(), operador2.getNombre()); // también puede redefinir el equals
    }
    @Test
    public void testGuardarYRecuperarCodigoDeCatalogo() throws Exception { //TODO test trivial, despues borrar
        CodigoCatalogo codigo1 = new CodigoCatalogo("123-asd");
        entityManager.getTransaction().begin();
        entityManager.persist(codigo1);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        CodigoCatalogo codigo2 = entityManager.find(CodigoCatalogo.class,1L);

        assertEquals(codigo1.getCodigo(), codigo2.getCodigo()); // también puede redefinir el equals
    }
    @Test
    public void testGuardarYRecuperarEstadoAsignado() throws Exception { //TODO test trivial, despues borrar
        Estado estado1 = new Estado("ASIGNADO", 1);
        EstadoAsignado estadoAsignado1 = new EstadoAsignado(estado1);
        entityManager.getTransaction().begin();
        entityManager.persist(estadoAsignado1);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        EstadoAsignado estadoAsignado2 = entityManager.find(EstadoAsignado.class,1L);

        assertEquals(estadoAsignado1.getEstadoActual().getNombre(), estadoAsignado2.getEstadoActual().getNombre()); // también puede redefinir el equals
    }
    @Test
    public void testGuardarYRecuperarIncidencia() throws Exception { //TODO test trivial, despues borrar
        Estado estado1 = new Estado("ASIGNADO", 1);
        Estado estado2 = new Estado("REPORTADO", 2);
        CodigoCatalogo codigo1 = new CodigoCatalogo("123-asd");
        Operador operador1 = new Operador("Pepe");
        DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
        LocalDate fecha = LocalDate.parse("15/04/2023", formatEurope);
        Incidencia incidencia1 = new Incidencia(codigo1, "incidencia test", fecha, estado1);
        incidencia1.setOperador(operador1);
        incidencia1.cambiarEstado(estado2);
        entityManager.getTransaction().begin();
        entityManager.persist(incidencia1);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManager = entityManagerFactory.createEntityManager();
        Incidencia incidencia2 = entityManager.find(Incidencia.class,1L);
        assertEquals(incidencia1.getDescripcion(), incidencia2.getDescripcion()); // también puede redefinir el equals
    }
}
