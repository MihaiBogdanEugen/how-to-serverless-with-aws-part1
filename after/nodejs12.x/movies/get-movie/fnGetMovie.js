const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))
const DynamoDBClient = new AWS.DynamoDB.DocumentClient()

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnGetMovie.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)

    if (!event['pathParameters'].hasOwnProperty('movieId')) {
        LOGGER.info('Missing path parameter {movieId}')
        return {
            'headers': {
                'Content-Type': 'application/json'
            },
            'statusCode': 400,
            'body': 'Missing path parameter {movieId}'
        }
    }

    movieId = event['pathParameters']['movieId']
    LOGGER.info(`Retrieving movie ${movieId}`)

    movieInfoParams = {
        TableName: process.env.MOVIE_INFOS_TABLE,
        Key:{
            "movie_id": movieId
        }
    }

    movieRatingParams = {
        TableName: process.env.MOVIE_RATINGS_TABLE,
        Key:{
            "movie_id": movieId
        }
    }

    try {
        movieInfo = await DynamoDBClient.get(movieInfoParams).promise()
        movieRating = await DynamoDBClient.get(movieRatingParams).promise()
    } catch (err) {
        LOGGER.error(err)
        return {
            'headers': {
                'Content-Type': 'application/json'
            },            
            'statusCode': 500,
            'body': JSON.stringify(err)
        }
    }

    if (movieInfo == null && movieRating == null) {
        return {
            'headers': {
                'Content-Type': 'application/json'
            },
            'statusCode': 404,
            'body': `Movie ${movie_id} not found`
        }
    }
    
    if (movieInfo == null) {
        return movieRating
    }

    if (movieRating == null) {
        return movieInfo
    }

    if (movieRating.hasOwnProperty('rotten_tomatoes_rating')) {
        movieInfo['rotten_tomatoes_rating'] = movieRating['rotten_tomatoes_rating']
    }
    
    if (movieRating.hasOwnProperty('imdb_rating')) {
        movieInfo['imdb_rating'] = movieRating['imdb_rating']
    }

    return {
        'headers': {
            'Content-Type': 'application/json'
        },            
        'statusCode': 200,
        'body': JSON.stringify(movieInfo)
    }
}