## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	./gradlew clean

## test: Run the tests
test:
	./gradlew test

## package: Build and package the source code into an uber-zip
package: clean test
	./gradlew :functions:get-movie:build && \
	./gradlew :functions:update-movie-rating:build && \
	./gradlew :functions:upload-movie-infos:build

.PHONY: help clean test package