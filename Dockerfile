# Sử dụng JDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven Wrapper và pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Cho phép chạy mvnw
RUN chmod +x mvnw

# Thiết lập Maven dùng UTF-8
ENV MAVEN_OPTS="-Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8"

# Tải offline dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source
COPY src src

# Build package, skip tests
RUN ./mvnw clean package -DskipTests -B

# Copy jar để chạy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
