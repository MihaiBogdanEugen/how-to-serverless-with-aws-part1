package de.mbe.tutorials.aws.serverless.movies.updatemovierating;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.config.DaggerFnComponent;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.repository.MoviesDynamoDbRepository;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.repository.models.MovieRating;
import de.mbe.tutorials.aws.serverless.movies.updatemovierating.utils.APIGatewayV2ProxyResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class FnUpdateMovieRating implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>, APIGatewayV2ProxyResponseUtils {

    private static final Logger LOGGER = LogManager.getLogger(FnUpdateMovieRating.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MoviesDynamoDbRepository moviesDynamoDbRepository;

    public FnUpdateMovieRating() {
        DaggerFnComponent.builder().build().inject(this);
    }

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent request, Context context) {

        LOGGER.info("FnAddMovieRating.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());

        if (!request.getPathParameters().containsKey("movieId") || isNullOrEmpty(request.getPathParameters().get("movieId"))) {
            return badRequest(LOGGER, "Missing path parameter {movieId}");
        }

        final var movieId = request.getPathParameters().get("movieId");
        LOGGER.info("Patching movie {}", movieId);

        try {

            final var movieRating = objectMapper.readValue(request.getBody(), MovieRating.class);
            moviesDynamoDbRepository.updateMovieRating(movieRating);
            return ok(LOGGER, "SUCCESS");

        } catch (AmazonDynamoDBException error) {
            return amazonDynamoDBException(LOGGER, error);
        } catch (Exception error) {
            return internalServerError(LOGGER, error);
        }
    }
}
