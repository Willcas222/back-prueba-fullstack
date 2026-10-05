package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Solicitud;

public interface ConsultarSolicitudUseCase {
    Solicitud getById(Long id);
}
