const pino = require('pino')
const LOGGER = pino({ level: process.env.LOG_LEVEL || 'info' })

exports.lambdaHandler = async (event, context) => {
    LOGGER.info(`FnUpdateMovieRating.RemainingTimeInMillis ${context.getRemainingTimeInMillis()}`)
}