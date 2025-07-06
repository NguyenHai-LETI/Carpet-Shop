# --------- Stage 1: Build ---------
FROM eclipse-temurin:21-jdk AS builder

# Thiết lập locale và Maven options
ENV LANG=C.UTF-8
ENV MAVEN_OPTS="-Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8"

WORKDIR /app

# Copy Maven Wrapper và pom.xml để cache dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Tải dependencies offline
RUN ./mvnw dependency:go-offline -B

# Copy source code và build
COPY src src
RUN ./mvnw clean package -DskipTests -Dmaven.resources.skip=true -B

# --------- Stage 2: Runtime ---------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy file JAR từ stage 'builder'
COPY --from=builder /app/target/*.jar app.jar

# Mở port 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
