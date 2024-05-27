package org.dev.quickshortapi.infraestructure.adapter.out;

import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.event.UrlVisitedEvent;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlEventStreamingAdapter;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class UrlEventStreamingAdapterTests {

    private UrlEventStreamingAdapter urlEventStreamingAdapter;
    private IUrlPersistencePort urlPersistenceAdapter;
    private IUrlEventTemplatePort<Event<UrlEvent>> urlEventKafkaTemplateAdapter;
    private IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter;

    @BeforeEach
    void setup() {
        urlPersistenceAdapter = Mockito.mock(IUrlPersistencePort.class);
        urlEventKafkaTemplateAdapter = Mockito.mock(IUrlEventTemplatePort.class);
        urlEventRabbitMQTemplateAdapter = Mockito.mock(IUrlEventTemplatePort.class);
        urlEventStreamingAdapter = new UrlEventStreamingAdapter(urlEventKafkaTemplateAdapter, urlPersistenceAdapter, urlEventRabbitMQTemplateAdapter);
    }

    @Test
    void sendVisitedEventSendsEvent() {
        Url url = new Url();
        url.setOriginalUrl("www.google.com");
        url.setShortUrl("HKl_v");
        urlEventStreamingAdapter.sendVisitedEvent(url);
        Mockito.verify(urlEventKafkaTemplateAdapter, times(1)).send(any());
    }

    @Test
    void fallbackVisitedEventSendsEvent() {
        Url url = new Url();
        url.setOriginalUrl("www.google.com");
        url.setShortUrl("HKl_v");
        urlEventStreamingAdapter.fallbackVisitedEvent(url, new Exception("Test Exception"));
        Mockito.verify(urlEventRabbitMQTemplateAdapter, times(1)).send(any());
    }

    @Test
    void visitedEventIncreasesVisits() {
        Url url = new Url();
        url.setOriginalUrl("www.google.com");
        url.setShortUrl("HKl_v");
        url.setLastVisitedDate(new Date());
        UrlEvent urlEvent = UrlMapper.toUrlEvent(url);
        Event<UrlEvent> visited = UrlMapper.toUrlVisitedEvent(urlEvent);
        urlEventStreamingAdapter.visitedEvent(visited);
        Mockito.verify(urlPersistenceAdapter, times(1)).increaseVisitsAndUpdateLastVisitedDate(any());
    }
}

