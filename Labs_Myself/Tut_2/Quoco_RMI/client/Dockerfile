FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/client-1.0-jar-with-dependencies.jar /client-1.0-jar-with-dependencies.jar
CMD /usr/bin/java -jar /client-1.0-jar-with-dependencies.jar ${SERVER_HOST}