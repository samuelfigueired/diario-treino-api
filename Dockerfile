# Build: compila com Maven e gera o jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# Execução: só o JRE e o jar
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/diario-treino-api-*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
# Memória ajustada para instâncias pequenas (512 MB)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k"
EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
