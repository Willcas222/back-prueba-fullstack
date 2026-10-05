package com.example.prueba_fullstack.adapters.out.persistence;

import com.example.prueba_fullstack.application.port.in.TicketCriteria;
import com.example.prueba_fullstack.application.port.in.TicketPage;
import com.example.prueba_fullstack.application.port.out.TicketRepositoryPort;
import com.example.prueba_fullstack.domain.model.Ticket;
import com.example.prueba_fullstack.domain.model.TicketHistory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketJpaRepositoryAdapter implements TicketRepositoryPort {

    private static final List<String> SORTABLE_FIELDS =
            List.of("id", "title", "requestUser", "category", "priority", "status", "createdAt", "updatedAt");

    private final SpringDataTicketRepository ticketRepository;
    private final SpringDataTicketHistoryRepository historyRepository;
    public TicketJpaRepositoryAdapter(SpringDataTicketRepository ticketRepository,
                                      SpringDataTicketHistoryRepository historyRepository) {
        this.ticketRepository = ticketRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    @Transactional
    public Ticket save(Ticket ticket) {
        return toDomain(ticketRepository.save(toEntity(ticket)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id).map(TicketJpaRepositoryAdapter::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketPage findPage(TicketCriteria criteria) {
        if (!SORTABLE_FIELDS.contains(criteria.sortBy())) {
            throw new IllegalArgumentException("Unsupported sortBy field: " + criteria.sortBy());
        }
        String sortBy = criteria.sortBy();
        Sort.Direction direction = "desc".equalsIgnoreCase(criteria.sortDirection())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest request = PageRequest.of(criteria.page(), criteria.size(), Sort.by(direction, sortBy));
        Specification<TicketJpaEntity> specification = (root, query, builder) -> builder.and(
                criteria.status() == null ? builder.conjunction() : builder.equal(root.get("status"), criteria.status()),
                criteria.priority() == null ? builder.conjunction() : builder.equal(root.get("priority"), criteria.priority()),
                criteria.category() == null ? builder.conjunction() : builder.equal(root.get("category"), criteria.category())
        );
        Page<TicketJpaEntity> result = ticketRepository.findAll(specification, request);
        return new TicketPage(result.getContent().stream().map(TicketJpaRepositoryAdapter::toDomain).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    @Override
    @Transactional
    public void saveStatusChange(Ticket ticket, TicketHistory history) {
        TicketJpaEntity savedTicket = ticketRepository.save(toEntity(ticket));
        historyRepository.save(new TicketHistoryJpaEntity(null, savedTicket, history.getPreviousStatus(),
                history.getNewStatus(), history.getChangedAt(), history.getResponsibleUser(),
                history.getObservation()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketHistory> findHistoryByTicketId(Long ticketId) {
        return historyRepository.findByTicket_IdOrderByChangedAtDesc(ticketId).stream()
                .map(TicketJpaRepositoryAdapter::toDomain).toList();
    }

    private static TicketJpaEntity toEntity(Ticket ticket) {
        return new TicketJpaEntity(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getRequestUser(),
                ticket.getNotificationPhone(), ticket.getCategory(), ticket.getPriority(), ticket.getStatus(),
                ticket.getCreatedAt(), ticket.getUpdatedAt());
    }

    private static Ticket toDomain(TicketJpaEntity entity) {
        return Ticket.restore(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getRequestUser(),
                entity.getNotificationPhone(), entity.getCategory(), entity.getPriority(), entity.getStatus(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private static TicketHistory toDomain(TicketHistoryJpaEntity entity) {
        try {
            return TicketHistory.restore(entity.getId(), entity.getTicket().getId(), entity.getPreviousStatus(),
                    entity.getNewStatus(), entity.getChangedAt(), entity.getResponsibleUser(), entity.getObservation());
        } catch (EntityNotFoundException exception) {
            throw new IllegalStateException("History references a missing ticket", exception);
        }
    }
}
