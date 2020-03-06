const AWS = require('aws-sdk')
const DynamoDBClient = new AWS.DynamoDB.DocumentClient();

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

const AWSXRay = require('aws-xray-sdk-core')
AWSXRay.captureHTTPsGlobal(require('http'));

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnGetMovie.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)

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
    LOGGER.info(`Retrieving movie ${movieId}`)

    try {

        docClient.get({
            TableName: table,
            Key:{
                "year": year,
                "title": title
            }
        }, function(err, data) {
            if (err) {
                console.error("Unable to read item. Error JSON:", JSON.stringify(err, null, 2));
            } else {
                console.log("GetItem succeeded:", JSON.stringify(data, null, 2));
            }
        });

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