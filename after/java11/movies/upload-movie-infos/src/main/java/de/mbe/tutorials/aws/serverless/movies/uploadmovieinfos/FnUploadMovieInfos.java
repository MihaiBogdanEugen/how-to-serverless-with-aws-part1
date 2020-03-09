package de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.config.DaggerFnComponent;
import de.mbe.tutorials.aws.serverless.movies.uploadmovieinfos.repository.MoviesDynamoDbRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class FnUploadMovieInfos implements RequestHandler<S3Event, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(FnUploadMovieInfos.class);

    @Inject
    @Named("movieInfosBucket") String movieInfosBucket;

    @Inject
    AmazonS3 amazonS3;

    @Inject
    MoviesDynamoDbRepository moviesDynamoDbRepository;

    public FnUploadMovieInfos() {
        DaggerFnComponent.builder().build().inject(this);
    }

    @Override
    public Integer handleRequest(S3Event input, Context context) {

        LOGGER.info("FnUploadMovieInfos.getRemainingTimeInMillis {} ", context.getRemainingTimeInMillis());

        var result = 0;
        final var lines = new ArrayList<String>();

        try {
            for (final var record : input.getRecords()) {

                final var s3Entity = record.getS3();
                final var bucketName = s3Entity.getBucket().getName();

                if (!bucketName.equalsIgnoreCase(movieInfosBucket)) {
                    continue;
                }

                final var key = s3Entity.getObject().getUrlDecodedKey();
                final var s3Object = amazonS3.getObject(bucketName, key);

                String line;

                try (final var inputStream = s3Object.getObjectContent()) {
                    try (final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                        while ((line = bufferedReader.readLine()) != null) {
                            lines.add(line);
                            if (lines.size() == 25) {
                                result += moviesDynamoDbRepository.saveLines(lines);
                                lines.clear();
                            }
                        }
                    }
                }
            }

            if (!lines.isEmpty()) {
                result += moviesDynamoDbRepository.saveLines(lines);
                lines.clear();
            }
        } catch (IOException | AmazonS3Exception | AmazonDynamoDBException error) {
            LOGGER.error(error.getMessage(), error);
        }

        LOGGER.info("{} movie infos uploaded successfully", result);

        return result;
    }
}
