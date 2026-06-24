# Estágio 1: Usar a imagem oficial com Java 21 para compilar
FROM eclipse-temurin:21-jdk-jammy AS build
COPY . .

# Dá permissão de execução para o script do Maven local e compila
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests -Dmaven.compiler.showWarnings=false

# Estágio 2: Executar o jar usando o Java 21 em ambiente de produção
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]