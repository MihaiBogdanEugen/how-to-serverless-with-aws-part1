package de.mbe.tutorials.aws.serverless.movies.updatemovierating.repository.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "movie_ratings")
public final class MovieRating {

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "movie_id")
    private String movieId;

    @DynamoDBAttribute(attributeName = "rotten_tomatoes_rating")
    private Integer rottenTomatoesRating;

    @DynamoDBAttribute(attributeName = "imdb_rating")
    private Integer imdbRating;

    public MovieRating() { }

    public String getMovieId() {
        return movieId;
    }

    public Integer getRottenTomatoesRating() {
        return rottenTomatoesRating;
    }

    public Integer getImdbRating() {
        return imdbRating;
    }

    public void setMovieId(final String movieId) {
        this.movieId = movieId;
    }

    public void setRottenTomatoesRating(final Integer rottenTomatoesRating) {
        this.rottenTomatoesRating = rottenTomatoesRating;
    }

    public void setImdbRating(final Integer imdbRating) {
        this.imdbRating = imdbRating;
    }
}