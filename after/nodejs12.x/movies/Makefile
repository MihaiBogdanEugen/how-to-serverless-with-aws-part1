
## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	rm -rdf node_modules/ && rm -rdf packages/ && rm -rdf temp/

## package: Build and package the source code into an uber-zip
package: clean
	mkdir -p packages/
	$(call package_nodejs_fn,get-movie)
	$(call package_nodejs_fn,update-movie-rating)
	$(call package_nodejs_fn,upload-movie-infos)

define package_nodejs_fn
	mkdir temp && \
	cp package.json temp/ && \
	cp $(1)/*.js temp/ && \
	cd temp && \
	npm install && \
	zip -r9 ../packages/$(1)-nodejs12.x.zip . && \
	cd ../ &&\
	rm -rdf temp/
endef

.PHONY: clean package