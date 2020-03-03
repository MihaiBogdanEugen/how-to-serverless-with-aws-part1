package de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.models.MovieRating;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final String movieRatingsTable;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieRatingsTable) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        this.movieRatingsTable = movieRatingsTable;
    }

    public void updateMovieRating(final MovieRating movieRating) {

        final var writeToMovieRatingConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE);

        if (!isNullOrEmpty(this.movieRatingsTable)) {
            writeToMovieRatingConfig.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.movieRatingsTable));
        }

        this.dynamoDBMapper.save(movieRating, writeToMovieRatingConfig.build());
    }
}
