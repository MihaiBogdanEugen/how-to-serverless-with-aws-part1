## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	./gradlew clean

## package: Build and package the source code into an uber-zip
package: clean
	./gradlew :get-movie:build && \
	./gradlew :update-movie-rating:build && \
	./gradlew :upload-movie-infos:build

.PHONY: help clean package