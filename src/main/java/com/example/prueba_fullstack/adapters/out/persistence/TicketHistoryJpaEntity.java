package com.example.prueba_fullstack.adapters.out.persistence;

import com.example.prueba_fullstack.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status_history")
public class TicketHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketJpaEntity ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false, length = 30)
    private Status previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private Status newStatus;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "responsible_user", nullable = false, length = 150)
    private String responsibleUser;

    @Column(length = 1000)
    private String observation;

    protected TicketHistoryJpaEntity() {
    }

    public TicketHistoryJpaEntity(Long id, TicketJpaEntity ticket, Status previousStatus, Status newStatus,
                                  LocalDateTime changedAt, String responsibleUser, String observation) {
        this.id = id;
        this.ticket = ticket;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
        this.responsibleUser = responsibleUser;
        this.observation = observation;
    }

    public Long getId() { return id; }
    public TicketJpaEntity getTicket() { return ticket; }
    public Status getPreviousStatus() { return previousStatus; }
    public Status getNewStatus() { return newStatus; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getResponsibleUser() { return responsibleUser; }
    public String getObservation() { return observation; }
}
