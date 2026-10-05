package com.example.prueba_fullstack.application.config;

import com.example.prueba_fullstack.application.port.in.TicketUseCase;
import com.example.prueba_fullstack.application.port.out.TicketNotificationPort;
import com.example.prueba_fullstack.application.port.out.TicketRepositoryPort;
import com.example.prueba_fullstack.application.service.TicketApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    TicketUseCase ticketUseCase(TicketRepositoryPort repository, TicketNotificationPort notification) {
        return new TicketApplicationService(repository, notification);
    }

    @Bean
    @ConditionalOnMissingBean(TicketNotificationPort.class)
    TicketNotificationPort ticketNotificationPort() {
        return ticket -> { };
    }
}
