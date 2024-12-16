FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY jarsDir/jointpurchase-0.0.1.jar /app/jointpurchase-0.0.1.jar
EXPOSE 8081
CMD ["java", "-jar", "jointpurchase-0.0.1.jar"]