package ar.utn.dds.Incidencia.Repositorios;

import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RepoIncidencias {
    private List<Incidencia> incidencias = new ArrayList<>();

    private EntityManager entityManager ;

    public RepoIncidencias(){}

    public RepoIncidencias(EntityManager em) {this.entityManager = em ;}

    public List<Incidencia> all() {
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Incidencia> criteriaQuery = criteriaBuilder.createQuery(Incidencia.class);
        Root<Incidencia> root = criteriaQuery.from(Incidencia.class);
        criteriaQuery.select(root);
        List<Incidencia> incidencias = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.getTransaction().commit();
        return incidencias;
    }

    public Incidencia getIncidencia(Long id) {
        Incidencia incidencia =findById( id);
        return incidencia;
    }

    public void save(Incidencia incidencia){
        entityManager.getTransaction().begin();
        entityManager.persist(incidencia);
        entityManager.getTransaction().commit();
    }

    public void delete(Incidencia incidencia){
        entityManager.getTransaction().begin();
        entityManager.remove(incidencia);
        entityManager.getTransaction().commit();
    }

    public Integer count(){
        List<Incidencia> incidencias = this.all();
        return incidencias.size();
    }

    public Incidencia findById(Long id) {
        entityManager.getTransaction().begin();
        Incidencia incidencia = this.entityManager.find(Incidencia.class, id);
        entityManager.getTransaction().commit();
        return incidencia;
    }

    public List<Incidencia> findByEstado(Estado estado){
        entityManager.getTransaction().begin();
        Query queryFindByEstado = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia WHERE estado_id=:estadoId ORDER BY id DESC");
        queryFindByEstado.setParameter("estadoId",estado.getId());
        queryFindByEstado.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryFindByEstado.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias;
    }

    public boolean findIncidenciasAbiertas(CodigoCatalogo codigoDeCatalogo){
        List<Incidencia> incidencias = findByLugar(codigoDeCatalogo);
        for(Incidencia inc : incidencias){
           if(inc.estadoAsignado.getOrden() == 1 || inc.estadoAsignado.getOrden() == 2 || inc.estadoAsignado.getOrden() == 3) {
               entityManager.getTransaction().commit();
                return true;
            }
        }
        return false;
    }

    public List<Incidencia> findByLugar(CodigoCatalogo codigoDeCatalogo){
        entityManager.getTransaction().begin();
        Query queryFindByLugar = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia WHERE incidencia.codigoCatalogo.codigo=:codigoId ORDER BY id DESC");
        queryFindByLugar.setParameter("codigoId",codigoDeCatalogo.getCodigo() );
        queryFindByLugar.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryFindByLugar.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias;
    }

    public List<Incidencia> listTodo(){
        this.incidencias = this.all();
        return this.incidencias;
    }

    public List<Incidencia> listReciente(Estado estadosFiltro){
        entityManager.getTransaction().begin();
        Query queryFindByEstadoOrderByFechaRecientes = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia WHERE estado_id=:estadoId ORDER BY fechaDeReporte DESC");
        queryFindByEstadoOrderByFechaRecientes.setParameter("estadoId",estadosFiltro.getId());
        queryFindByEstadoOrderByFechaRecientes.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryFindByEstadoOrderByFechaRecientes.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias;
    }

    public List<Incidencia> listViejo(Estado estadosFiltro){
        entityManager.getTransaction().begin();
        Query queryFindByEstadoOrderByFechasAntiguas = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia WHERE estado_id=:estadoId ORDER BY fechaDeReporte ASC");
        queryFindByEstadoOrderByFechasAntiguas.setParameter("estadoId",estadosFiltro.getId());
        queryFindByEstadoOrderByFechasAntiguas.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryFindByEstadoOrderByFechasAntiguas.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias;
    }

    public List<Incidencia> listReciente(){
        entityManager.getTransaction().begin();
        Query queryOrderByFechaRecientes = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia ORDER BY fechaDeReporte DESC");
        queryOrderByFechaRecientes.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryOrderByFechaRecientes.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias;
    }

    public List<Incidencia> listViejo(){
        entityManager.getTransaction().begin();
        Query queryOrderByFechasAntiguas = entityManager.createQuery("SELECT incidencia FROM Incidencia incidencia ORDER BY fechaDeReporte ASC");
        queryOrderByFechasAntiguas.setMaxResults(Integer.parseInt(System.getenv("NUM_INCIDENCIAS")));
        this.incidencias = queryOrderByFechasAntiguas.getResultList();
        entityManager.getTransaction().commit();
        return this.incidencias ;
    }

    public Boolean deleteIncidencia(Long id){
        this.delete(findById(id));
        return true;
    }

    public boolean createIncidencia(Incidencia incidencia){
        //DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
        boolean rta = false;
        if(incidencia.descripcion!=null  || incidencia.codigoCatalogo!=null ||  incidencia.fechaDeReporte!=null)
        {
            //Incidencia incidenciaNueva = new Incidencia(incidencia.getCodigoCatalogo(), incidencia.getDescripcion(), LocalDate.parse(incidencia.getFechaDeReporte(), formatEurope));
            this.save(incidencia);
            rta=true;
        }
        return rta;
    }

    public boolean editIncidencia(Incidencia incidencia, Estado estadoNuevo) {
        incidencia.cambiarEstado(estadoNuevo);
        this.save(incidencia);
        return true;
    }
    public boolean editIncidencia(Incidencia incidencia, Estado estadoNuevo, String descripcion) {
        incidencia.cambiarEstado(estadoNuevo);
        incidencia.setDescripcion(descripcion);
        this.save(incidencia);
        return true;
    }
}
