package com.example.prueba_fullstack.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelRequest(
        @NotBlank @Size(max = 150) String responsibleUser,
        @Size(max = 1000) String observation) {
}
