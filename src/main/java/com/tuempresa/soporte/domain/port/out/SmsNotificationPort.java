package com.tuempresa.soporte.domain.port.out;

import com.tuempresa.soporte.domain.model.Solicitud;

public interface SmsNotificationPort {
    void statusChanged(Solicitud ticket);
}
