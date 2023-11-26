import ar.utn.dds.Incidencia.models.entities.CodigoCatalogo;
import ar.utn.dds.Incidencia.models.entities.Estado;
import ar.utn.dds.Incidencia.models.entities.Incidencia;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidenciaTest {

    private static DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
    LocalDate fecha = LocalDate.parse("15/04/2023", formatEurope);
    CodigoCatalogo codigoCatalogo = new CodigoCatalogo("1234-12");
    Estado reportado = new Estado("REPORTADO", 0);
    Estado asignado = new Estado("ASIGNADO", 1);
    Estado confirmado = new Estado("CONFIRMADO", 2);
    Estado enProgreso = new Estado("EN_PROGRESO", 3);
    Estado solucion = new Estado("SOLUCIONADO", 4);
    Estado desestimado = new Estado("DESESTIMADO", 5);


    Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", fecha ,reportado);
    Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio", fecha , asignado );
    Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", fecha ,asignado);


    public IncidenciaTest() throws IncidenciaInvalidaException {
    }

    @Test
    public void testIncidenciaCambioDeEstado(){
        incidencia1.cambiarEstado(asignado);
        assertTrue(incidencia1.getEstadoAsignado() == asignado, "Cambio de estado de Repotado a Asignado correctamente");
    }

    @Test
    public void testIncidenciaWorkflow(){
        incidencia1.cambiarEstado(enProgreso);
        assertTrue(incidencia1.getEstadoAsignado() == reportado, "Siendo el estado incial REPORTADO no cambia al estado EN_PROGRASO por no respetar Workflow");

    }

    @Test
    public void testIncidenciaDesestimada() {
        incidencia1.cambiarEstado(asignado);
        incidencia1.cambiarEstado(desestimado);
        assertTrue(incidencia1.getEstadoAsignado() == desestimado, "Recorrio todos los estados hasta DESESTIMADO, logica de validarCambioEstado correcta");
    }


    @Test
    public void testIncidenciaSolucionada() {
        incidencia1.cambiarEstado(asignado);
        incidencia1.cambiarEstado(confirmado);
        incidencia1.cambiarEstado(enProgreso);
        incidencia1.cambiarEstado(solucion);
        incidencia1.cambiarEstado(confirmado);

        assertTrue(incidencia1.getEstadoAsignado() == solucion, "La incidencia una vez solucionada no cambia de estado");
    }

}
