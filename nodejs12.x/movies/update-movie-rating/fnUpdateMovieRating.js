const AWS = require('aws-sdk')

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

const AWSXRay = require("aws-xray-sdk-core")
AWSXRay.captureHTTPsGlobal(require("http"));

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnUpdateMovieRating.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)

    if (!event['pathParameters'].hasOwnProperty('movieId')) {
        LOGGER.info('Missing path parameter movieId')
        return {
            'statusCode': 400,
            'headers': {
                'Content-Type': 'application/json'
            },
            'body': 'Missing {movieId} path parameter'
        }
    }

    movieId = event['pathParameters']['movieId']
    LOGGER.info(`Patching movie ${movieId}`)

    try {



    } catch (err) {
        LOGGER.error(err)
        return {
            'statusCode': 500,
            'headers': {
                'Content-Type': 'application/json'
            },
            'body': JSON.stringify(err)
        }
    }
}