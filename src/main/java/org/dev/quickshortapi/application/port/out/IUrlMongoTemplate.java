package org.dev.quickshortapi.application.port.out;

import com.mongodb.client.result.UpdateResult;

import java.util.Date;

public interface IUrlMongoTemplate {

UpdateResult updateVisitsByIncrementAndLastVisitedDate(String id, int visits, Date lastVisitedDate);
}
