package ar.utn.dds.Incidencia.Repositorios;

import ar.utn.dds.Incidencia.models.entities.LoteIncidencias;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class RepoLoteIncidencias {
    private EntityManager entityManager ;

    public RepoLoteIncidencias(){}

    public RepoLoteIncidencias(EntityManager em) {this.entityManager = em ;}

    public LoteIncidencias findById(Long id) {
        return this.entityManager.find(LoteIncidencias.class, id);
    }

    public LoteIncidencias getLoteIncendencias(Long id) {
        entityManager.getTransaction().begin();
        LoteIncidencias loteIncidencias = findById(id);
        entityManager.getTransaction().commit();
        return loteIncidencias;
    }

    public void save(LoteIncidencias loteIncidencias){
        entityManager.getTransaction().begin();
        entityManager.persist(loteIncidencias);
        entityManager.getTransaction().commit();
    }

    public List<LoteIncidencias> all() {
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LoteIncidencias> criteriaQuery = criteriaBuilder.createQuery(LoteIncidencias.class);
        Root<LoteIncidencias> root = criteriaQuery.from(LoteIncidencias.class);
        criteriaQuery.select(root);
        List<LoteIncidencias> lostesIncidencias = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.getTransaction().commit();
        return lostesIncidencias;
    }
}
