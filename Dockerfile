# 1) Chọn base image với JDK 21
FROM eclipse-temurin:21-jdk

# 2) Thiết lập locale sang UTF-8 để Maven đọc resources đúng encoding
ENV LANG=C.UTF-8
ENV MAVEN_OPTS="-Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8"

# 3) Thư mục làm việc trong container
WORKDIR /app

# 4) Copy Maven Wrapper và pom.xml để cache dependencies trước
COPY mvnw pom.xml ./
COPY .mvn .mvn

# 5) Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw

# 6) Tải offline dependencies (tối ưu build)
RUN ./mvnw dependency:go-offline -B

# 7) Copy toàn bộ source code
COPY src src

# 8) Build package, skip tests
RUN ./mvnw clean package -DskipTests -B

# 9) Copy file JAR đã build ra để chạy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 10) Mở port 8080 (theo mặc định Spring Boot)
EXPOSE 8080

# 11) Command để khởi chạy Spring Boot app
ENTRYPOINT ["java", "-jar", "/app.jar"]
