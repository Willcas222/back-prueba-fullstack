package com.tuempresa.soporte.domain.port.in;

import com.tuempresa.soporte.domain.model.Categoria;
import com.tuempresa.soporte.domain.model.Prioridad;

public record SolicitudCommand(String title, String description, String requestUser, String notificationPhone,
                            Categoria category, Prioridad priority) {
}
