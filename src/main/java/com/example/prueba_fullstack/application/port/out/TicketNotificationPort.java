package com.example.prueba_fullstack.application.port.out;

import com.example.prueba_fullstack.domain.model.Ticket;

public interface TicketNotificationPort {
    void statusChanged(Ticket ticket);
}
