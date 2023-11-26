package ar.utn.dds.mda.Repositorios;

import ar.utn.dds.mda.models.enities.MedioDeAccesibilidad;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class RepoMedioDeAccesibilidad {

    private EntityManager entityManager ;

    public RepoMedioDeAccesibilidad(){}
    public RepoMedioDeAccesibilidad(EntityManager em) {this.entityManager = em ;}

    public List<MedioDeAccesibilidad> all() {
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MedioDeAccesibilidad> criteriaQuery = criteriaBuilder.createQuery(MedioDeAccesibilidad.class);
        Root<MedioDeAccesibilidad> root = criteriaQuery.from(MedioDeAccesibilidad.class);
        criteriaQuery.select(root);
        List<MedioDeAccesibilidad> medios = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.getTransaction().commit();
        return medios;
    }

    public MedioDeAccesibilidad findByCodigoDeCatalogo(String codigoDeCatalogo) {
        try {
            entityManager.getTransaction().begin();
            MedioDeAccesibilidad medioDeAccesibilidad = (MedioDeAccesibilidad) this.entityManager.createQuery("SELECT medioDeAccesibilidad FROM MedioDeAccesibilidad medioDeAccesibilidad WHERE medioDeAccesibilidad.codigoDeCatalogo=:nombreCodigoDeCatalogo").setParameter("nombreCodigoDeCatalogo", codigoDeCatalogo).getSingleResult();
            entityManager.getTransaction().commit();
            return medioDeAccesibilidad;

        }catch (NoResultException e) {

            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscarfindByCodigoDeCatalogo", e);
        }
    }

    //inaccesibles

    public List<MedioDeAccesibilidad> inaccesibles() {
        try {
            entityManager.getTransaction().begin();
            List<MedioDeAccesibilidad> medios = (List<MedioDeAccesibilidad>) this.entityManager.createQuery("SELECT medioDeAccesibilidad FROM MedioDeAccesibilidad medioDeAccesibilidad WHERE medioDeAccesibilidad.inaccesible = true ").getResultList();
            entityManager.getTransaction().commit();
            return medios;
        }catch (NoResultException e) {

                return null;
        }
    }
    public List<MedioDeAccesibilidad> findByEstacionYLinea(String estacion,String linea) {
        entityManager.getTransaction().begin();
        List<MedioDeAccesibilidad> medios = (List<MedioDeAccesibilidad>) this.entityManager.createQuery("SELECT medioDeAccesibilidad FROM MedioDeAccesibilidad medioDeAccesibilidad WHERE medioDeAccesibilidad.inaccesible = true AND medioDeAccesibilidad.estacion.nombreDeLaEstacion = :estacion AND medioDeAccesibilidad.estacion.lineaDeTransporte.nombre = :linea ").setParameter("estacion", estacion).setParameter("linea", linea).getResultList();
        entityManager.getTransaction().commit();
        return medios;
    }
    public List<MedioDeAccesibilidad> findByEstacion(String estacion) {
        entityManager.getTransaction().begin();
        List<MedioDeAccesibilidad> medios = (List<MedioDeAccesibilidad>) this.entityManager.createQuery("SELECT medioDeAccesibilidad FROM MedioDeAccesibilidad medioDeAccesibilidad WHERE medioDeAccesibilidad.inaccesible = true AND medioDeAccesibilidad.estacion.nombreDeLaEstacion = :estacion").setParameter("estacion", estacion).getResultList();
        entityManager.getTransaction().commit();
        return medios;
    }
    public List<MedioDeAccesibilidad> findByLinea(String linea) {
        entityManager.getTransaction().begin();
        List<MedioDeAccesibilidad> medios = (List<MedioDeAccesibilidad>) this.entityManager.createQuery("SELECT medioDeAccesibilidad FROM MedioDeAccesibilidad medioDeAccesibilidad WHERE medioDeAccesibilidad.inaccesible = true AND medioDeAccesibilidad.estacion.lineaDeTransporte.nombre = :linea ").setParameter("linea", linea).getResultList();
        entityManager.getTransaction().commit();
        return medios;
    }


    public void save(MedioDeAccesibilidad medioDeAccesibilidad){
        entityManager.getTransaction().begin();
        entityManager.persist(medioDeAccesibilidad);
        entityManager.getTransaction().commit();
    }

}
