FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
# Копируем только файл pom.xml для кеширования зависимостей
COPY pom.xml .
# Копируем исходники
COPY src ./src
# Собираем jar-файл
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
# Копируем сгенерированный jar из первого этапа
COPY --from=build /app/target/migomed-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
