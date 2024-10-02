# syntax=docker/dockerfile:1

# Build
FROM amazoncorretto:17-alpine-jdk AS build-stage
COPY ./lib ./src /server/
WORKDIR /server

#compile & create jar
RUN javac -d ./bin $(find ./com/server/* ./com/util/* | grep .java)
RUN echo Main-Class: com.server.SimpleServer > MANIFEST.MF
RUN jar -cfm SimpleServer.jar MANIFEST.MF -C ./bin .

# Deploy
FROM amazoncorretto:17-alpine-jdk AS deploy-stage
WORKDIR /server
COPY --from=build-stage /server/ ./

ENV AWS_DEFAULT_REGION=eu-west-2
RUN chmod a+x SimpleServer.jar

ENTRYPOINT ["java", "-jar", "SimpleServer.jar", "127.0.0.0", "1026"]
EXPOSE 1026
