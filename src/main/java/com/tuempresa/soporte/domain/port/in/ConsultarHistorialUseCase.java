package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.HistorialCambios;

import java.util.List;

public interface ConsultarHistorialUseCase {
    List<HistorialCambios> getHistory(Long id);
}
