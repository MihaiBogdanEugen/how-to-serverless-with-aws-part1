CODE_VERSION?=java

## help: Prints this help message
help:
	@echo "Usage: \n"
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## clean: Clean the files and directories generated during build
clean:
	rm -rdf packages/
ifeq ($(CODE_VERSION), java)
	@(echo "Using Java11")
	cd java11/movies/ && \
	./gradlew clean && \
	rm -rdf .settings/ && \
	rm -df .project && \
	rm -rdf */bin/ && \
	rm -rdf */.settings/ && \
	rm -df */.classpath && \
	rm -df */.project
else ifeq ($(CODE_VERSION), python)
	@(echo "Using Python3.8")
else ifeq ($(CODE_VERSION), nodejs)
	@(echo "Using NodeJs12.x")
else
	@(echo "ERROR: Unknown code version")
endif

## package: Build and package the source code into an uber-zip
package: clean
	mkdir -p packages/
ifeq ($(CODE_VERSION), java)
	$(call package_java_fn,get-movie)
	$(call package_java_fn,update-movie-rating)
	$(call package_java_fn,upload-movie-infos)
else ifeq ($(CODE_VERSION), python)
	$(call package_python_fn,get-movie)
	$(call package_python_fn,update-movie-rating)
	$(call package_python_fn,upload-movie-infos)
else ifeq ($(CODE_VERSION), nodejs)
	$(call package_nodejs_fn,get-movie)
	$(call package_nodejs_fn,update-movie-rating)
	$(call package_nodejs_fn,upload-movie-infos)
else
	@(echo "ERROR: Unknown code version")
endif

define package_java_fn
	cd java11/movies/ && \
	./gradlew :$(1):build && \
	cp $(1)/build/distributions/$(1).zip ../../packages/$(1)-java11.zip
endef

define package_python_fn
	cd python3.8/movies/ && \
	pip3 install -r requirements.txt -t ./temp/ && \
	cp -r $(1) temp/ && \
	cd ./temp/ && \
	zip -r9 ./../../../packages/$(1)-python3.8.zip . && \
	cd ../ && \
	rm -rdf temp/
endef

define package_nodejs_fn
	cd nodejs12.x/movies && \
	npm --prefix ./temp install . && \
	cp -r $(1) temp/ && \
	cd ./temp/ && \
	zip -r9 ./../../../packages/$(1)-nodejs12.x.zip . && \
	cd ../ && \
	rm -rdf temp/
endef

.PHONY: help clean package