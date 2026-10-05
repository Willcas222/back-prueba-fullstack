package com.tuempresa.soporte.domain.model;

import com.tuempresa.soporte.domain.model.Estado;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistorialCambios {

    private final Long id;
    private final Long ticketId;
    private final Estado previousEstado;
    private final Estado newEstado;
    private final LocalDateTime changedAt;
    private final String responsibleUser;
    private final String observation;

    private HistorialCambios(Long id, Long ticketId, Estado previousEstado, Estado newEstado,
                          LocalDateTime changedAt, String responsibleUser, String observation) {
        this.id = id;
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId is required");
        this.previousEstado = Objects.requireNonNull(previousEstado, "previousEstado is required");
        this.newEstado = Objects.requireNonNull(newEstado, "newEstado is required");
        this.changedAt = Objects.requireNonNull(changedAt, "changedAt is required");
        this.responsibleUser = requireText(responsibleUser, "responsibleUser");
        this.observation = observation == null ? null : observation.trim();
    }

    public static HistorialCambios create(Long ticketId, Estado previousEstado, Estado newEstado,
                                       String responsibleUser, String observation) {
        return new HistorialCambios(null, ticketId, previousEstado, newEstado, LocalDateTime.now(),
                responsibleUser, observation);
    }

    public static HistorialCambios restore(Long id, Long ticketId, Estado previousEstado, Estado newEstado,
                                        LocalDateTime changedAt, String responsibleUser, String observation) {
        return new HistorialCambios(id, ticketId, previousEstado, newEstado, changedAt, responsibleUser, observation);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    public Long getId() { return id; }
    public Long getSolicitudId() { return ticketId; }
    public Estado getPreviousEstado() { return previousEstado; }
    public Estado getNewEstado() { return newEstado; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getResponsibleUser() { return responsibleUser; }
    public String getObservation() { return observation; }
}
