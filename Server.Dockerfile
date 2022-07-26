FROM adoptopenjdk/openjdk11:latest

EXPOSE 99

WORKDIR /app


RUN apt update

# install grpcurl
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:longsleep/golang-backports
RUN apt update
RUN apt install golang-go -y
ENV PATH=${PATH}:/root/go/bin
RUN go install github.com/fullstorydev/grpcurl/cmd/grpcurl@latest

RUN apt install dos2unix

HEALTHCHECK --interval=30s --timeout=3s \
  CMD grpcurl -plaintext localhost:99 NetworkObjectService/health || exit 1


COPY . /app

RUN dos2unix ./gradlew

RUN ./gradlew -Pexclude.android=true :desktop:build

CMD ./gradlew -Pexclude.android=true :desktop:server
