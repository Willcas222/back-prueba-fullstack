package com.example.prueba_fullstack.application.service;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Ticket " + id + " was not found");
    }
}
