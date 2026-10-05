package com.example.prueba_fullstack.domain.model;

import com.example.prueba_fullstack.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class TicketHistory {

    private final Long id;
    private final Long ticketId;
    private final Status previousStatus;
    private final Status newStatus;
    private final LocalDateTime changedAt;
    private final String responsibleUser;
    private final String observation;

    private TicketHistory(Long id, Long ticketId, Status previousStatus, Status newStatus,
                          LocalDateTime changedAt, String responsibleUser, String observation) {
        this.id = id;
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId is required");
        this.previousStatus = Objects.requireNonNull(previousStatus, "previousStatus is required");
        this.newStatus = Objects.requireNonNull(newStatus, "newStatus is required");
        this.changedAt = Objects.requireNonNull(changedAt, "changedAt is required");
        this.responsibleUser = requireText(responsibleUser, "responsibleUser");
        this.observation = observation == null ? null : observation.trim();
    }

    public static TicketHistory create(Long ticketId, Status previousStatus, Status newStatus,
                                       String responsibleUser, String observation) {
        return new TicketHistory(null, ticketId, previousStatus, newStatus, LocalDateTime.now(),
                responsibleUser, observation);
    }

    public static TicketHistory restore(Long id, Long ticketId, Status previousStatus, Status newStatus,
                                        LocalDateTime changedAt, String responsibleUser, String observation) {
        return new TicketHistory(id, ticketId, previousStatus, newStatus, changedAt, responsibleUser, observation);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public Status getPreviousStatus() { return previousStatus; }
    public Status getNewStatus() { return newStatus; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getResponsibleUser() { return responsibleUser; }
    public String getObservation() { return observation; }
}
