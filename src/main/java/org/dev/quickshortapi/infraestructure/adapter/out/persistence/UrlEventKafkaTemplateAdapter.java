package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlEventKafkaTemplateAdapter implements IUrlEventTemplatePort<Event<UrlEvent>> {

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    public UrlEventKafkaTemplateAdapter(KafkaTemplate<String, Event<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(Event<UrlEvent> event) {
        kafkaTemplate.send(TOPIC_NAME, event.getId(), event);
    }
}
