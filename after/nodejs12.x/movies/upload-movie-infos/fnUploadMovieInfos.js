const AWSXRay = require('aws-xray-sdk-core')
const AWS = AWSXRay.captureAWS(require('aws-sdk'))
const DynamoDBClient = new AWS.DynamoDB.DocumentClient()

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnUploadMovieInfos.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)
}