FROM        openjdk:8

RUN         apt-get update
RUN         curl -sL https://deb.nodesource.com/setup_8.x | bash
RUN         apt-get install -y jekyll nodejs

# Install sbt
RUN         curl -Ls https://git.io/sbt > /bin/sbt && chmod 0755 /bin/sbt

# Cache sbt
ADD         project/build.properties /cache/project/
RUN         cd /cache/ && sbt -v exit

# Cache plugins
ADD         project/plugins.sbt /cache/project/
RUN         cd /cache/ && sbt -v exit

# Cache dependencies
ADD         project/ /cache/project/
ADD         build.sbt /cache/
RUN         cd /cache/ && sbt -v coverage +test:update

RUN         rm -r /cache/

WORKDIR     /lokal/