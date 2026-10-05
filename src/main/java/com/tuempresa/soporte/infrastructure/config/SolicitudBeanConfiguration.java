package com.tuempresa.soporte.infrastructure.config;

import com.tuempresa.soporte.application.service.SolicitudService;
import com.tuempresa.soporte.domain.port.out.HistorialCambiosRepositoryPort;
import com.tuempresa.soporte.domain.port.out.SmsNotificationPort;
import com.tuempresa.soporte.domain.port.out.SolicitudRepositoryPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolicitudBeanConfiguration {

    @Bean
    public SolicitudService solicitudService(SolicitudRepositoryPort solicitudRepository,
                                             HistorialCambiosRepositoryPort historialRepository,
                                             SmsNotificationPort smsNotification) {
        return new SolicitudService(solicitudRepository, historialRepository, smsNotification);
    }

    @Bean
    @ConditionalOnMissingBean(SmsNotificationPort.class)
    public SmsNotificationPort smsNotificationPort() {
        return solicitud -> { };
    }
}
