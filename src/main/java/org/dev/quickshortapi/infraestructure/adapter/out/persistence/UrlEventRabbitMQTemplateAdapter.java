package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlEventRabbitMQTemplateAdapter implements IUrlEventTemplatePort<String> {

   private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.name}")
    private String QUEUE_NAME;

    public UrlEventRabbitMQTemplateAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(String urlEvent) {
           rabbitTemplate.convertAndSend(QUEUE_NAME, urlEvent);
    }
}
