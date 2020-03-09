package de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FnUploadMovieInfos implements RequestHandler<S3Event, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(FnUploadMovieInfos.class);

    @Override
    public Integer handleRequest(S3Event input, Context context) {
        LOGGER.info("FnUploadMovieInfos.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());
        return null;
    }
}
