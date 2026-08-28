#!/bin/bash
# 微信云框架 Spring Boot 后端 Linux / 容器一键启动脚本

APP_NAME="scan-order-backend-springboot"
JAR_NAME=$(ls *.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_NAME" ]; then
  JAR_NAME="scan-order-backend-springboot.jar"
fi

JAVA_OPTS="-Xms512m -Xmx1024m -Dfile.encoding=UTF-8"

mkdir -p logs

echo "========================================================================"
echo "正在启动 $APP_NAME ($JAR_NAME)..."
echo "========================================================================"

nohup java $JAVA_OPTS -jar $JAR_NAME --spring.profiles.active=prod > logs/app.log 2>&1 &

echo "服务已在后台启动！查看日志: tail -f logs/app.log"
echo "健康检查端点: http://localhost:8080/api/health"
