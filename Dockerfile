FROM        adoptopenjdk/openjdk11:alpine

RUN         apk add --no-cache bash git nodejs

# Install sbt
RUN         wget -O /usr/local/bin/sbt https://git.io/sbt && chmod 0755 /usr/local/bin/sbt

ENTRYPOINT  /bin/bash

WORKDIR     /root/babel/