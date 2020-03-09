package de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import dagger.Module;
import dagger.Provides;
import de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.repository.MoviesDynamoDbRepository;

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
    @Named("movieInfosBucket")
    String movieInfosBucket() {
        return System.getenv("MOVIE_INFOS_BUCKET");
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
    AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()))
                .build();
    }

    @Singleton
    @Provides
    public MoviesDynamoDbRepository moviesDynamoDbRepository(
            final AmazonDynamoDB amazonDynamoDB,
            @Named("movieInfosTable") final String movieInfosTable) {
        return new MoviesDynamoDbRepository(amazonDynamoDB, movieInfosTable);
    }
}