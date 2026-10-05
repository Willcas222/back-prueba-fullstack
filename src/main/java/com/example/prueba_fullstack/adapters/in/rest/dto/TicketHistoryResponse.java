package com.example.prueba_fullstack.adapters.in.rest.dto;

import com.example.prueba_fullstack.domain.enums.Status;
import com.example.prueba_fullstack.domain.model.TicketHistory;

import java.time.LocalDateTime;

public record TicketHistoryResponse(Long id, Long ticketId, Status previousStatus, Status newStatus,
                                    LocalDateTime changedAt, String responsibleUser, String observation) {
    public static TicketHistoryResponse from(TicketHistory history) {
        return new TicketHistoryResponse(history.getId(), history.getTicketId(), history.getPreviousStatus(),
                history.getNewStatus(), history.getChangedAt(), history.getResponsibleUser(),
                history.getObservation());
    }
}
