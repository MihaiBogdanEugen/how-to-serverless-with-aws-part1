package de.mbe.tutorials.aws.serverless.movies.getmovie.config;

import dagger.Component;
import de.mbe.tutorials.aws.serverless.movies.getmovie.FnGetMovie;

import javax.inject.Singleton;

@Singleton
@Component(modules = {FnModule.class})
public interface FnComponent {
    void inject(FnGetMovie fnGetMovie);
}
