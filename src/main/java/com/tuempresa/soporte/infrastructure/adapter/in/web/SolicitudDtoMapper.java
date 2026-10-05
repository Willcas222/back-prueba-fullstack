package com.tuempresa.soporte.infrastructure.adapter.in.web;

import com.tuempresa.soporte.domain.model.HistorialCambios;
import com.tuempresa.soporte.domain.model.Solicitud;
import com.tuempresa.soporte.domain.port.in.SolicitudCommand;
import com.tuempresa.soporte.domain.port.in.SolicitudPage;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.HistorialCambiosResponse;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudPageResponse;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudRequest;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudResponse;
import org.springframework.stereotype.Component;

@Component
public class SolicitudDtoMapper {

    public SolicitudCommand toCommand(SolicitudRequest request) {
        return new SolicitudCommand(request.title(), request.description(), request.requestUser(),
                request.notificationPhone(), request.category(), request.priority());
    }

    public SolicitudResponse toResponse(Solicitud solicitud) {
        return SolicitudResponse.from(solicitud);
    }

    public HistorialCambiosResponse toResponse(HistorialCambios historial) {
        return HistorialCambiosResponse.from(historial);
    }

    public SolicitudPageResponse toPageResponse(SolicitudPage page) {
        return SolicitudPageResponse.from(page);
    }
}
