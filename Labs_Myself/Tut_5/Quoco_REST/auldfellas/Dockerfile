FROM openjdk:11.0.8-jre
ENV SERVER_HOST localhost
COPY target/auldfellas-1.0.jar /server.jar
CMD sleep 7; java -jar /server.jar service.Application ${SERVER_HOST}