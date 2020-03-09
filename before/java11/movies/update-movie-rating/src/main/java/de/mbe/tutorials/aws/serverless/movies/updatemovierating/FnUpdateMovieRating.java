package de.mbe.tutorials.aws.serverless.movies.updatemovierating;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FnUpdateMovieRating implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

    private static final Logger LOGGER = LogManager.getLogger(FnUpdateMovieRating.class);

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent request, Context context) {
        LOGGER.info("FnAddMovieRating.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());
        return null;
    }
}
