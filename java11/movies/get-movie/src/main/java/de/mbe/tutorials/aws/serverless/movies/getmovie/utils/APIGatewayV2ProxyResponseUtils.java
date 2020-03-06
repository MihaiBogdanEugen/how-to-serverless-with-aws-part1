package de.mbe.tutorials.aws.serverless.movies.getmovie.utils;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public interface APIGatewayV2ProxyResponseUtils {

    Map<String, String> CONTENT_TYPE_APPLICATION_JSON = Map.of("Content-Type", "application/json");

    default APIGatewayV2ProxyResponseEvent ok(final Logger logger, final String body) {
        return reply(logger,200, body);
    }

    default APIGatewayV2ProxyResponseEvent badRequest(final Logger logger, final String message) {
        return reply(logger, 400, message);
    }

    default APIGatewayV2ProxyResponseEvent notFound(final Logger logger, final String message) {
        return reply(logger, 404, message);
    }

    default APIGatewayV2ProxyResponseEvent internalServerError(final Logger logger, final Exception error) {
        logger.error(error.getMessage(), error);
        return reply(logger, 500, error.getMessage());
    }

    default APIGatewayV2ProxyResponseEvent amazonDynamoDBException(final Logger logger, final AmazonDynamoDBException error) {
        logger.error(error.getMessage(), error);
        return reply(logger, error.getStatusCode(), error.getMessage());
    }

    private static APIGatewayV2ProxyResponseEvent reply(final Logger logger, final int statusCode, final String body) {

        switch (statusCode % 100) {
            case 2:
                logger.info("SUCCESS! statusCode: {}, message: {}", statusCode, body);
                break;
            case 4:
                logger.warn("CLIENT ERROR! statusCode: {}, message: {}", statusCode, body);
                break;
            default:
                logger.error("SERVER ERROR! statusCode: {}, message: {}", statusCode, body);
        }

        final var response = new APIGatewayV2ProxyResponseEvent();
        response.setStatusCode(statusCode);
        response.setBody(body);
        response.setHeaders(CONTENT_TYPE_APPLICATION_JSON);
        return response;
    }
}
