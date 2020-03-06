package de.mbe.tutorials.aws.serverless.movies.getmovie.repository.models;

import java.time.LocalDate;

public final class Movie {

    private String id;
    private String name;
    private String countryOfOrigin;
    private LocalDate releaseDate;
    private Integer rottenTomatoesRating;
    private Integer imdbRating;

    public Movie() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Integer getRottenTomatoesRating() {
        return rottenTomatoesRating;
    }

    public Integer getImdbRating() {
        return imdbRating;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCountryOfOrigin(final String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setReleaseDate(final LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRottenTomatoesRating(final Integer rottenTomatoesRating) {
        this.rottenTomatoesRating = rottenTomatoesRating;
    }

    public void setImdbRating(final Integer imdbRating) {
        this.imdbRating = imdbRating;
    }
}