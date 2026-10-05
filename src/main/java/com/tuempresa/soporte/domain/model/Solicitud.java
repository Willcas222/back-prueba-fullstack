package com.tuempresa.soporte.domain.model;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.model.Estado;

import java.time.LocalDateTime;
import java.util.Objects;

public class Solicitud {

    private Long id;
    private String title;
    private String description;
    private String requestUser;
    private String notificationPhone;
    private Categoria category;
    private Prioridad priority;
    private Estado status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Solicitud(Long id, String title, String description, String requestUser, String notificationPhone,
                   Categoria category, Prioridad priority, Estado status, LocalDateTime createdAt,
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

    public static Solicitud create(String title, String description, String requestUser, String notificationPhone,
                                Categoria category, Prioridad priority) {
        LocalDateTime now = LocalDateTime.now();
        return new Solicitud(null, title, description, requestUser, notificationPhone, category, priority,
                Estado.PENDIENTE, now, now);
    }

    public static Solicitud restore(Long id, String title, String description, String requestUser,
                                 String notificationPhone, Categoria category, Prioridad priority, Estado status,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Solicitud(id, title, description, requestUser, notificationPhone, category, priority, status,
                createdAt, updatedAt);
    }

    public void edit(String title, String description, String requestUser, String notificationPhone,
                     Categoria category, Prioridad priority) {
        if (status != Estado.PENDIENTE) {
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

    public void changeEstado(Estado newEstado, String observation) {
        Objects.requireNonNull(newEstado, "status is required");
        boolean allowed = (status == Estado.PENDIENTE
                && (newEstado == Estado.EN_PROGRESO || newEstado == Estado.CANCELADA))
                || (status == Estado.EN_PROGRESO && newEstado == Estado.RESUELTA);
        if (!allowed) {
            throw new IllegalStateException("Transition from " + status + " to " + newEstado + " is not allowed");
        }
        if (newEstado == Estado.RESUELTA && priority == Prioridad.CRITICA
                && (observation == null || observation.isBlank())) {
            throw new IllegalArgumentException("An observation is required to resolve a critical ticket");
        }
        this.status = newEstado;
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
    public Categoria getCategoria() { return category; }
    public Prioridad getPrioridad() { return priority; }
    public Estado getEstado() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
