package com.example.prueba_fullstack.adapters.out.persistence;

import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;
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
public class TicketJpaEntity {

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
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected TicketJpaEntity() {
    }

    public TicketJpaEntity(Long id, String title, String description, String requestUser, String notificationPhone,
                           Category category, Priority priority, Status status, LocalDateTime createdAt,
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
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
