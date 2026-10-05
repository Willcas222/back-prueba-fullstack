package com.example.prueba_fullstack.domain.model;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketTest {

    @Test
    void permitsOnlyTheSpecifiedStatusTransitions() {
        Ticket ticket = newTicket(Priority.MEDIA);
        ticket.changeStatus(Status.EN_PROGRESO, null);
        ticket.changeStatus(Status.RESUELTA, "Hecho");

        assertEquals(Status.RESUELTA, ticket.getStatus());
        assertThrows(IllegalStateException.class, () -> ticket.changeStatus(Status.EN_PROGRESO, "reabrir"));
    }

    @Test
    void allowsPendingTicketsToBeCancelledButNotResolved() {
        Ticket ticket = newTicket(Priority.BAJA);
        assertThrows(IllegalStateException.class, () -> ticket.changeStatus(Status.RESUELTA, "Hecho"));
        ticket.changeStatus(Status.CANCELADA, "Duplicada");
        assertEquals(Status.CANCELADA, ticket.getStatus());
        assertThrows(IllegalStateException.class, () -> ticket.changeStatus(Status.EN_PROGRESO, "reabrir"));
    }

    @Test
    void requiresAnObservationToResolveCriticalTickets() {
        Ticket ticket = newTicket(Priority.CRITICA);
        ticket.changeStatus(Status.EN_PROGRESO, null);

        assertThrows(IllegalArgumentException.class, () -> ticket.changeStatus(Status.RESUELTA, " "));
        ticket.changeStatus(Status.RESUELTA, "Servicio restablecido");
        assertEquals(Status.RESUELTA, ticket.getStatus());
    }

    private Ticket newTicket(Priority priority) {
        return Ticket.create("Equipo sin conexión", "No inicia", "Ana", null, Category.HARDWARE, priority);
    }
}
