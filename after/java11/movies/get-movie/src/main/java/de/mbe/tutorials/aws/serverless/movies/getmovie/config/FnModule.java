package de.mbe.tutorials.aws.serverless.movies.getmovie.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dagger.Module;
import dagger.Provides;
import de.mbe.tutorials.aws.serverless.movies.getmovie.repository.MoviesDynamoDbRepository;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class FnModule {

    @Singleton
    @Provides
    @Named("movieInfosTable")
    String movieInfosTable() {
        return System.getenv("MOVIE_INFOS_TABLE");
    }

    @Singleton
    @Provides
    @Named("movieRatingsTable")
    String movieRatingsTable() {
        return System.getenv("MOVIE_RATINGS_TABLE");
    }

    @Singleton
    @Provides
    AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()))
                .build();
    }

    @Singleton
    @Provides
    ObjectMapper objectMapper() {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Singleton
    @Provides
    public MoviesDynamoDbRepository moviesDynamoDbRepository(
            final AmazonDynamoDB amazonDynamoDB,
            @Named("movieInfosTable") final String movieInfosTable,
            @Named("movieRatingsTable") final String movieRatingsTable) {
        return new MoviesDynamoDbRepository(amazonDynamoDB, movieInfosTable, movieRatingsTable);
    }
}