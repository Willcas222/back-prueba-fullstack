package com.example.prueba_fullstack.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataTicketHistoryRepository extends JpaRepository<TicketHistoryJpaEntity, Long> {
    List<TicketHistoryJpaEntity> findByTicket_IdOrderByChangedAtDesc(Long ticketId);
}
