package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import com.tuempresa.soporte.domain.model.Estado;
import com.tuempresa.soporte.domain.model.HistorialCambios;

import java.time.LocalDateTime;

public record HistorialCambiosResponse(Long id, Long ticketId, Estado previousStatus, Estado newStatus,
                                    LocalDateTime changedAt, String responsibleUser, String observation) {
    public static HistorialCambiosResponse from(HistorialCambios history) {
        return new HistorialCambiosResponse(history.getId(), history.getSolicitudId(), history.getPreviousEstado(),
                history.getNewEstado(), history.getChangedAt(), history.getResponsibleUser(),
                history.getObservation());
    }
}
