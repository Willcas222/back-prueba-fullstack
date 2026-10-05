package com.example.prueba_fullstack.adapters.in.rest.dto;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TicketRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 4000) String description,
        @NotBlank @Size(max = 150) String requestUser,
        @Pattern(regexp = "^$|^\\+?[1-9]\\d{7,14}$", message = "notificationPhone must be a valid international phone number")
        String notificationPhone,
        @NotNull Category category,
        @NotNull Priority priority) {
}
