package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelarSolicitudRequest(
        @NotBlank @Size(max = 150) String responsibleUser,
        @Size(max = 1000) String observation) {
}
