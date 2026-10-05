package com.example.prueba_fullstack.adapters.in.rest.dto;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;
import com.example.prueba_fullstack.domain.model.Ticket;

import java.time.LocalDateTime;

public record TicketResponse(Long id, String title, String description, String requestUser,
                             String notificationPhone, Category category, Priority priority, Status status,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getRequestUser(),
                ticket.getNotificationPhone(), ticket.getCategory(), ticket.getPriority(), ticket.getStatus(),
                ticket.getCreatedAt(), ticket.getUpdatedAt());
    }
}
