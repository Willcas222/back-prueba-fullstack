package com.tuempresa.soporte.domain.port.out;

import com.tuempresa.soporte.domain.model.HistorialCambios;

import java.util.List;

public interface HistorialCambiosRepositoryPort {
    List<HistorialCambios> findBySolicitudId(Long solicitudId);
}
