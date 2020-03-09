import json
import logging
import os
import boto3

from aws_xray_sdk.core import patch_all

patch_all()

logger = logging.getLogger()
logger.setLevel(logging.INFO)

dynamoDB = boto3.resource('dynamodb')
s3 = boto3.resource('s3')


def handle_request(event, context):
    logger.info(f"FnUploadMovieInfos.fn_add_movies started remaining_time_in_millis = {context.get_remaining_time_in_millis()}")

    try:
        result = upload_movies(event, os.environ["MOVIE_INFOS_BUCKET"], os.environ["MOVIE_INFOS_TABLE"])
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 200,
            "body": str(result)
        }
    except Exception as e:
        logger.error(f"Error uploading movie infos: {str(e)}")
        return {
            "headers": {
                "Content-Type": "application/json"
            },
            "statusCode": 500,
            "body": f"{str(e)}"
        }


def upload_movies(event, movies_bucket, movies_table):

    result = 0

    for record in event["Records"]:

        bucket_name = record["s3"]["bucket"]["name"]
        logger.info(f"bucket_name = {bucket_name}")

        if movies_bucket.lower() != bucket_name.lower():
            continue

        key = record["s3"]["object"]["key"]
        logger.info(f"key = {key}")

        s3_object = s3.Object(bucket_name, key)
        body = s3_object.get()["Body"]

        table = dynamoDB.Table(movies_table)

        with table.batch_writer() as batch:
            for line in body._raw_stream:
                str_line = line.decode("utf-8")
                batch.put_item(Item=get_item(str_line))
                result += 1

    return result


def get_item(line):

    parts = line.split(",")

    item = {}

    if parts[0]:
        item["movie_id"] = parts[0]

    if parts[1]:
        item["name"] = parts[1]

    if parts[2]:
        item["country_of_origin"] = parts[2]

    if parts[3]:
        item["release_date"] = parts[3]

    return json.loads(json.dumps(item))
