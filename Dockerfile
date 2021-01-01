FROM        adoptopenjdk/openjdk11:ubuntu

RUN         apt update
RUN         apt install -y git nodejs
RUN         apk add --no-cache bash git gnupg nodejs

# Install sbt
RUN         wget -O /usr/local/bin/sbt https://git.io/sbt && chmod 0755 /usr/local/bin/sbt

ENTRYPOINT  /bin/bash

WORKDIR     /root/babel/