FROM adoptopenjdk/openjdk16

RUN apt-get update
RUN apt-get -y install curl zip

RUN curl -s "https://get.sdkman.io" | bash
RUN echo ". $HOME/.sdkman/bin/sdkman-init.sh; sdk install gradle" | bash

WORKDIR /usr/project