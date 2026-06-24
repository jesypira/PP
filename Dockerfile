# Estágio 1: Usar uma imagem leve com Java para compilar
FROM eclipse-temurin:17-jdk-jammy AS build
COPY . .

# Dá permissão de execução para o script do Maven local e compila
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests -Dmaven.compiler.showWarnings=false

# Estágio 2: Executar o jar
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]