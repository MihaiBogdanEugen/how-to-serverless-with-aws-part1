package de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.models.Movie;
import de.mbe.tutorials.aws.serverless.movies.models.MovieInfo;
import de.mbe.tutorials.aws.serverless.movies.models.MovieRating;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final String movieInfosTable;
    private final String movieRatingsTable;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieInfosTable, final String movieRatingsTable) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        this.movieInfosTable = movieInfosTable;
        this.movieRatingsTable = movieRatingsTable;
    }

    public Movie getByMovieId(final String id) {

        final var readFromMovieInfosConfig = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);

        if (!isNullOrEmpty(this.movieInfosTable)) {
            readFromMovieInfosConfig.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.movieInfosTable));
        }

        final var movieInfo = this.dynamoDBMapper.load(MovieInfo.class, id, readFromMovieInfosConfig.build());

        final var readFromMovieRatingsConfig = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);

        if (!isNullOrEmpty(this.movieRatingsTable)) {
            readFromMovieRatingsConfig.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.movieRatingsTable));
        }

        final var movieRating = this.dynamoDBMapper.load(MovieRating.class, id, readFromMovieRatingsConfig.build());


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

        movie.setId(id);
        return movie;
    }
}
