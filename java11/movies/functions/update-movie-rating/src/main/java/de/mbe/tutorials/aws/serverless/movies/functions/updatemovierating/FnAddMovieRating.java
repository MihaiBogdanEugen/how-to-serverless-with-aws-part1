package de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.repository.MoviesDynamoDbRepository;
import de.mbe.tutorials.aws.serverless.movies.functions.updatemovierating.utils.APIGatewayV2ProxyResponseUtils;
import de.mbe.tutorials.aws.serverless.movies.models.MovieRating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class FnAddMovieRating implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>, APIGatewayV2ProxyResponseUtils {

    private static final Logger LOGGER = LogManager.getLogger(FnAddMovieRating.class);
    private final ObjectMapper MAPPER = new ObjectMapper();

    private final MoviesDynamoDbRepository repository;

    public FnAddMovieRating() {

        final var amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .build();

        final var movieRatingsTable = System.getenv("MOVIE_RATINGS_TABLE");

        this.repository = new MoviesDynamoDbRepository(amazonDynamoDB, movieRatingsTable);
    }

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent request, Context context) {

        LOGGER.info("FnAddMovieRating.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());

        if (!request.getHttpMethod().equalsIgnoreCase("patch")) {
            return methodNotAllowed(LOGGER, "Method " + request.getHttpMethod() + " not allowed");
        }

        if (!request.getPathParameters().containsKey("id") || isNullOrEmpty(request.getPathParameters().get("id"))) {
            return badRequest(LOGGER, "Missing {id} path parameter");
        }

        final var id = request.getPathParameters().get("id");
        LOGGER.info("Patching movie {}", id);

        try {

            final var movieRating = MAPPER.readValue(request.getBody(), MovieRating.class);
            this.repository.updateMovieRating(movieRating);
            return ok(LOGGER, "SUCCESS");

        } catch (AmazonDynamoDBException error) {
            return amazonDynamoDBException(LOGGER, error);
        } catch (Exception error) {
            return internalServerError(LOGGER, error);
        }
    }
}
