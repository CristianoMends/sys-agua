# Etapa 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS BUILD
WORKDIR /usr/app/

# Copia o arquivo pom.xml e o código fonte para a imagem
COPY pom.xml ./
COPY src ./src

# Realiza o build da aplicação (compila o código e gera o arquivo JAR)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre AS RUNTIME

# Variáveis para configuração do aplicativo
ENV APP_HOME=/usr/app/
ENV JAR_NAME=app.jar

WORKDIR $APP_HOME

# Copia o arquivo JAR gerado na etapa de build para o container
COPY --from=BUILD $APP_HOME/target/*.jar $JAR_NAME

# Comando de entrada configurável para verificar se o build funcionou
ENTRYPOINT ["java", "-version"]
