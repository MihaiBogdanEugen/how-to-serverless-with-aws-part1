package de.mbe.tutorials.aws.serverless.movies.functions.getmovie;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mbe.tutorials.aws.serverless.movies.functions.getmovie.repository.MoviesDynamoDbRepository;
import de.mbe.tutorials.aws.serverless.movies.functions.getmovie.utils.APIGatewayV2ProxyResponseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class FnGetMovie implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>, APIGatewayV2ProxyResponseUtils {

    private static final Logger LOGGER = LogManager.getLogger(FnGetMovie.class);

    private final ObjectMapper MAPPER = new ObjectMapper();

    private final MoviesDynamoDbRepository repository;

    public FnGetMovie() {

        final var amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .build();

        final var movieInfosTable = System.getenv("MOVIE_INFOS_TABLE");
        final var movieRatingsTable = System.getenv("MOVIE_RATINGS_TABLE");

        this.repository = new MoviesDynamoDbRepository(amazonDynamoDB, movieInfosTable, movieRatingsTable);
    }

    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent request, Context context) {

        LOGGER.info("FnGetMovie.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());

        if (!request.getHttpMethod().equalsIgnoreCase("get")) {
            return methodNotAllowed(LOGGER, "Method " + request.getHttpMethod() + " not allowed");
        }

        if (!request.getPathParameters().containsKey("id") || isNullOrEmpty(request.getPathParameters().get("id"))) {
            return badRequest(LOGGER, "Missing {id} path parameter");
        }

        final var id = request.getPathParameters().get("id");
        LOGGER.info("Retrieving movie {}", id);

        try {

            final var movie = repository.getByMovieId(id);

            if (movie == null) {
                return notFound(LOGGER, "Movie " + id + " not found");
            }

            final var movieAsString = MAPPER.writeValueAsString(movie);
            return ok(LOGGER, movieAsString);

        } catch (AmazonDynamoDBException error) {
            return amazonDynamoDBException(LOGGER, error);
        } catch (Exception error) {
            return internalServerError(LOGGER, error);
        }
    }
}
