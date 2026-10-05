package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Solicitud;

public interface CancelarSolicitudUseCase {
    Solicitud cancel(Long id, String usuarioResponsable, String observacion);
}
