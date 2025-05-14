# === Шаг 1: Сборка приложения с помощью Maven ===
FROM maven:3.8-openjdk-21-slim AS build
WORKDIR /app
# Копируем только файл pom.xml для кеширования зависимостей
COPY pom.xml .
# Копируем исходники
COPY src ./src
# Собираем jar-файл (ключ -DskipTests, если хотите пропустить тесты)
RUN mvn clean package -DskipTests

# === Шаг 2: Запуск приложения ===
FROM openjdk:21-slim
WORKDIR /app
# Копируем сгенерированный jar из первого этапа (проверьте имя jar-файла, оно может быть другим)
COPY --from=build /app/target/migomed.jar app.jar

# Открываем указанный порт (при необходимости)
ENV PORT=8080
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
