FROM        adoptopenjdk/openjdk11:ubuntu

RUN         apt update
RUN         apt install -y git nodejs

# Install sbt
RUN         curl -Ls https://git.io/sbt > ~/bin/sbt && chmod 0755 ~/bin/sbt

ENTRYPOINT  /bin/bash

WORKDIR     /root/babel/