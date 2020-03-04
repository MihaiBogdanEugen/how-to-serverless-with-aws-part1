## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	rm -rdf packages/ && rm -rdf temp/

## package: Build and package the source code into an uber-zip
package: clean
	mkdir -p packages/
	$(call package_python_fn,get-movie)
	$(call package_python_fn,update-movie-rating)
	$(call package_python_fn,upload-movie-infos)

define package_python_fn
	pip3 install -r requirements.txt -t ./temp/$(1) && \
	cp -r $(1) temp/$(1)/ && \
	cd ./temp/$(1)/ && \
	zip -r9 ../../packages/$(1).zip . && \
	cd ../../ &&\
	rm -rdf temp/
endef

.PHONY: clean package