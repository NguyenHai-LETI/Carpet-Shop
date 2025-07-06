# Sử dụng JDK 21
FROM eclipse-temurin:21-jdk

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy Maven Wrapper và pom.xml để cache dependencies
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw

# Tải dependencies offline
RUN ./mvnw dependency:go-offline -B

# Copy toàn bộ source và build
COPY src src
RUN ./mvnw clean package -DskipTests -B

# Runtime: copy jar đã build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Mở port (mặc định 8080)
EXPOSE 8080

# Command để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]
