package de.mbe.tutorials.aws.serverless.movies.updatemovierating.config;

import dagger.Component;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.FnUpdateMovieRating;

import javax.inject.Singleton;

@Singleton
@Component(modules = {FnModule.class})
public interface FnComponent {
    void inject(FnUpdateMovieRating fnUpdateMovieRating);
}
