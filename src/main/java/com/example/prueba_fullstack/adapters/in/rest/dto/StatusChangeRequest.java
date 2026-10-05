package com.example.prueba_fullstack.adapters.in.rest.dto;

import com.example.prueba_fullstack.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StatusChangeRequest(
        @NotNull Status status,
        @NotBlank @Size(max = 150) String responsibleUser,
        @Size(max = 1000) String observation) {
}
