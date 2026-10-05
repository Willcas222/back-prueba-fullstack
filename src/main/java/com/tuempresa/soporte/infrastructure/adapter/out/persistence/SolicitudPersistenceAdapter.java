package com.tuempresa.soporte.infrastructure.adapter.out.persistence;

import com.tuempresa.soporte.domain.port.in.SolicitudCriteria;
import com.tuempresa.soporte.domain.port.in.SolicitudPage;
import com.tuempresa.soporte.domain.port.out.HistorialCambiosRepositoryPort;
import com.tuempresa.soporte.domain.port.out.SolicitudRepositoryPort;
import com.tuempresa.soporte.domain.model.Solicitud;
import com.tuempresa.soporte.domain.model.HistorialCambios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class SolicitudPersistenceAdapter implements SolicitudRepositoryPort, HistorialCambiosRepositoryPort {

    private static final List<String> SORTABLE_FIELDS =
            List.of("id", "title", "requestUser", "category", "priority", "status", "createdAt", "updatedAt");

    private final SolicitudSpringDataRepository solicitudRepository;
    private final HistorialCambiosSpringDataRepository historialRepository;
    private final SolicitudJpaMapper mapper;

    public SolicitudPersistenceAdapter(SolicitudSpringDataRepository solicitudRepository,
                                       HistorialCambiosSpringDataRepository historialRepository,
                                       SolicitudJpaMapper mapper) {
        this.solicitudRepository = solicitudRepository;
        this.historialRepository = historialRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Solicitud save(Solicitud ticket) {
        return mapper.toDomain(solicitudRepository.save(mapper.toEntity(ticket)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> findById(Long id) {
        return solicitudRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudPage findPage(SolicitudCriteria criteria) {
        if (!SORTABLE_FIELDS.contains(criteria.sortBy())) {
            throw new IllegalArgumentException("Unsupported sortBy field: " + criteria.sortBy());
        }
        String sortBy = criteria.sortBy();
        Sort.Direction direction = "desc".equalsIgnoreCase(criteria.sortDirection())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest request = PageRequest.of(criteria.page(), criteria.size(), Sort.by(direction, sortBy));
        Specification<SolicitudJpaEntity> specification = (root, query, builder) -> builder.and(
                criteria.status() == null ? builder.conjunction() : builder.equal(root.get("status"), criteria.status()),
                criteria.priority() == null ? builder.conjunction() : builder.equal(root.get("priority"), criteria.priority()),
                criteria.category() == null ? builder.conjunction() : builder.equal(root.get("category"), criteria.category())
        );
        Page<SolicitudJpaEntity> result = solicitudRepository.findAll(specification, request);
        return new SolicitudPage(result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    @Override
    @Transactional
    public void saveEstadoChange(Solicitud ticket, HistorialCambios history) {
        SolicitudJpaEntity savedSolicitud = solicitudRepository.save(mapper.toEntity(ticket));
        historialRepository.save(new HistorialCambiosJpaEntity(null, savedSolicitud, history.getPreviousEstado(),
                history.getNewEstado(), history.getChangedAt(), history.getResponsibleUser(), history.getObservation()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialCambios> findBySolicitudId(Long solicitudId) {
        return historialRepository.findByTicket_IdOrderByChangedAtDesc(solicitudId).stream()
                .map(mapper::toDomain).toList();
    }
}
