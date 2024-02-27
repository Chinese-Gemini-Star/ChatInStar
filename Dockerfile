FROM openjdk:17
LABEL authors="GeminiStar"

ADD build/libs/chatinstar-0.0.1-SNAPSHOT.jar /app/chatinstar-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/chatinstar-0.0.1-SNAPSHOT.jar"]