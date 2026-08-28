# ========================================================
# 微信云托管 (WeChat CloudRun) Spring Boot 多阶段构建镜像
# ========================================================

# 阶段 1: 使用 Maven 进行源码编译打包
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# 复制 pom.xml 及源码
COPY pom.xml .
COPY src ./src

# 构建可执行 JAR (跳过单测加速构建)
RUN mvn clean package -DskipTests

# 阶段 2: 运行环境
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 复制产物
COPY --from=builder /app/target/scan-order-backend-springboot-*.jar app.jar

# 微信云托管默认容器端口为 80 (或读取系统环境变量 PORT)
EXPOSE 80
ENV PORT=80
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms128m -Xmx512m -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# 启动容器并传递端口参数
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT}"]
