@echo off
chcp 65001 >nul
title 寻味云居 - 扫码点餐 Spring Boot 微信云框架启动工具 (Windows Server)

echo ========================================================================
echo  寻味云居 - 扫码点餐微信云框架 Spring Boot 后端启动工具
echo  适用系统: Windows Server 2012 R2 / 2016 / 2019 / 2022 / Windows 10/11
echo ========================================================================
echo.

set JAVA_MEM_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m
set APP_JAR=scan-order-backend-springboot.jar
set LOG_FILE=logs\app.log

if not exist logs mkdir logs

echo [1/3] 检查 JDK 运行环境 (需 Java 17+)...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Java 运行环境，请先安装 JDK 17 并配置 PATH 环境变量！
    pause
    exit /b
)

echo [2/3] 检查 JAR 包文件 (%APP_JAR%)...
if not exist "%APP_JAR%" (
    echo [提示] 正在自动搜索当前目录下的 .jar 文件...
    for %%f in (*.jar) do (
        set APP_JAR=%%f
    )
)

echo [3/3] 正在启动 Spring Boot 服务 (%APP_JAR%)...
echo 日志将实时输出至 %LOG_FILE% ...
echo ------------------------------------------------------------------------

start "ScanOrderSpringBoot" java %JAVA_MEM_OPTS% -Dfile.encoding=UTF-8 -jar %APP_JAR% --spring.profiles.active=prod

echo.
echo [成功] 服务启动命令已发送！
echo 接口地址: http://localhost:8080/api
echo 健康检查: http://localhost:8080/api/health
echo ------------------------------------------------------------------------
pause
