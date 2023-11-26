package ar.utn.dds.Incidencia.Repositorios;

import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class RepoCodigoDeCatalogo {

    private EntityManager entityManager ;

    public RepoCodigoDeCatalogo(){}

    public RepoCodigoDeCatalogo(EntityManager em) {this.entityManager = em ;}

    public CodigoCatalogo findByNombre(String nombre){
        entityManager.getTransaction().begin();
        Query queryFindByEstado = entityManager.createQuery("SELECT codigoCatalogo FROM CodigoCatalogo codigoCatalogo WHERE codigoCatalogo.codigo =:codigoNombre ");
        queryFindByEstado.setParameter("codigoNombre",nombre);
        CodigoCatalogo codigoCatalogo;
        try {
            codigoCatalogo  =  (CodigoCatalogo) queryFindByEstado.getSingleResult();
        } catch (NoResultException nre){
            codigoCatalogo = null;
        }
        entityManager.getTransaction().commit();
        return codigoCatalogo;
    }
}
