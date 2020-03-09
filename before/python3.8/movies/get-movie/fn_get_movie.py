import logging

logger = logging.getLogger()
logger.setLevel(logging.INFO)


def handle_request(event, context):
    logger.info(f"FnGetMovie.RemainingTimeInMillis {context.get_remaining_time_in_millis()}")
