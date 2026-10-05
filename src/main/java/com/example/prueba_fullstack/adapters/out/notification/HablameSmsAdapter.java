package com.example.prueba_fullstack.adapters.out.notification;

import com.example.prueba_fullstack.application.port.out.TicketNotificationPort;
import com.example.prueba_fullstack.domain.model.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(name = "notification.hablame.enabled", havingValue = "true")
public class HablameSmsAdapter implements TicketNotificationPort {

    private final RestClient restClient;
    private final String url;
    private final String customer;
    private final String apiKey;
    private final String password;
    private final String username;
    private final String flash;

    public HablameSmsAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${notification.hablame.url}") String url,
            @Value("${notification.hablame.customer}") String customer,
            @Value("${notification.hablame.api-key}") String apiKey,
            @Value("${notification.hablame.password}") String password,
            @Value("${notification.hablame.username}") String username,
            @Value("${notification.hablame.flash:true}") boolean flash) {
        this.restClient = restClientBuilder.build();
        this.url = requireCredential(url, "HABLAME_URL");
        this.customer = requireCredential(customer, "HABLAME_CUSTOMER");
        this.apiKey = requireCredential(apiKey, "HABLAME_API_KEY");
        this.password = requireCredential(password, "HABLAME_PASSWORD");
        this.username = requireCredential(username, "HABLAME_USERNAME");
        this.flash = flash ? "1" : "0";
    }

    @Override
    public void statusChanged(Ticket ticket) {
        String phone = ticket.getNotificationPhone();
        if (phone == null || phone.isBlank()) {
            throw new IllegalStateException("A notification phone is required when Hablame notifications are enabled");
        }
        HablameRequest request = new HablameRequest(customer, apiKey, password,
                new SmsPayload(flash, username, "La solicitud #" + ticket.getId() + " cambió a "
                        + ticket.getStatus() + ".", "ticket-" + ticket.getId(), "", "ticket-" + ticket.getId()),
                new String[]{phone});
        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    private static String requireCredential(String value, String variableName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(variableName + " must be configured when Hablame notifications are enabled");
        }
        return value;
    }

    private record HablameRequest(String cliente, String api, String password, SmsPayload SMS, String[] numeros) {
    }

    private record SmsPayload(String flash, String usuario, String texto,
                              String identificador, String fecha, String referencia) {
    }
}
