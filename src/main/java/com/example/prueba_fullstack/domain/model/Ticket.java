package com.example.prueba_fullstack.domain.model;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {

    private Long id;
    private String title;
    private String description;
    private String requestUser;
    private String notificationPhone;
    private Category category;
    private Priority priority;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Ticket(Long id, String title, String description, String requestUser, String notificationPhone,
                   Category category, Priority priority, Status status, LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.title = requireText(title, "title");
        this.description = requireText(description, "description");
        this.requestUser = requireText(requestUser, "requestUser");
        this.notificationPhone = notificationPhone;
        this.category = Objects.requireNonNull(category, "category is required");
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt is required");
    }

    public static Ticket create(String title, String description, String requestUser, String notificationPhone,
                                Category category, Priority priority) {
        LocalDateTime now = LocalDateTime.now();
        return new Ticket(null, title, description, requestUser, notificationPhone, category, priority,
                Status.PENDIENTE, now, now);
    }

    public static Ticket restore(Long id, String title, String description, String requestUser,
                                 String notificationPhone, Category category, Priority priority, Status status,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Ticket(id, title, description, requestUser, notificationPhone, category, priority, status,
                createdAt, updatedAt);
    }

    public void edit(String title, String description, String requestUser, String notificationPhone,
                     Category category, Priority priority) {
        if (status != Status.PENDIENTE) {
            throw new IllegalStateException("Only pending tickets can be edited");
        }
        this.title = requireText(title, "title");
        this.description = requireText(description, "description");
        this.requestUser = requireText(requestUser, "requestUser");
        this.notificationPhone = notificationPhone;
        this.category = Objects.requireNonNull(category, "category is required");
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(Status newStatus, String observation) {
        Objects.requireNonNull(newStatus, "status is required");
        boolean allowed = (status == Status.PENDIENTE
                && (newStatus == Status.EN_PROGRESO || newStatus == Status.CANCELADA))
                || (status == Status.EN_PROGRESO && newStatus == Status.RESUELTA);
        if (!allowed) {
            throw new IllegalStateException("Transition from " + status + " to " + newStatus + " is not allowed");
        }
        if (newStatus == Status.RESUELTA && priority == Priority.CRITICA
                && (observation == null || observation.isBlank())) {
            throw new IllegalArgumentException("An observation is required to resolve a critical ticket");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRequestUser() { return requestUser; }
    public String getNotificationPhone() { return notificationPhone; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
