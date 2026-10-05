package com.tuempresa.soporte.application.service;

import com.tuempresa.soporte.domain.exception.SolicitudNotFoundException;
import com.tuempresa.soporte.domain.model.Estado;
import com.tuempresa.soporte.domain.model.HistorialCambios;
import com.tuempresa.soporte.domain.model.Solicitud;
import com.tuempresa.soporte.domain.port.in.BuscarSolicitudesUseCase;
import com.tuempresa.soporte.domain.port.in.CambiarEstadoSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.CancelarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.ConsultarHistorialUseCase;
import com.tuempresa.soporte.domain.port.in.ConsultarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.CrearSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.EditarSolicitudUseCase;
import com.tuempresa.soporte.domain.port.in.SolicitudCommand;
import com.tuempresa.soporte.domain.port.in.SolicitudCriteria;
import com.tuempresa.soporte.domain.port.in.SolicitudPage;
import com.tuempresa.soporte.domain.port.out.HistorialCambiosRepositoryPort;
import com.tuempresa.soporte.domain.port.out.SmsNotificationPort;
import com.tuempresa.soporte.domain.port.out.SolicitudRepositoryPort;

import java.util.List;

public class SolicitudService implements CrearSolicitudUseCase, ConsultarSolicitudUseCase,
        BuscarSolicitudesUseCase, EditarSolicitudUseCase, CambiarEstadoSolicitudUseCase,
        CancelarSolicitudUseCase, ConsultarHistorialUseCase {

    private final SolicitudRepositoryPort solicitudRepository;
    private final HistorialCambiosRepositoryPort historialRepository;
    private final SmsNotificationPort smsNotification;

    public SolicitudService(SolicitudRepositoryPort solicitudRepository,
                            HistorialCambiosRepositoryPort historialRepository,
                            SmsNotificationPort smsNotification) {
        this.solicitudRepository = solicitudRepository;
        this.historialRepository = historialRepository;
        this.smsNotification = smsNotification;
    }

    @Override
    public Solicitud create(SolicitudCommand command) {
        return solicitudRepository.save(Solicitud.create(command.title(), command.description(), command.requestUser(),
                command.notificationPhone(), command.category(), command.priority()));
    }

    @Override
    public Solicitud getById(Long id) {
        return solicitudRepository.findById(id).orElseThrow(() -> new SolicitudNotFoundException(id));
    }

    @Override
    public SolicitudPage search(SolicitudCriteria criteria) {
        return solicitudRepository.findPage(criteria);
    }

    @Override
    public Solicitud update(Long id, SolicitudCommand command) {
        Solicitud solicitud = getById(id);
        solicitud.edit(command.title(), command.description(), command.requestUser(), command.notificationPhone(),
                command.category(), command.priority());
        return solicitudRepository.save(solicitud);
    }

    @Override
    public Solicitud changeEstado(Long id, Estado estado, String usuarioResponsable, String observacion) {
        Solicitud solicitud = getById(id);
        Estado estadoAnterior = solicitud.getEstado();
        solicitud.changeEstado(estado, observacion);
        HistorialCambios historial = HistorialCambios.create(
                id, estadoAnterior, estado, usuarioResponsable, observacion);
        solicitudRepository.saveEstadoChange(solicitud, historial);
        smsNotification.statusChanged(solicitud);
        return solicitud;
    }

    @Override
    public Solicitud cancel(Long id, String usuarioResponsable, String observacion) {
        return changeEstado(id, Estado.CANCELADA, usuarioResponsable, observacion);
    }

    @Override
    public List<HistorialCambios> getHistory(Long id) {
        getById(id);
        return historialRepository.findBySolicitudId(id);
    }
}
