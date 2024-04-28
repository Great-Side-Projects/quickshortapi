package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import org.dev.quickshortapi.application.port.out.IUrlEventStreamingPort;
import org.dev.quickshortapi.application.port.out.IUrlEventTemplatePort;
import org.dev.quickshortapi.application.port.out.IUrlPersistencePort;
import org.dev.quickshortapi.common.Event.Event;
import org.dev.quickshortapi.common.Event.UrlVisitedEvent;
import org.dev.quickshortapi.common.PersistenceAdapter;
import org.dev.quickshortapi.domain.Url;
import org.springframework.kafka.annotation.KafkaListener;

@PersistenceAdapter
public class UrlEventStreamingAdapter implements IUrlEventStreamingPort {

    private final IUrlPersistencePort urlPersistenceAdapter;
    private final IUrlEventTemplatePort urlEventTemplate;

    public UrlEventStreamingAdapter(IUrlEventTemplatePort urlEventTemplate, UrlPersistenceAdapter urlPersistenceAdapter) {
        this.urlEventTemplate = urlEventTemplate;
        this.urlPersistenceAdapter = urlPersistenceAdapter;
    }

    @Override
    public void sendVisitedEvent(Url url) {
        UrlVisitedEvent visited =  UrlMapper.toUrlVisitedEvent(url);
        urlEventTemplate.send(visited.getId(), visited);
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}",
            groupId = "quickshort")
    @Override
    public void visitedEvent(Event<Url> url) {
        urlPersistenceAdapter.increaseVisits(url.getData());
        System.out.println("Visitas incrementadas: " + url.getData().getShortUrl() + " - " + url.getId());
    }
}
