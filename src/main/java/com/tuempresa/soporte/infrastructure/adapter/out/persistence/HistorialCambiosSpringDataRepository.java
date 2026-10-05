package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialCambiosSpringDataRepository extends JpaRepository<HistorialCambiosJpaEntity, Long> {
    List<HistorialCambiosJpaEntity> findByTicket_IdOrderByChangedAtDesc(Long solicitudId);
}
