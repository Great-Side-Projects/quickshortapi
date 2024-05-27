package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEventKafkaTemplateAdapter;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UrlEventKafkaTemplateAdapterTests {

    private UrlEventKafkaTemplateAdapter urlEventKafkaTemplateAdapter;
    private KafkaTemplate<String, Event<?>> kafkaTemplate;

    @BeforeEach
    void setup() {
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        urlEventKafkaTemplateAdapter = new UrlEventKafkaTemplateAdapter(kafkaTemplate);
    }

    @Test
    void sendSendsEventToKafka() {
        Url url = new Url();
        url.setOriginalUrl("www.google.com");
        url.setShortUrl("HKl_v");
        url.setLastVisitedDate(new Date());
        UrlEvent urlEvent = UrlMapper.toUrlEvent(url);
        Event<UrlEvent> visited = UrlMapper.toUrlVisitedEvent(urlEvent);
        urlEventKafkaTemplateAdapter.send(visited);
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(any(), any(), eq(visited));
    }
}
