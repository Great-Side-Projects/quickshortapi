package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.common.event.Event;
import org.dev.quickshortapi.common.event.UrlVisitedEvent;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.domain.Url;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Date;

@PersistenceAdapter
public class UrlEventStreamingAdapter implements IUrlEventStreamingPort {

    private final IUrlPersistencePort urlPersistenceAdapter;
    private final IUrlEventTemplatePort urlEventTemplateAdapter;

    public UrlEventStreamingAdapter(IUrlEventTemplatePort urlEventTemplateAdapter, UrlPersistenceAdapter urlPersistenceAdapter) {
        this.urlEventTemplateAdapter = urlEventTemplateAdapter;
        this.urlPersistenceAdapter = urlPersistenceAdapter;
    }

    @Override
    public void sendVisitedEvent(Url url) {
        url.setLastVisitedDate(new Date());
        UrlVisitedEvent visited =  UrlMapper.toUrlVisitedEvent(url);
        urlEventTemplateAdapter.send(visited.getId(), visited);
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    @Override
    public void visitedEvent(Event<Url> url) {
        urlPersistenceAdapter.increaseVisitsAndUpdateLastVisitedDate(url.getData());
        System.out.println("Visitas incrementadas: " + url.getData().getShortUrl() + " - " + url.getId());
    }
}
