package org.dev.quickshortapi.infraestructure.adapter.in.web;

import org.dev.quickshortapi.domain.event.UrlEvent;
import org.dev.quickshortapi.domain.event.UrlVisitedEvent;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component("kafka")
public class KafkaHealthCheck implements HealthIndicator {

  private final KafkaTemplate<String, Object> KafkaTemplate;
  private String test_topic;

   public KafkaHealthCheck(KafkaTemplate<String, Object> kafkaTemplate,
                           @Value("${spring.kafka.topic.name}") String test_topic) {
       this.test_topic = test_topic;
       KafkaTemplate = kafkaTemplate;
       this.getHealth(true);
    }

    @Override
    public Health health() {
        try {
            // ping message to kafka
            UrlEvent urlEvent = new UrlEvent("1", "http://ping.com", "ping", new Date());
            UrlVisitedEvent urlVisitedEvent = UrlMapper.toUrlVisitedEvent(urlEvent);
            KafkaTemplate.send(test_topic, urlVisitedEvent).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException | NullPointerException e) {
          return Health.down(e).build();
        }
        return Health.up().build();
    }
}
