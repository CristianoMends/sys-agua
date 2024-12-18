# Etapa 1: Build
FROM gradle:8.11.1-jdk-21-and-23-jammy AS BUILD
WORKDIR /usr/app/

# Copia apenas os arquivos necessários
COPY build.gradle settings.gradle /gradle ./
COPY src ./src

# Realiza o build da aplicação
RUN gradle clean build -x test

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre AS RUNTIME

# Variáveis para configuração do aplicativo
ENV APP_HOME=/usr/app/
ENV JAR_NAME=app.jar

WORKDIR $APP_HOME

# Copia o arquivo JAR gerado na etapa de build
COPY --from=BUILD $APP_HOME/build/libs/*.jar $JAR_NAME

# Expondo a porta da aplicação
EXPOSE 8080

# Comando de entrada configurável
ENTRYPOINT ["java", "-jar", "app.jar"]
