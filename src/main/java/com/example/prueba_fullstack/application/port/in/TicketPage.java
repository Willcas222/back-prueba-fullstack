package com.example.prueba_fullstack.application.port.in;

import com.example.prueba_fullstack.domain.model.Ticket;

import java.util.List;

public record TicketPage(List<Ticket> content, int page, int size, long totalElements, int totalPages) {
}
