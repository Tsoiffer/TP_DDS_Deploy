import ar.utn.dds.Incidencia.*;
import ar.utn.dds.Incidencia.exception.IncidenciaInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidenciaTest {

    private static DateTimeFormatter formatEurope = DateTimeFormatter.ofPattern("dd/LL/yyyy");
    LocalDate fecha = LocalDate.parse("15/04/2023", formatEurope);
    CodigoCatalogo codigoCatalogo = new CodigoCatalogo("1234-12");
    Incidencia incidencia1 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", fecha ,Estados.REPORTADO);

    Incidencia incidencia2 = new Incidencia(codigoCatalogo, "Ascensor subte H estacion Parque Patricios fuera de servicio", fecha , Estados.ASIGNADO );

    Incidencia incidencia3 = new Incidencia(codigoCatalogo, "Escalera subte B estacion Pasteur fuera de servicio", fecha ,Estados.ASIGNADO);


    public IncidenciaTest() throws IncidenciaInvalidaException {
    }

    @Test
    public void testIncidenciaCambioDeEstado(){
        incidencia1.cambiarEstado(Estados.ASIGNADO);
        assertTrue(incidencia1.getEstado() == Estados.ASIGNADO, "Cambio de estado de Repotado a Asignado correctamente");
    }

    @Test
    public void testIncidenciaWorkflow(){
        incidencia1.cambiarEstado(Estados.EN_PROGRESO);
        assertTrue(incidencia1.getEstado() == Estados.REPORTADO, "Siendo el estado incial REPORTADO no cambia al estado EN_PROGRASO por no respetar Workflow");

    }

    @Test
    public void testIncidenciaDesestimada() {
        incidencia1.cambiarEstado(Estados.ASIGNADO);
        incidencia1.cambiarEstado(Estados.DESESTIMADO);
        assertTrue(incidencia1.getEstado() == Estados.DESESTIMADO, "Recorrio todos los estados hasta DESESTIMADO, logica de validarCambioEstado correcta");
    }


    @Test
    public void testIncidenciaSolucionada() {
        incidencia1.cambiarEstado(Estados.ASIGNADO);
        incidencia1.cambiarEstado(Estados.CONFIRMADO);
        incidencia1.cambiarEstado(Estados.EN_PROGRESO);
        incidencia1.cambiarEstado(Estados.SOLUCIONADO);
        incidencia1.cambiarEstado(Estados.CONFIRMADO);

        assertTrue(incidencia1.getEstado() == Estados.SOLUCIONADO, "La incidencia una vez solucionada no cambia de estado");
    }

}
