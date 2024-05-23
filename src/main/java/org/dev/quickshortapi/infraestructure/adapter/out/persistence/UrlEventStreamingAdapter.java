package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.domain.Url;
import org.dev.quickshortapi.domain.event.Event;
import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.event.UrlVisitedEvent;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@PersistenceAdapter
public class UrlEventStreamingAdapter implements IUrlEventStreamingPort {

    private final IUrlPersistencePort urlPersistenceAdapter;
    private final IUrlEventTemplatePort<Event<UrlEvent>> urlEventKafkaTemplateAdapter;
    private final IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter;
    private static final String MESSAGE_ERROR = "Error sending event to RabbitMQ {0}";

    Logger logger = Logger.getLogger(getClass().getName());

    public UrlEventStreamingAdapter(IUrlEventTemplatePort<Event<UrlEvent>>  urlEventKafkaTemplateAdapter, UrlPersistenceAdapter urlPersistenceAdapter, IUrlEventTemplatePort<String> urlEventRabbitMQTemplateAdapter) {
        this.urlEventKafkaTemplateAdapter = urlEventKafkaTemplateAdapter;
        this.urlPersistenceAdapter = urlPersistenceAdapter;
        this.urlEventRabbitMQTemplateAdapter = urlEventRabbitMQTemplateAdapter;
    }

    @Override
    @CircuitBreaker(name = "urlEventStreaming", fallbackMethod = "fallbackVisitedEvent")
    public void sendVisitedEvent(Url url) {
        url.setLastVisitedDate(new Date());
        UrlEvent urlEvent = UrlMapper.toUrlEvent(url);
        Event<UrlEvent> visited = UrlMapper.toUrlVisitedEvent(urlEvent);
        urlEventKafkaTemplateAdapter.send(visited);
    }

    public void fallbackVisitedEvent(Url url, Throwable t) {
        logger.log(Level.SEVERE, "Error sending visited event: {0}", t.getMessage());
        try {
            UrlVisitedEvent visited = UrlMapper.toUrlVisitedEvent(UrlMapper.toUrlEvent(url));
            String eventString = String.format("Error sending visited event: %s, Event: %s", t.getMessage(), visited);
            urlEventRabbitMQTemplateAdapter.send(eventString);
        } catch (Exception e) {
            logger.log(Level.SEVERE, MESSAGE_ERROR, e.getMessage());
        }
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    @Override
    public void visitedEvent(Event<UrlEvent> urlEvent) {
        urlPersistenceAdapter.increaseVisitsAndUpdateLastVisitedDate(urlEvent.getData());
        logger.log(Level.INFO, "Visitas incrementadas: {0} - {1}", new Object[]{urlEvent.getData().getShortUrl(), urlEvent.getId()});
    }
}
