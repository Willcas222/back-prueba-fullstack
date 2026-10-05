package com.example.prueba_fullstack.adapters.in.rest.dto;

import com.example.prueba_fullstack.application.port.in.TicketPage;

import java.util.List;

public record TicketPageResponse(List<TicketResponse> content, int page, int size,
                                 long totalElements, int totalPages) {
    public static TicketPageResponse from(TicketPage page) {
        return new TicketPageResponse(page.content().stream().map(TicketResponse::from).toList(),
                page.page(), page.size(), page.totalElements(), page.totalPages());
    }
}
