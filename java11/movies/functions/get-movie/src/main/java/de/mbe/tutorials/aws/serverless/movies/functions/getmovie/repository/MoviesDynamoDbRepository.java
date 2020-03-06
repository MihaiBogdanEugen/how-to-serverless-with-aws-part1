package de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository.models.Movie;
import de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository.models.MovieInfo;
import de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository.models.MovieRating;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBMapperConfig readMovieRatingConfig;
    private final DynamoDBMapperConfig readMovieInfoConfig;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieInfosTable, final String movieRatingsTable) {

        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        final var readMovieRatingConfigBuilder = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        if (!isNullOrEmpty(movieRatingsTable)) {
            readMovieRatingConfigBuilder.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(movieRatingsTable));
        }

        this.readMovieRatingConfig = readMovieRatingConfigBuilder.build();

        final var readMovieInfoConfigBuilder = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        if (!isNullOrEmpty(movieInfosTable)) {
            readMovieInfoConfigBuilder.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(movieInfosTable));
        }

        this.readMovieInfoConfig = readMovieInfoConfigBuilder.build();
    }

    public Movie getByMovieId(final String movieId) {

        final var movieInfo = this.dynamoDBMapper.load(MovieInfo.class, movieId, this.readMovieInfoConfig);
        final var movieRating = this.dynamoDBMapper.load(MovieRating.class, movieId, this.readMovieRatingConfig);

        final var movie = new Movie();

        if (movieInfo == null && movieRating == null) {
            return null;
        }

        if (movieInfo != null) {
            movie.setName(movieInfo.getName());
            movie.setCountryOfOrigin(movieInfo.getCountryOfOrigin());
            movie.setReleaseDate(movieInfo.getReleaseDate());
        }

        if (movieRating != null) {
            movie.setImdbRating(movieRating.getImdbRating());
            movie.setRottenTomatoesRating(movieRating.getRottenTomatoesRating());
        }

        movie.setId(movieId);
        return movie;
    }
}
