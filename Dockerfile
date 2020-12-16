FROM        adoptopenjdk/openjdk11:alpine

RUN         apk add --no-cache bash git nodejs

# Install sbt
RUN         wget -O /usr/local/bin/sbt https://git.io/sbt && chmod 0755 /usr/local/bin/sbt

# Cache sbt
RUN         mkdir -p \
              ./cache/project/ \
              ./cache/src/main/scala/ \
              ./cache/src/test/scala/
ADD         ./project/build.properties ./cache/project/
ADD         ./.jvmopts ./cache/
RUN         cd ./cache/ && sbt -v exit

# Cache scala
ADD         ./scalaVersion.sbt ./cache/
RUN         echo "class App" > ./cache/src/main/scala/App.scala
RUN         cd ./cache/ && sbt -v compile

# Cache plugins
ADD         ./project/plugins.sbt ./cache/project/
RUN         cd ./cache/ && sbt -v compile

# Cache dependencies
ADD         ./project ./cache/project/
ADD         ./build.sbt ./cache/
RUN         mkdir -p ./cache/core/src/test/scala/ ./cache/website/docs/
RUN         echo "class Test" > ./cache/core/src/test/scala/Test.scala
RUN         cd ./cache/ && sbt -v ";set every sourceGenerators := List.empty;test"

# Clean cache
RUN         rm -r ./cache/

ENTRYPOINT  /bin/bash
WORKDIR     /root/lokal/