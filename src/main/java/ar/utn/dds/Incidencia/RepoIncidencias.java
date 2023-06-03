package ar.utn.dds.Incidencia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RepoIncidencias {
    private List<Incidencia> incidencias = new ArrayList<>();

    public RepoIncidencias() {}

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
    public String deleteIncidencia(String codigo){
        boolean seElimino = this.incidencias.removeIf(item -> item.codigoCatalogo.codigo.equals(codigo));
        if (seElimino)
            return "El registro " + codigo + " se eliminó correctamente.";
        else
            return "No se pudo eliminar la incidencia con código " + codigo + ".";
    }

    public String createIncidencia(Incidencia incidencia){
        Incidencia incidenciaNueva = new Incidencia(incidencia.getCodigoCatalogo(), incidencia.getDescripcion(), LocalDate.parse(incidencia.getFechaDeReporte()));
        this.save(incidenciaNueva);
        return "Nueva incidencia " + incidenciaNueva.getCodigoCatalogo() + "creada.";
    }

    public String editIncidencia(String codigo, String json) throws JsonProcessingException {
        Incidencia incidencia = this.findByLugar(new CodigoCatalogo(codigo)).get(0);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(json, Map.class);
        String estado = map.get("estado");

        incidencia.cambiarEstado(Estados.valueOf(estado));
        return "La incidencia " + codigo + " ha sido modificada. El nuevo estado es " + incidencia.getEstado() + ".";
    }
}
