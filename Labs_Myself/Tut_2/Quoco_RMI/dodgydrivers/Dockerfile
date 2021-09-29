FROM openjdk:8-jre-alpine
COPY target/dodgydrivers-1.0.jar /dodgydrivers-1.0.jar
CMD ["/usr/bin/java", "-cp", "/dodgydrivers-1.0.jar", "DDQServer", "mybroker"]