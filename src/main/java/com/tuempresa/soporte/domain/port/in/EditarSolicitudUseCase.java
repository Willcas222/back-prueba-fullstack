package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Solicitud;

public interface EditarSolicitudUseCase {
    Solicitud update(Long id, SolicitudCommand command);
}
