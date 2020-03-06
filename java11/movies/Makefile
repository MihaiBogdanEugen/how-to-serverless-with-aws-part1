## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	./gradlew clean && rm -rdf packages/

## package: Build and package the source code into an uber-zip
package: clean
	mkdir -p packages/
	$(call package_java11_fn,get-movie)
	$(call package_java11_fn,update-movie-rating)
	$(call package_java11_fn,upload-movie-infos)

define package_java11_fn
	./gradlew :$(1):build && \
	cp $(1)/build/distributions/$(1)-java11.zip packages/
endef	

.PHONY: help clean package