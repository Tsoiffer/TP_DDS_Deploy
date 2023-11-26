package ar.utn.dds.Incidencia;

import ar.utn.dds.Incidencia.Repositorios.RepoCodigoDeCatalogo;
import ar.utn.dds.Incidencia.Repositorios.RepoEstado;
import ar.utn.dds.Incidencia.Repositorios.RepoIncidencias;
import ar.utn.dds.Incidencia.Repositorios.RepoLoteIncidencias;
import ar.utn.dds.Incidencia.models.entities.LoteIncidencias;
import com.opencsv.exceptions.CsvValidationException;
import com.rabbitmq.client.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IncidenciasWorker extends DefaultConsumer {
    private String queueName;
    public static EntityManagerFactory entityManagerFactory;

    public IncidenciasWorker(Channel channel, String queueName, EntityManagerFactory em){
        super(channel);
        this.queueName = queueName;
        this.entityManagerFactory = em;
    }

    private void init() throws IOException {
        this.getChannel().queueDeclare(this.queueName, true,false,false, null);
        this.getChannel().basicConsume(this.queueName, false, this);

    }
    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException
    {
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
        String loteInsidenciaID = new String(body, "UTF-8");
        System.out.println("se recibio el siguiente loteInsidenciaID:");
        System.out.println(loteInsidenciaID);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RepoLoteIncidencias repoLoteIncidencia = new RepoLoteIncidencias(entityManager);
        RepoIncidencias repoIncidencia = new RepoIncidencias(entityManager);
        RepoEstado repoEstado = new RepoEstado(entityManager);
        RepoCodigoDeCatalogo repoCodigoDeCatalogo = new RepoCodigoDeCatalogo(entityManager);

        LoteIncidencias loteIncidencias = repoLoteIncidencia.getLoteIncendencias( Long.parseLong(loteInsidenciaID));
        System.out.println("llego el Lote");
        try {
            loteIncidencias.procesarIncidencias(repoIncidencia,repoEstado,repoCodigoDeCatalogo);
            repoLoteIncidencia.save(loteIncidencias);
            System.out.println("pase el procesar");
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        System.out.println("se finaliza la carga de el siguiente loteInsidenciaID:");
        System.out.println(loteInsidenciaID);
        entityManager.close();


    }


    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(System.getenv().get("QUEUE_HOST"));
        factory.setUsername(System.getenv().get("QUEUE_USERNAME"));
        factory.setPassword(System.getenv().get("QUEUE_PASSWORD"));
        factory.setVirtualHost(System.getenv().get("QUEUE_USERNAME"));
        String queueName = System.getenv().get("QUEUE_NAME");
        startEntityManagerFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        IncidenciasWorker worker = new IncidenciasWorker(channel,queueName,entityManagerFactory);
        worker.init();
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




