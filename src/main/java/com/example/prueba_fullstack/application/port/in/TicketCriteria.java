package com.example.prueba_fullstack.application.port.in;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;

public record TicketCriteria(Status status, Priority priority, Category category,
                             int page, int size, String sortBy, String sortDirection) {
}
