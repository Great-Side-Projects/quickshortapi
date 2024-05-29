package org.dev.quickshortapi.infraestructure.adapter.out;

import com.mongodb.client.result.UpdateResult;
import org.dev.quickshortapi.domain.exceptionhandler.UrlInternalServerErrorException;
import org.dev.quickshortapi.infraestructure.adapter.out.persistence.UrlMongoTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Date;
import java.util.stream.Stream;
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
    void updateVisitsByIncrementAndLastVisitedDateThrowsExceptionWhenMongoTemplateFails() {
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenThrow(new RuntimeException());
        assertThrows(UrlInternalServerErrorException.class, () -> invokeUpdateVisitsByIncrementAndLastVisitedDate());
    }

    private UpdateResult invokeUpdateVisitsByIncrementAndLastVisitedDate() {
        try {
            return urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate("id", 1, new Date());
        } catch (RuntimeException e) {
            throw new UrlInternalServerErrorException("An error occurred while updating visits: " +  e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDataForTest")
    void updateVisitsByIncrementAndLastVisitedDateReturnsExpectedResult(String id, int increment, Date date, UpdateResult expectedUpdateResult) {
        Mockito.when(mongoTemplate.updateFirst(any(), any(), (Class<?>) any())).thenReturn(expectedUpdateResult);
        UpdateResult result = urlMongoTemplate.updateVisitsByIncrementAndLastVisitedDate(id, increment, date);
        assertThat(result).isEqualTo(expectedUpdateResult);
    }

    private static Stream<Arguments> provideDataForTest() {
        return Stream.of(
                Arguments.of("id", 1, new Date(), UpdateResult.acknowledged(1, 1L, null)),
                Arguments.of("id", 1, new Date(), UpdateResult.acknowledged(0, 0L, null)),
                Arguments.of("id", 5, new Date(), UpdateResult.acknowledged(1, 5L, null))
        );
    }
}
