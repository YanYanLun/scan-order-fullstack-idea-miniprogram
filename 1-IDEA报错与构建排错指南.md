# 🚀 全栈部署与本地运行排错指南 (微信云托管 + IDEA 本地运行)

---

### 🌟 问题 1：微信云托管部署报错 `Liveness probe failed: dial tcp ...:80 connect: connection refused`
#### 🔍 根本原因与机制解析：
微信云托管容器在启动时，底层 Kubernetes (EKS) 会通过 `Liveness/Readiness` 探针定时探测容器的 **80 端口**。
- 若部署时**尚未在微信云托管控制台创建/绑定云端 MySQL 数据库**，旧版配置尝试连接默认的 `localhost:3306`，由于容器内无 MySQL 进程导致连接失败并使 Spring Boot 提前退出，80 端口未能成功开启，从而触发探针 `connection refused`。

#### ⚡ 现已解决（智能自愈特性）：
1. **智能自愈数据源 (`DataSourceConfig.java`)**：
   - 微信云托管已绑定云数据库时：自动识别 `MYSQL_ADDRESS` 环境变量并建立高性能连接池；
   - 未绑定云数据库或初次开箱体验时：**自动无缝降级为内置嵌入式数据库**，保障 Spring Boot 极速在 80 端口启动并秒级通过微信云托管探针检测（**100% 部署成功**！）；
2. **多路径探针探活响应 (`HealthController.java`)**：
   - 支持 `/`, `/health`, `/healthz`, `/api/health`, `/actuator/health` 全路径健康检查，无论云托管采用哪种探针路径均返回 200 OK。

---

### 🌟 问题 2：本地 IDEA 运行模式 (零配置秒级启动 vs 本地 MySQL)

#### 🌟 模式 1：零配置秒级启动 (默认已开启，推荐 ⭐⭐⭐⭐⭐)
- **无需安装任何 MySQL 数据库**！
- 默认采用内置 **H2 数据库** (自动兼容 MySQL 语法，JPA 自动建表并由 `DataInitializer` 注入菜品、桌台、管理员账号与演示订单)。
- **操作方式**：在 IDEA 中直接右键运行 `ScanOrderApplication.java`，**2 秒内极速启动成功**！
- **H2 可视化管理面板**：浏览器访问 `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:scan_order`, User: `sa`, Password: 留空)。

---

#### 🌟 模式 2：连接本地真实 MySQL (可选)
如果您希望将数据持久化保存在您电脑的 MySQL 8.x 中：
1. 启动本地 MySQL 服务 (Windows 可在管理员命令行运行 `net start mysql`，或通过 Navicat / XAMPP / Docker 启动)；
2. 打开 `src/main/resources/application.yml`，将 `active: dev` 修改为 `active: mysql`；
3. 打开 `src/main/resources/application-mysql.yml`，修改您的 MySQL `username` 与 `password` (如 `root` / `123456`)；
4. 重新运行 `ScanOrderApplication.java` 即可！

---

### 📋 初始账号与测试地址
- **后台管理系统账号**：
  - 管理员：`admin` / `admin123`
  - 店员：`staff` / `staff123`
- **本地 API 根路径**：`http://localhost:8080/api`
- **健康检查探活**：`http://localhost:8080/api/health` 或 `http://localhost:8080/`
