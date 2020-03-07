import json
import logging
import os
import boto3

from aws_xray_sdk.core import patch_all
patch_all()

logger = logging.getLogger()
logger.setLevel(logging.INFO)

dynamoDB = boto3.resource("dynamodb")


def handle_request(event, context):
    logger.info(f"FnUpdateMovieRating.RemainingTimeInMillis {context.get_remaining_time_in_millis()}")

    if "movieId" not in event["pathParameters"]:
        logger.info("Missing path parameter {movieId}")
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 400,
            "body": "Missing path parameter {movieId}"
        }

    movie_id = event["pathParameters"]["movieId"]
    logger.info(f"Patching movie {movie_id}")

    body = event["body"]
    if not body:
        logger.info("Missing body object")
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 400,
            "body": "Missing body object"
        }

    try:
        update_movie_rating(movie_id, json.loads(body), os.environ["MOVIE_RATINGS_TABLE"])
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 200,
            "body": "SUCCESS"
        }
    except Exception as e:
        logger.error(f"Error while updating stat {body} in DynamoDB, error: {str(e)}")
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 500,
            "body": f"{str(e)}"
        }


def update_movie_rating(movie_id, body, movie_ratings_table):
    update_expression = ""
    expression_attribute_values = {}

    if "rottenTomatoesRating" in body:
        update_expression += " rotten_tomatoes_rating = :rotten_tomatoes_rating,"
        expression_attribute_values[":rotten_tomatoes_rating"] = int(body["rottenTomatoesRating"])

    if "imdbRating" in body:
        update_expression += " imdb_rating = :imdb_rating,"
        expression_attribute_values[":imdb_rating"] = int(body["imdbRating"])

    if not update_expression:
        return

    update_expression = "SET" + update_expression
    update_expression = update_expression[:-1]

    expression_attribute_values = json.loads(json.dumps(expression_attribute_values))

    logger.info(f"UpdateExpression: {update_expression}")
    logger.info(f"ExpressionAttributeValues: {expression_attribute_values}")

    table = dynamoDB.Table(movie_ratings_table)
    return table.update_item(
        Key={
            "movie_id": movie_id
        },
        UpdateExpression=update_expression,
        ExpressionAttributeValues=expression_attribute_values
    )
