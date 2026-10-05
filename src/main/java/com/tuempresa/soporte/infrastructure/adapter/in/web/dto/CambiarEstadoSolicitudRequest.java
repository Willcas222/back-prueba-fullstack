package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import com.tuempresa.soporte.domain.model.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CambiarEstadoSolicitudRequest(
        @NotNull Estado status,
        @NotBlank @Size(max = 150) String responsibleUser,
        @Size(max = 1000) String observation) {
}
