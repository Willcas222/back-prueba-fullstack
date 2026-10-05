package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SolicitudSpringDataRepository extends JpaRepository<SolicitudJpaEntity, Long>,
        JpaSpecificationExecutor<SolicitudJpaEntity> {
}
