package de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.repository.models.MovieRating;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBMapperConfig writeMovieRatingConfig;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieRatingsTable) {

        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        final var writeMovieRatingConfigBuilder = DynamoDBMapperConfig.builder()
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE);
        if (!isNullOrEmpty(movieRatingsTable)) {
            writeMovieRatingConfigBuilder.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(movieRatingsTable));
        }

        this.writeMovieRatingConfig = writeMovieRatingConfigBuilder.build();
    }

    public void updateMovieRating(final MovieRating movieRating) {
        this.dynamoDBMapper.save(movieRating, writeMovieRatingConfig);
    }
}
