# Bước 1: Build ứng dụng (Sử dụng Maven và JDK 17)
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng (Sử dụng JDK 17 bản rút gọn cho nhẹ)
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]