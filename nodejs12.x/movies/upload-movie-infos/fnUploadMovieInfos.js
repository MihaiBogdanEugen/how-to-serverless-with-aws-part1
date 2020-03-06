const AWS = require('aws-sdk')

const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

const AWSXRay = require("aws-xray-sdk-core")
AWSXRay.captureHTTPsGlobal(require("http"));

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnUploadMovieInfos.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)

}