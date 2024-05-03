package org.dev.quickshortapi.infraestructure.adapter.out.persistence;

import com.mongodb.client.result.UpdateResult;
import org.dev.quickshortapi.application.port.out.IUrlMongoTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class UrlMongoTemplate implements IUrlMongoTemplate {

    private final  MongoTemplate mongoTemplate;
    private static final String ID = "_id";
    private static final String LAST_VISITED_DATE = "lastVisitedDate";
    private static final String VISITS = "visits";

    public UrlMongoTemplate(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UpdateResult updateVisitsByIncrementAndLastVisitedDate(String id, int visitsToIncrease, Date lastVisitedDate) {
        Query query = new Query(Criteria.where(ID).is(id));
        Update update = new Update().inc(VISITS, visitsToIncrease)
                .set(LAST_VISITED_DATE, lastVisitedDate);
      return mongoTemplate.updateFirst(query, update, UrlEntity.class);
    }

}
