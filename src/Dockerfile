# Estágio 1: Compilar o projeto usando Maven e Java 17
FROM maven:3.8.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Executar o jar gerado em um ambiente leve com Java 17
FROM openjdk:21-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]