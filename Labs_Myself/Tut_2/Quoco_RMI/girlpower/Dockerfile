FROM openjdk:8-jre-alpine
ENV SERVER_HOST=localhost
COPY target/girlpower-1.0-jar-with-dependencies.jar /girlpower-1.0-jar-with-dependencies.jar
CMD /usr/bin/java -jar /girlpower-1.0-jar-with-dependencies.jar ${SERVER_HOST}