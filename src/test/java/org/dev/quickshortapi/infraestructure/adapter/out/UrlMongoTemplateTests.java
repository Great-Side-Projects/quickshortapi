package org.dev.quickshortapi.infraestructure.adapter.out;

import com.mongodb.client.result.UpdateResult;
import org.dev.quickshortapi.domain.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMongoTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class UrlMongoTemplateTests {

    private UrlMongoTemplate urlMongoTemplate;
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        urlMongoTemplate = new UrlMongoTemplate(mongoTemplate);
    }

    @Test
    void updateVisitsByIncrementAndLastVisitedDateReturnsExpectedResult() {
        UpdateResult updateResult = UpdateResult.acknowledged(1, 1L, null);
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenReturn(updateResult);
        UpdateResult result = urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate("id", 1, new Date());
        assertThat(result).isEqualTo(updateResult);
    }

    @Test
    void updateVisitsByIncrementAndLastVisitedDateThrowsExceptionWhenMongoTemplateFails() {
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenThrow(new RuntimeException());
        assertThrows(UrlInternalServerErrorException.class, () -> urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate("id", 1, new Date()));
    }

    @Test
    void updateVisitsByIncrementAndLastVisitedDateReturnsNullWhenNoDocumentIsUpdated() {
        UpdateResult updateResult = UpdateResult.acknowledged(0, 0L, null);
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenReturn(updateResult);
        UpdateResult result = urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate("id", 1, new Date());
        assertThat(result).isEqualTo(updateResult);
    }

    @Test
    void updateVisitsByIncrementAndLastVisitedDateReturnsExpectedResultWhenMultipleVisitsIncreased() {
        UpdateResult updateResult = UpdateResult.acknowledged(1, 5L, null);
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenReturn(updateResult);
        UpdateResult result = urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate("id", 5, new Date());
        assertThat(result).isEqualTo(updateResult);
    }
}
