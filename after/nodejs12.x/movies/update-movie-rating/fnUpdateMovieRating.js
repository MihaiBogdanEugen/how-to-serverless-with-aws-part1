const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))
const DynamoDBClient = new AWS.DynamoDB.DocumentClient()

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnUpdateMovieRating.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)

    if (!event['pathParameters'].hasOwnProperty('movieId')) {
        LOGGER.info('Missing path parameter {movieId}')
        return {
            'statusCode': 400,
            'headers': {
                'Content-Type': 'application/json'
            },
            'body': 'Missing path parameter {movieId}'
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