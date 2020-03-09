package de.mbe.tutorials.aws.serverless.movies.updatemovierating.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.repository.MoviesDynamoDbRepository;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public final class FnModule {

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
        return new ObjectMapper();
    }

    @Singleton
    @Provides
    public MoviesDynamoDbRepository moviesDynamoDbRepository(
            final AmazonDynamoDB amazonDynamoDB,
            @Named("movieRatingsTable") final String movieRatingsTable) {
        return new MoviesDynamoDbRepository(amazonDynamoDB, movieRatingsTable);
    }
}