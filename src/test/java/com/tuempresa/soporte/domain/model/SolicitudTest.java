package com.tuempresa.soporte.domain.model;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.model.Estado;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolicitudTest {

    @Test
    void permitsOnlyTheSpecifiedEstadoTransitions() {
        Solicitud ticket = newSolicitud(Prioridad.MEDIA);
        ticket.changeEstado(Estado.EN_PROGRESO, null);
        ticket.changeEstado(Estado.RESUELTA, "Hecho");

        assertEquals(Estado.RESUELTA, ticket.getEstado());
        assertThrows(IllegalStateException.class, () -> ticket.changeEstado(Estado.EN_PROGRESO, "reabrir"));
    }

    @Test
    void allowsPendingSolicitudsToBeCancelledButNotResolved() {
        Solicitud ticket = newSolicitud(Prioridad.BAJA);
        assertThrows(IllegalStateException.class, () -> ticket.changeEstado(Estado.RESUELTA, "Hecho"));
        ticket.changeEstado(Estado.CANCELADA, "Duplicada");
        assertEquals(Estado.CANCELADA, ticket.getEstado());
        assertThrows(IllegalStateException.class, () -> ticket.changeEstado(Estado.EN_PROGRESO, "reabrir"));
    }

    @Test
    void requiresAnObservationToResolveCriticalSolicituds() {
        Solicitud ticket = newSolicitud(Prioridad.CRITICA);
        ticket.changeEstado(Estado.EN_PROGRESO, null);

        assertThrows(IllegalArgumentException.class, () -> ticket.changeEstado(Estado.RESUELTA, " "));
        ticket.changeEstado(Estado.RESUELTA, "Servicio restablecido");
        assertEquals(Estado.RESUELTA, ticket.getEstado());
    }

    private Solicitud newSolicitud(Prioridad priority) {
        return Solicitud.create("Equipo sin conexión", "No inicia", "Ana", null, Categoria.HARDWARE, priority);
    }
}
