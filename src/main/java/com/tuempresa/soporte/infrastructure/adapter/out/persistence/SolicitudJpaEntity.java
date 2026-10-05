package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.model.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets", indexes = {
        @Index(name = "idx_ticket_status", columnList = "status"),
        @Index(name = "idx_ticket_priority", columnList = "priority"),
        @Index(name = "idx_ticket_category", columnList = "category")
})
public class SolicitudJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(name = "request_user", nullable = false, length = 150)
    private String requestUser;

    @Column(name = "notification_phone", length = 30)
    private String notificationPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Prioridad priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Estado status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected SolicitudJpaEntity() {
    }

    public SolicitudJpaEntity(Long id, String title, String description, String requestUser, String notificationPhone,
                           Categoria category, Prioridad priority, Estado status, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requestUser = requestUser;
        this.notificationPhone = notificationPhone;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
