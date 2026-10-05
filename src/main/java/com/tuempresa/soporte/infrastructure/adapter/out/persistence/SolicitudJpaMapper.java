package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import com.tuempresa.soporte.domain.model.HistorialCambios;
import com.tuempresa.soporte.domain.model.Solicitud;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SolicitudJpaMapper {

    public SolicitudJpaEntity toEntity(Solicitud solicitud) {
        return new SolicitudJpaEntity(solicitud.getId(), solicitud.getTitle(), solicitud.getDescription(),
                solicitud.getRequestUser(), solicitud.getNotificationPhone(), solicitud.getCategoria(),
                solicitud.getPrioridad(), solicitud.getEstado(), solicitud.getCreatedAt(), solicitud.getUpdatedAt());
    }

    public Solicitud toDomain(SolicitudJpaEntity entity) {
        return Solicitud.restore(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getRequestUser(),
                entity.getNotificationPhone(), entity.getCategoria(), entity.getPrioridad(), entity.getEstado(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public HistorialCambios toDomain(HistorialCambiosJpaEntity entity) {
        try {
            return HistorialCambios.restore(entity.getId(), entity.getSolicitud().getId(),
                    entity.getPreviousEstado(), entity.getNewEstado(), entity.getChangedAt(),
                    entity.getResponsibleUser(), entity.getObservation());
        } catch (EntityNotFoundException exception) {
            throw new IllegalStateException("Historial de cambios referencia una solicitud inexistente", exception);
        }
    }
}
