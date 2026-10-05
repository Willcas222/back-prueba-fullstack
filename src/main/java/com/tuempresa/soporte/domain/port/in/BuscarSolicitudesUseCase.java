package com.tuempresa.soporte.domain.port.in;

public interface BuscarSolicitudesUseCase {
    SolicitudPage search(SolicitudCriteria criteria);
}
