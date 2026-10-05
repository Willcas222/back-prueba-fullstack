package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.model.Estado;
import com.tuempresa.soporte.domain.model.Solicitud;

import java.time.LocalDateTime;

public record SolicitudResponse(Long id, String title, String description, String requestUser,
                             String notificationPhone, Categoria category, Prioridad priority, Estado status,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static SolicitudResponse from(Solicitud ticket) {
        return new SolicitudResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getRequestUser(),
                ticket.getNotificationPhone(), ticket.getCategoria(), ticket.getPrioridad(), ticket.getEstado(),
                ticket.getCreatedAt(), ticket.getUpdatedAt());
    }
}
