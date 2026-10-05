package com.example.prueba_fullstack.application.service;

import com.example.prueba_fullstack.application.port.in.TicketCommand;
import com.example.prueba_fullstack.application.port.in.TicketCriteria;
import com.example.prueba_fullstack.application.port.in.TicketPage;
import com.example.prueba_fullstack.application.port.in.TicketUseCase;
import com.example.prueba_fullstack.application.port.out.TicketNotificationPort;
import com.example.prueba_fullstack.application.port.out.TicketRepositoryPort;
import com.example.prueba_fullstack.domain.enums.Status;
import com.example.prueba_fullstack.domain.model.Ticket;
import com.example.prueba_fullstack.domain.model.TicketHistory;

import java.util.List;

public class TicketApplicationService implements TicketUseCase {

    private final TicketRepositoryPort repository;
    private final TicketNotificationPort notification;

    public TicketApplicationService(TicketRepositoryPort repository, TicketNotificationPort notification) {
        this.repository = repository;
        this.notification = notification;
    }

    @Override
    public Ticket create(TicketCommand command) {
        return repository.save(Ticket.create(command.title(), command.description(), command.requestUser(),
                command.notificationPhone(), command.category(), command.priority()));
    }

    @Override
    public Ticket getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    @Override
    public TicketPage search(TicketCriteria criteria) {
        return repository.findPage(criteria);
    }

    @Override
    public Ticket update(Long id, TicketCommand command) {
        Ticket ticket = getById(id);
        ticket.edit(command.title(), command.description(), command.requestUser(), command.notificationPhone(),
                command.category(), command.priority());
        return repository.save(ticket);
    }

    @Override
    public Ticket changeStatus(Long id, Status newStatus, String responsibleUser, String observation) {
        Ticket ticket = getById(id);
        Status previousStatus = ticket.getStatus();
        ticket.changeStatus(newStatus, observation);
        TicketHistory history = TicketHistory.create(id, previousStatus, newStatus, responsibleUser, observation);
        repository.saveStatusChange(ticket, history);
        notification.statusChanged(ticket);
        return ticket;
    }

    @Override
    public Ticket cancel(Long id, String responsibleUser, String observation) {
        return changeStatus(id, Status.CANCELADA, responsibleUser, observation);
    }

    @Override
    public List<TicketHistory> getHistory(Long id) {
        getById(id);
        return repository.findHistoryByTicketId(id);
    }
}
