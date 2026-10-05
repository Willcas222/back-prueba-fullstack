package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Estado;
import com.tuempresa.soporte.domain.model.Solicitud;

public interface CambiarEstadoSolicitudUseCase {
    Solicitud changeEstado(Long id, Estado estado, String usuarioResponsable, String observacion);
}
