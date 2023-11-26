package ar.utn.dds.Incidencia.Repositorios;

import ar.utn.dds.Incidencia.models.entities.Estado;

import javax.persistence.EntityManager;

public class RepoEstado {
    private EntityManager entityManager ;

    public RepoEstado(){}

    public RepoEstado(EntityManager em) {this.entityManager = em ;}
    public void inicializarEstados(){ //SOLO USAR CUANDO ES NECESARIO INICIALIZAR LA BD
        Estado estado = new Estado("REPORTADO",0);
        this.save(estado);
        estado = new Estado("ASIGNADO",1);
        this.save(estado);
        estado = new Estado("CONFIRMADO",2);
        this.save(estado);
        estado = new Estado("EN_PROGRESO",3);
        this.save(estado);
        estado = new Estado("SOLUCIONADO",4);
        this.save(estado);
        estado = new Estado("DESESTIMADO",5);
        this.save(estado);
    };

    public Estado findById(Long id) {
        entityManager.getTransaction().begin();
        Estado estado =  this.entityManager.find(Estado.class, id);
        entityManager.getTransaction().commit();
        return estado;
    }

    public Estado findByName(String nombreEstado) {
        entityManager.getTransaction().begin();
        Estado estado = (Estado) this.entityManager.createQuery("SELECT estado FROM Estado estado WHERE nombre=:nombreEstado").setParameter("nombreEstado", nombreEstado).getSingleResult();
        entityManager.getTransaction().commit();
        return estado;
    }

    public Estado findByOrden(Integer ordenEstado) {
        entityManager.getTransaction().begin();
        Estado estado = (Estado) this.entityManager.createQuery("SELECT estado FROM Estado estado WHERE orden=:ordenEstado").setParameter("ordenEstado", ordenEstado).getSingleResult();
        entityManager.getTransaction().commit();
        return estado;
    }

    public Estado getEstado(Long id) {
        entityManager.getTransaction().begin();
        Estado estado = findById(id);
        entityManager.getTransaction().commit();
        return estado;
    }

    public void save(Estado estado){
        entityManager.getTransaction().begin();
        entityManager.persist(estado);
        entityManager.getTransaction().commit();
        ;
    }
}
