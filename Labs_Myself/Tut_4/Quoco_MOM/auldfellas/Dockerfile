FROM openjdk:8-jre-alpine
COPY target/auldfellas-1.0-jar-with-dependencies.jar /service.dodgydrivers-1.0-jar-with-dependencies.jar
CMD /usr/bin/java -jar /service.dodgydrivers-1.0-jar-with-dependencies.jar ${SERVER_HOST}