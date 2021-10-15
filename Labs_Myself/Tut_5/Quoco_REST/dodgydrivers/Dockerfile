FROM openjdk:11.0.8-jre
ENV SERVER_HOST localhost
COPY target/dodgydrivers-1.0-jar-with-dependencies.jar /server.jar
CMD sleep 7; java -cp /server.jar service.Receiver ${SERVER_HOST}
