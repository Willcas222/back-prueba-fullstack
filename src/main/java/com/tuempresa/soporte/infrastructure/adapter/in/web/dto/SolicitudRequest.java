package com.tuempresa.soporte.infrastructure.adapter.in.web.dto;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SolicitudRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 4000) String description,
        @NotBlank @Size(max = 150) String requestUser,
        @Pattern(regexp = "^$|^\\+?[1-9]\\d{7,14}$", message = "notificationPhone must be a valid international phone number")
        String notificationPhone,
        @NotNull Categoria category,
        @NotNull Prioridad priority) {
}
