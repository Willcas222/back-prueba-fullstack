package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import com.tuempresa.soporte.domain.port.in.SolicitudPage;

import java.util.List;

public record SolicitudPageResponse(List<SolicitudResponse> content, int page, int size,
                                 long totalElements, int totalPages) {
    public static SolicitudPageResponse from(SolicitudPage page) {
        return new SolicitudPageResponse(page.content().stream().map(SolicitudResponse::from).toList(),
                page.page(), page.size(), page.totalElements(), page.totalPages());
    }
}
