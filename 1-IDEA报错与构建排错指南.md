# 🚀 后端运行排错指南 (Connection refused 数据库连接与构建说明)

---

### 🌟 现象分析：为什么之前会出现 `Connection refused` 错误？
控制台报错日志：
```text
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
Caused by: java.net.ConnectException: Connection refused: no further information
```
**根本原因**：之前配置默认直连本地 `localhost:3306` 的 MySQL 数据库，而您的电脑上**尚未安装 MySQL 或 MySQL 服务未启动**。

---

### ⚡ 全新特性：双模式数据库 (零安装即开即用 vs 本地真实 MySQL)

本项目已全面升级为**开箱即用架构**，提供 3 种开箱运行模式：

#### 🌟 模式 1：零配置秒级启动 (默认已开启，推荐 ⭐⭐⭐⭐⭐)
- **无需安装任何 MySQL 数据库**！
- 默认采用内置 **H2 内存数据库** (自动兼容 MySQL 语法，JPA 自动建表并由 `DataInitializer` 注入菜品、桌台、管理员账号与演示订单)。
- **操作方式**：在 IDEA 中直接右键运行 `ScanOrderApplication.java`，**2 秒内极速启动成功**！
- **H2 可视化管理面板**：浏览器访问 `http://localhost:8080/api/h2-console` (JDBC URL: `jdbc:h2:mem:scan_order`, User: `sa`, Password: 留空)。

---

#### 🌟 模式 2：连接本地真实 MySQL (可选)
如果您希望将数据持久化保存在您电脑的 MySQL 8.x 中：
1. 启动本地 MySQL 服务 (Windows 可在管理员命令行运行 `net start mysql`，或通过 Navicat / XAMPP / Docker 启动)；
2. 打开 `src/main/resources/application.yml`，将 `active: dev` 修改为 `active: mysql`；
3. 打开 `src/main/resources/application-mysql.yml`，修改您的 MySQL `username` 与 `password` (如 `root` / `123456`)；
4. 重新运行 `ScanOrderApplication.java` 即可！

---

#### 🌟 模式 3：微信云托管 (WeChat CloudRun) 云端生产环境
- 部署至微信云托管时，系统会自动激活 `active: prod`；
- 微信云托管会自动注入云端 MySQL 环境变量 (`MYSQL_ADDRESS`, `MYSQL_USERNAME`, `MYSQL_PASSWORD`)，无需手动修改代码！

---

### 📋 初始账号与测试地址
- **后台管理系统账号**：
  - 管理员：`admin` / `admin123`
  - 店员：`staff` / `staff123`
- **本地 API 根路径**：`http://localhost:8080/api`
- **健康检查探活**：`http://localhost:8080/api/health`
