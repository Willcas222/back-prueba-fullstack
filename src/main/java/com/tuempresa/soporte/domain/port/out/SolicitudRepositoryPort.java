package com.tuempresa.soporte.domain.port.out;

import com.tuempresa.soporte.domain.model.HistorialCambios;
import com.tuempresa.soporte.domain.model.Solicitud;
import com.tuempresa.soporte.domain.port.in.SolicitudCriteria;
import com.tuempresa.soporte.domain.port.in.SolicitudPage;

import java.util.Optional;

public interface SolicitudRepositoryPort {
    Solicitud save(Solicitud solicitud);
    Optional<Solicitud> findById(Long id);
    SolicitudPage findPage(SolicitudCriteria criteria);
    void saveEstadoChange(Solicitud solicitud, HistorialCambios historialCambios);
}
