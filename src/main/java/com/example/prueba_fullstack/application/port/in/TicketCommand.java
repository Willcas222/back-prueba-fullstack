package com.example.prueba_fullstack.application.port.in;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;

public record TicketCommand(String title, String description, String requestUser, String notificationPhone,
                            Category category, Priority priority) {
}
