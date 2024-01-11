# Use uma imagem Java como base
FROM openjdk:17-alpine

# Defina o diretório de trabalho como /app
WORKDIR /app

# Copie o arquivo jar para o diretório de trabalho
COPY target/SAMIR-1.0.0-SNAPSHOT.jar /app/SAMIR-1.0.0-SNAPSHOT.jar

# Execute o aplicativo
CMD ["java", "-jar", "SAMIR-1.0.0-SNAPSHOT.jar"]
