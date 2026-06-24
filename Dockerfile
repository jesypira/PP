# Estágio 1: Compilar o projeto usando Maven e Java 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Executar o jar usando a imagem oficial e atualizada do Eclipse Temurin (Java 17)
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]