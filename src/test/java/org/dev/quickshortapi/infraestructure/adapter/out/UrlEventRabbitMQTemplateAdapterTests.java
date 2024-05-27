package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEventRabbitMQTemplateAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.mockito.ArgumentMatchers.*;

class UrlEventRabbitMQTemplateAdapterTests {

    private UrlEventRabbitMQTemplateAdapter urlEventRabbitMQTemplateAdapter;
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setup() {
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        urlEventRabbitMQTemplateAdapter = new UrlEventRabbitMQTemplateAdapter(rabbitTemplate);
    }

    @Test
    void sendSendsUrlEvent() {
        String urlEvent = "urlEvent";
        urlEventRabbitMQTemplateAdapter.send(urlEvent);
        Mockito.verify(rabbitTemplate, Mockito.times(1)).convertAndSend(any(), eq(urlEvent));
    }
}