package com.example.prueba_fullstack.application.port.in;

import com.example.prueba_fullstack.domain.enums.Status;
import com.example.prueba_fullstack.domain.model.Ticket;
import com.example.prueba_fullstack.domain.model.TicketHistory;

import java.util.List;

public interface TicketUseCase {
    Ticket create(TicketCommand command);
    Ticket getById(Long id);
    TicketPage search(TicketCriteria criteria);
    Ticket update(Long id, TicketCommand command);
    Ticket changeStatus(Long id, Status newStatus, String responsibleUser, String observation);
    Ticket cancel(Long id, String responsibleUser, String observation);
    List<TicketHistory> getHistory(Long id);
}
