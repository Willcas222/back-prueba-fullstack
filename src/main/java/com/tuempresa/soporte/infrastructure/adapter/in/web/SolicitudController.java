package com.tuempresa.soporte.infrastructure.adapter.in.web;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Estado;
import com.tuempresa.soporte.domain.model.Prioridad;
import com.tuempresa.soporte.domain.port.in.BuscarSolicitudesUseCase;
import com.tuempresa.soporte.domain.port.in.CambiarEstadoSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.CancelarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.ConsultarHistorialUseCase;
import com.tuempresa.soporte.domain.port.in.ConsultarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.CrearSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.EditarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.SolicitudCriteria;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.CancelarSolicitudRequest;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.CambiarEstadoSolicitudRequest;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.HistorialCambiosResponse;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudPageResponse;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudRequest;
import com.tuempresa.soporte.infrastructure.adapter.in.web.dto.SolicitudResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class SolicitudController {

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("id", "title", "requestUser", "category", "priority", "status", "createdAt", "updatedAt");

    private final CrearSolicitudUseCase crearSolicitud;
    private final ConsultarSolicitudUseCase consultarSolicitud;
    private final BuscarSolicitudesUseCase buscarSolicitudes;
    private final EditarSolicitudUseCase editarSolicitud;
    private final CambiarEstadoSolicitudUseCase cambiarEstadoSolicitud;
    private final CancelarSolicitudUseCase cancelarSolicitud;
    private final ConsultarHistorialUseCase consultarHistorial;
    private final SolicitudDtoMapper mapper;

    public SolicitudController(CrearSolicitudUseCase crearSolicitud,
                               ConsultarSolicitudUseCase consultarSolicitud,
                               BuscarSolicitudesUseCase buscarSolicitudes,
                               EditarSolicitudUseCase editarSolicitud,
                               CambiarEstadoSolicitudUseCase cambiarEstadoSolicitud,
                               CancelarSolicitudUseCase cancelarSolicitud,
                               ConsultarHistorialUseCase consultarHistorial,
                               SolicitudDtoMapper mapper) {
        this.crearSolicitud = crearSolicitud;
        this.consultarSolicitud = consultarSolicitud;
        this.buscarSolicitudes = buscarSolicitudes;
        this.editarSolicitud = editarSolicitud;
        this.cambiarEstadoSolicitud = cambiarEstadoSolicitud;
        this.cancelarSolicitud = cancelarSolicitud;
        this.consultarHistorial = consultarHistorial;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse create(@Valid @RequestBody SolicitudRequest request) {
        return mapper.toResponse(crearSolicitud.create(mapper.toCommand(request)));
    }

    @GetMapping("/{id}")
    public SolicitudResponse getById(@PathVariable @Min(1) Long id) {
        return mapper.toResponse(consultarSolicitud.getById(id));
    }

    @GetMapping
    public SolicitudPageResponse search(
            @RequestParam(required = false) Estado status,
            @RequestParam(required = false) Prioridad priority,
            @RequestParam(required = false) Categoria category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        if (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("sortDirection debe ser asc o desc");
        }
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Campo de orden no permitido: " + sortBy);
        }
        SolicitudCriteria criteria = new SolicitudCriteria(
                status, priority, category, page, size, sortBy, sortDirection);
        return mapper.toPageResponse(buscarSolicitudes.search(criteria));
    }

    @PutMapping("/{id}")
    public SolicitudResponse update(@PathVariable @Min(1) Long id,
                                    @Valid @RequestBody SolicitudRequest request) {
        return mapper.toResponse(editarSolicitud.update(id, mapper.toCommand(request)));
    }

    @PatchMapping("/{id}/status")
    public SolicitudResponse changeEstado(@PathVariable @Min(1) Long id,
                                          @Valid @RequestBody CambiarEstadoSolicitudRequest request) {
        return mapper.toResponse(cambiarEstadoSolicitud.changeEstado(
                id, request.status(), request.responsibleUser(), request.observation()));
    }

    @PostMapping("/{id}/cancel")
    public SolicitudResponse cancel(@PathVariable @Min(1) Long id,
                                    @Valid @RequestBody CancelarSolicitudRequest request) {
        return mapper.toResponse(cancelarSolicitud.cancel(id, request.responsibleUser(), request.observation()));
    }

    @GetMapping("/{id}/history")
    public List<HistorialCambiosResponse> history(@PathVariable @Min(1) Long id) {
        return consultarHistorial.getHistory(id).stream().map(mapper::toResponse).toList();
    }
}
