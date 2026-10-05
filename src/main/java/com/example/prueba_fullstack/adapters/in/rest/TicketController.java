package com.example.prueba_fullstack.adapters.in.rest;

import com.example.prueba_fullstack.adapters.in.rest.dto.CancelRequest;
import com.example.prueba_fullstack.adapters.in.rest.dto.StatusChangeRequest;
import com.example.prueba_fullstack.adapters.in.rest.dto.TicketHistoryResponse;
import com.example.prueba_fullstack.adapters.in.rest.dto.TicketPageResponse;
import com.example.prueba_fullstack.adapters.in.rest.dto.TicketRequest;
import com.example.prueba_fullstack.adapters.in.rest.dto.TicketResponse;
import com.example.prueba_fullstack.application.port.in.TicketCommand;
import com.example.prueba_fullstack.application.port.in.TicketCriteria;
import com.example.prueba_fullstack.application.port.in.TicketUseCase;
import com.example.prueba_fullstack.domain.enums.Category;
import com.example.prueba_fullstack.domain.enums.Priority;
import com.example.prueba_fullstack.domain.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "${frontend.origin:http://localhost:5173}")
@Validated
public class TicketController {

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("id", "title", "requestUser", "category", "priority", "status", "createdAt", "updatedAt");

    private final TicketUseCase ticketUseCase;

    public TicketController(TicketUseCase ticketUseCase) {
        this.ticketUseCase = ticketUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody TicketRequest request) {
        return TicketResponse.from(ticketUseCase.create(toCommand(request)));
    }

    @GetMapping("/{id}")
    public TicketResponse getById(@PathVariable @Min(1) Long id) {
        return TicketResponse.from(ticketUseCase.getById(id));
    }

    @GetMapping
    public TicketPageResponse search(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        if (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("sortDirection must be asc or desc");
        }
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Unsupported sortBy field: " + sortBy);
        }
        return TicketPageResponse.from(ticketUseCase.search(
                new TicketCriteria(status, priority, category, page, size, sortBy, sortDirection)));
    }

    @PutMapping("/{id}")
    public TicketResponse update(@PathVariable @Min(1) Long id, @Valid @RequestBody TicketRequest request) {
        return TicketResponse.from(ticketUseCase.update(id, toCommand(request)));
    }

    @PatchMapping("/{id}/status")
    public TicketResponse changeStatus(@PathVariable @Min(1) Long id,
                                       @Valid @RequestBody StatusChangeRequest request) {
        return TicketResponse.from(ticketUseCase.changeStatus(id, request.status(), request.responsibleUser(),
                request.observation()));
    }

    @PostMapping("/{id}/cancel")
    public TicketResponse cancel(@PathVariable @Min(1) Long id, @Valid @RequestBody CancelRequest request) {
        return TicketResponse.from(ticketUseCase.cancel(id, request.responsibleUser(), request.observation()));
    }

    @GetMapping("/{id}/history")
    public List<TicketHistoryResponse> history(@PathVariable @Min(1) Long id) {
        return ticketUseCase.getHistory(id).stream().map(TicketHistoryResponse::from).toList();
    }

    private static TicketCommand toCommand(TicketRequest request) {
        return new TicketCommand(request.title(), request.description(), request.requestUser(),
                request.notificationPhone(), request.category(), request.priority());
    }
}
