FROM openjdk:11.0.8-jre
COPY target/client-1.0-jar-with-dependencies.jar /client-1.0-jar-with-dependencies.jar
CMD java -Duser.language=en -Duser.country=IE -jar /client-1.0-jar-with-dependencies.jar -h ${SERVER_HOST} 
