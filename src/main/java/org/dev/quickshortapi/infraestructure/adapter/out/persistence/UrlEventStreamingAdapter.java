package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
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
    private final IUrlEventTemplatePort<String, Event<UrlEvent>> urlEventTemplateAdapter;

    Logger logger = Logger.getLogger(getClass().getName());

    public UrlEventStreamingAdapter(IUrlEventTemplatePort<String,Event<UrlEvent>>  urlEventTemplateAdapter, UrlPersistenceAdapter urlPersistenceAdapter) {
        this.urlEventTemplateAdapter = urlEventTemplateAdapter;
        this.urlPersistenceAdapter = urlPersistenceAdapter;
    }

    @Override
    public void sendVisitedEvent(UrlEvent urlEvent) {
        urlEvent.setLastVisitedDate(new Date());
        UrlVisitedEvent visited =  UrlMapper.toUrlVisitedEvent(urlEvent);
        urlEventTemplateAdapter.send(visited.getId(), visited);
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    @Override
    public void visitedEvent(Event<UrlEvent> urlEvent) {
        urlPersistenceAdapter.increaseVisitsAndUpdateLastVisitedDate(urlEvent.getData());
        logger.log(Level.INFO, "Visitas incrementadas: {0} - {1}", new Object[]{urlEvent.getData().getShortUrl(), urlEvent.getId()});
    }
}
