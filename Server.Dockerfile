FROM adoptopenjdk/openjdk11:latest

EXPOSE 99

WORKDIR /app

COPY . /app

RUN apt update
RUN apt install dos2unix
RUN dos2unix ./gradlew

RUN ./gradlew -Pexclude.android=true :desktop:build

CMD ./gradlew -Pexclude.android=true :desktop:server
