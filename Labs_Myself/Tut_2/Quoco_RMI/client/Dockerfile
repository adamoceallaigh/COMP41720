FROM openjdk:8-jre-alpine
COPY target/client-1.0.jar /client-1.0.jar
CMD ["/usr/bin/java", "-cp", "/client-1.0.jar", "Main", "mybroker"]