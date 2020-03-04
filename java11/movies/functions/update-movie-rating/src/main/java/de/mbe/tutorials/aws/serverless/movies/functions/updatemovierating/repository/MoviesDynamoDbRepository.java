package de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.repository;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.models.MovieRating;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final String movieRatingsTable;
    private final DynamoDBMapperConfig writeToMovieRatingConfig;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieRatingsTable) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        this.movieRatingsTable = movieRatingsTable;

        final var writeToMovieRatingConfigBuilder = DynamoDBMapperConfig.builder()
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE);

        if (!isNullOrEmpty(this.movieRatingsTable)) {
            writeToMovieRatingConfigBuilder.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.movieRatingsTable));
        }
        writeToMovieRatingConfig = writeToMovieRatingConfigBuilder.build();
    }

    public void updateMovieRating(final MovieRating movieRating) {
        this.dynamoDBMapper.save(movieRating, writeToMovieRatingConfig);
    }
}
