package ar.utn.dds.Incidencia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RepoIncidencias {
    private List<Incidencia> incidencias = new ArrayList<>();

    public RepoIncidencias() {

    }

    public void save(Incidencia incidencia){
        this.incidencias.add(incidencia);
    }
    public Integer count(){
        return incidencias.size();
    }
    public List<Incidencia> findByEstado(Estados estado){
        return this.incidencias.stream()
                .filter(e -> e.estado.estadoActual == estado)
                .toList();
    }
    public List<Incidencia> findByLugar(CodigoCatalogo lugar){
        return this.incidencias.stream().
                filter(incidencia -> incidencia.codigoCatalogo.equals(lugar)).
                limit(Long.parseLong(System.getenv("NUM_INCIDENCIAS"))).
                toList();
    }
    public List<Incidencia> listReciente(Estados estadosFiltro){
        return findByEstado(estadosFiltro).stream().
                sorted(Comparator.comparing(Incidencia::getFechaDeReporte).reversed()).
                limit(Long.parseLong(System.getenv("NUM_INCIDENCIAS"))).
                toList();
    }
    public List<Incidencia> listViejo(Estados estadosFiltro){
        return findByEstado(estadosFiltro).stream().
                sorted(Comparator.comparing(Incidencia::getFechaDeReporte)).
                limit(Long.parseLong(System.getenv("NUM_INCIDENCIAS"))).
                toList();
    }
    public List<Incidencia> listReciente(){ //Caso no querer filtrar por Estado.
        return this.incidencias.stream().
                sorted(Comparator.comparing(Incidencia::getFechaDeReporte).reversed()).
                limit(Long.parseLong(System.getenv("NUM_INCIDENCIAS"))).
                toList();
    }
    public List<Incidencia> listViejo(){ //Caso no querer filtrar por Estado.
        return this.incidencias.stream().
                sorted(Comparator.comparing(Incidencia::getFechaDeReporte)).
                limit(Long.parseLong(System.getenv("NUM_INCIDENCIAS"))).
                toList();
    }
}
