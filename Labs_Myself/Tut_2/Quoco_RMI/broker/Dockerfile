FROM openjdk:8-jre-alpine
COPY target/broker-1.0.jar /broker-1.0.jar
CMD ["/usr/bin/java", "-cp", "/broker-1.0.jar", "BrokerServer"]