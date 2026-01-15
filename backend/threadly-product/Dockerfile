#you have to give every single information in the Dockerfile
#for example:
#java ka kaunsa version download karna hai
#postgresql ka kaunsa version download karna hai
#steps?
#jar file kaise banaoge?
#mvn clean package
#.jar local system k andar banegi, you will have to copy the jar file into docker container
#
#run java -jar jobhi jar file ka naam hoga
#
#docker run image_name

# Stage Build
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine AS run

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]



