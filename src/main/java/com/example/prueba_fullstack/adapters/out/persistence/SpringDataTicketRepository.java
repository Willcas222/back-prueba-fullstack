package com.example.prueba_fullstack.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface SpringDataTicketRepository extends JpaRepository<TicketJpaEntity, Long>,
        JpaSpecificationExecutor<TicketJpaEntity> {
}
