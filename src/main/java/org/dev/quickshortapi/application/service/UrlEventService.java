package org.dev.quickshortapi.application.service;

import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.common.Event.Event;
import org.dev.quickshortapi.domain.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlEventService implements IUrlEventTemplatePort<String, Event<Url>> {

    private KafkaTemplate<String, Event<?>> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    public UrlEventService(KafkaTemplate<String, Event<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String key, Event<Url> data) {
        kafkaTemplate.send(TOPIC_NAME, key, data);
    }
}
