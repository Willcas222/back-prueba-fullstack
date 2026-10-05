package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Solicitud;

import java.util.List;

public record SolicitudPage(List<Solicitud> content, int page, int size, long totalElements, int totalPages) {
}
