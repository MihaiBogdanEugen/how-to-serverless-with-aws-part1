package de.mbe.tutorials.aws.serverless.movies.functions.uploadmovieinfos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.functions.uploadmovieinfos.repository.models.MovieInfo;
import de.mbe.tutorials.aws.serverless.movies.functions.uploadmovieinfos.repository.models.convertors.LocalDateConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDBMapperConfig writeMovieInfoConfig;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieInfosTable) {

        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        final var writeMovieInfoConfigBuilder = DynamoDBMapperConfig.builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.PUT);
        if (!isNullOrEmpty(movieInfosTable)) {
            writeMovieInfoConfigBuilder.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(movieInfosTable));
        }

        this.writeMovieInfoConfig = writeMovieInfoConfigBuilder.build();
    }

    public int saveLines(final List<String> lines) {

        if (lines.isEmpty()) {
            return 0;
        }

        final var movieInfos = lines.stream()
                .map(line -> line.split(","))
                .map(parts -> new MovieInfo(parts[0], parts[1], parts[2], LocalDateConverter.parseString(parts[3])))
                .collect(Collectors.toList());

        return saveMovies(movieInfos);
    }

    public int saveMovies(final List<MovieInfo> movieInfos) {

        if (movieInfos.isEmpty()) {
            return 0;
        }

        this.dynamoDBMapper.batchWrite(movieInfos, Collections.emptyList(), this.writeMovieInfoConfig);

        return movieInfos.size();
    }
}
