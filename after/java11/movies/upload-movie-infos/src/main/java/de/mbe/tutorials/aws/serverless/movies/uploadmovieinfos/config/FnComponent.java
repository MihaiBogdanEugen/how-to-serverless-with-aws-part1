package de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.config;

import dagger.Component;
import de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.FnUploadMovieInfos;

import javax.inject.Singleton;

@Singleton
@Component(modules = {FnModule.class})
public interface FnComponent {
    void inject(FnUploadMovieInfos fnUploadMovieInfos);
}
