package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import com.tuempresa.soporte.domain.model.Estado;
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
public class HistorialCambiosJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private SolicitudJpaEntity ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false, length = 30)
    private Estado previousEstado;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private Estado newEstado;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "responsible_user", nullable = false, length = 150)
    private String responsibleUser;

    @Column(length = 1000)
    private String observation;

    protected HistorialCambiosJpaEntity() {
    }

    public HistorialCambiosJpaEntity(Long id, SolicitudJpaEntity ticket, Estado previousEstado, Estado newEstado,
                                  LocalDateTime changedAt, String responsibleUser, String observation) {
        this.id = id;
        this.ticket = ticket;
        this.previousEstado = previousEstado;
        this.newEstado = newEstado;
        this.changedAt = changedAt;
        this.responsibleUser = responsibleUser;
        this.observation = observation;
    }

    public Long getId() { return id; }
    public SolicitudJpaEntity getSolicitud() { return ticket; }
    public Estado getPreviousEstado() { return previousEstado; }
    public Estado getNewEstado() { return newEstado; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getResponsibleUser() { return responsibleUser; }
    public String getObservation() { return observation; }
}
