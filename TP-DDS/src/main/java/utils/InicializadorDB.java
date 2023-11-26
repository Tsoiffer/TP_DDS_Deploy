package utils;

import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
//Limpia la DB usando una conexión a la DB create-drop y iniciliza
public class InicializadorDB {
    public static EntityManagerFactory entityManagerFactory;
    public static void main(String[] args) throws IOException, TimeoutException {


        startEntityManagerFactory();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoEstado repoEstado = new RepoEstado(entityManager);
        RepoIncidencias repoIncidencias = new RepoIncidencias(entityManager);
        //INICIO Inicializado DE BD


        repoEstado.inicializarEstados();


        Estado estado = repoEstado.findByName("REPORTADO");
        CodigoCatalogo codigoCatalogo = new CodigoCatalogo("TOE-001");
        Incidencia unaIncidencia = new Incidencia(codigoCatalogo,"incidencia prueba","2020/12/02",estado);
        repoIncidencias.save(unaIncidencia);
        CodigoCatalogo otroCodigoCatalogo = new CodigoCatalogo("SAA-001");
        Incidencia otraIncidencia = new Incidencia(otroCodigoCatalogo,"otraIncidencia prueba","2022/12/02",estado);
        repoIncidencias.save(otraIncidencia);
        otraIncidencia = new Incidencia(otroCodigoCatalogo,"otraIncidencia prueba","2020/10/02",estado);
        repoIncidencias.save(otraIncidencia);
        otraIncidencia = new Incidencia(otroCodigoCatalogo,"otraIncidencia prueba","2020/12/05",estado);
        repoIncidencias.save(otraIncidencia);
        estado = repoEstado.findByName("ASIGNADO");
        otraIncidencia = new Incidencia(otroCodigoCatalogo,"incidencia en otro estado - prueba","2020/12/12",estado);
        repoIncidencias.save(otraIncidencia);
        //INICIO Inicializado DE BD

        unaIncidencia = repoIncidencias.findById(1L);

        System.out.println(estado.getNombre());
        System.out.println(unaIncidencia.getDescripcion());
        //INICIO PRUEBAS DE CONSULTAS BD
        Query queryCodigoCatalogo = entityManager.createQuery("SELECT codCatalogo FROM CodigoCatalogo codCatalogo WHERE codigo=:codigoCatalogo");
        queryCodigoCatalogo.setParameter("codigoCatalogo","SAA-001");
        CodigoCatalogo codigoDeCatalogo = (CodigoCatalogo) queryCodigoCatalogo.getSingleResult();;
        Query queryOrderByFechaRecientes = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia ORDER BY fechaDeReporte DESC");
        queryOrderByFechaRecientes.setMaxResults(4);
        List <Incidencia> incidencias = queryOrderByFechaRecientes.getResultList();
        System.out.println(incidencias.get(0).getFechaDeReporte());
        System.out.println(incidencias.get(1).getFechaDeReporte());
        System.out.println(incidencias.get(2).getFechaDeReporte());
        System.out.println(incidencias.size() );
        //FIN PRUEBAS DE CONSULTAS BD
        entityManager.close();

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
