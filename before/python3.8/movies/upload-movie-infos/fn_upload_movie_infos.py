import logging

logger = logging.getLogger()
logger.setLevel(logging.INFO)


def handle_request(event, context):
    logger.info(f"FnUploadMovieInfos.fn_add_movies started remaining_time_in_millis = {context.get_remaining_time_in_millis()}")
