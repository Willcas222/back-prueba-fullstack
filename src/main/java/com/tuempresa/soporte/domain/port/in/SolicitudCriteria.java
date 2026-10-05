package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.model.Estado;

public record SolicitudCriteria(Estado status, Prioridad priority, Categoria category,
                             int page, int size, String sortBy, String sortDirection) {
}
