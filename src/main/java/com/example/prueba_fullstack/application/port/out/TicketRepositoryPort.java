package com.example.prueba_fullstack.application.port.out;

import com.example.prueba_fullstack.application.port.in.TicketCriteria;
import com.example.prueba_fullstack.application.port.in.TicketPage;
import com.example.prueba_fullstack.domain.model.Ticket;
import com.example.prueba_fullstack.domain.model.TicketHistory;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryPort {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(Long id);
    TicketPage findPage(TicketCriteria criteria);
    void saveStatusChange(Ticket ticket, TicketHistory history);
    List<TicketHistory> findHistoryByTicketId(Long ticketId);
}
