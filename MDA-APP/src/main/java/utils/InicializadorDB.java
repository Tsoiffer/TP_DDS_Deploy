package utils;

//Limpia la DB usando una conexión a la DB create-drop y iniciliza

import ar.utn.dds.mda.Repositorios.RepoMedioDeAccesibilidad;
import ar.utn.dds.mda.models.enities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class InicializadorDB {
    public static EntityManagerFactory entityManagerFactory;
    public static void main(String[] args) throws IOException, TimeoutException {

        startEntityManagerFactory();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoMedioDeAccesibilidad repoMedioDeAccesibilidad = new RepoMedioDeAccesibilidad(entityManager);
        //INICIO Inicializado DE BD

        //CREAMOS LOS TIPOS DE TRANSPORTE
        TipoMedioDeTransporte tipoTren = new TipoMedioDeTransporte("TREN");
        TipoMedioDeTransporte tipoSubte = new TipoMedioDeTransporte("SUBTE");

        //CREAMOS LAS LINEAS
        LineaDeTransporte subteLineaA = new LineaDeTransporte("A",tipoSubte);
        LineaDeTransporte subteLineaC = new LineaDeTransporte("C",tipoSubte);
        LineaDeTransporte trenSarmiento = new LineaDeTransporte("SARMIENTO", tipoTren);

        //CREAMOS LAS ESTACIONES
        Estacion estacionLima = new Estacion("LIMA",subteLineaA);
        List<Estacion> combinacion = new ArrayList<>();
        combinacion.add(estacionLima);
        Estacion estacionAvDeMayo = new Estacion("AV: DE MAYO",subteLineaC,combinacion);
        Estacion estacionOnce = new Estacion("ONCE",trenSarmiento);

        //CREAMOS LOS TIPOS DE MEDIO DE ACCESIBILIDAD
        TipoDeMedidaDeAccesibilidad escalera = new TipoDeMedidaDeAccesibilidad("ESCALERA");
        TipoDeMedidaDeAccesibilidad ascensor = new TipoDeMedidaDeAccesibilidad("ASCENSOR");

        //CREAMOS LOS MEDIO DE ACCESIBILIDAD

        // Primera letra identifica que es un Subte la segunda identifica que es la linea A y la tercera identifica que es un Ascensor
        MedioDeAccesibilidad ascensorLineaA = new MedioDeAccesibilidad("SAA-001",false,ascensor,estacionLima);
        MedioDeAccesibilidad escaleraLineaC = new MedioDeAccesibilidad("SCE-001",true,escalera,estacionAvDeMayo);
        MedioDeAccesibilidad escaleraTrenSarmiento = new MedioDeAccesibilidad("TOE-001",true,escalera,estacionOnce);
        MedioDeAccesibilidad ascensorTrenSarmiento = new MedioDeAccesibilidad("TOA-002",false,ascensor,estacionOnce);


        //Guardamos los medios de accesibilidad
        repoMedioDeAccesibilidad.save(ascensorLineaA);
        repoMedioDeAccesibilidad.save(escaleraLineaC);
        repoMedioDeAccesibilidad.save(escaleraTrenSarmiento);
        repoMedioDeAccesibilidad.save(ascensorTrenSarmiento);
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
