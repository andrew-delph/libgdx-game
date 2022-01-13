# FROM adoptopenjdk/openjdk11:latest

FROM saschpe/android-sdk:31-jdk11.0.13_8
WORKDIR /app

COPY . /app

RUN apt update
RUN apt install dos2unix
RUN dos2unix ./gradlew

# RUN apt install android-sdk -y

# RUN ./gradlew tasks

CMD ./gradlew desktop:server
