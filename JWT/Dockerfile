FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8081
ADD target/BMP.jar BMP.jar
ENTRYPOINT ["java","-jar","BMP.jar","--spring.profiles.active=docker"]