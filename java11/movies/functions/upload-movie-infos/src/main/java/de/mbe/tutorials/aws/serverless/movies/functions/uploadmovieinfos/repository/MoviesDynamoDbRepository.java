package de.mbe.tutorials.aws.serverless.movies.functions.uploadmovieinfos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import de.mbe.tutorials.aws.serverless.movies.models.MovieInfo;
import de.mbe.tutorials.aws.serverless.movies.models.convertors.LocalDateConverter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public final class MoviesDynamoDbRepository {

    private final DynamoDBMapper dynamoDBMapper;
    private final String movieInfosTable;

    public MoviesDynamoDbRepository(final AmazonDynamoDB amazonDynamoDB, final String movieInfosTable) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        this.movieInfosTable = movieInfosTable;
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

        final var writeToMovieInfosConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER);

        if (!isNullOrEmpty(this.movieInfosTable)) {
            writeToMovieInfosConfig.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(this.movieInfosTable));
        }

        this.dynamoDBMapper.batchWrite(movieInfos, Collections.emptyList(), writeToMovieInfosConfig.build());

        return movieInfos.size();
    }
}
