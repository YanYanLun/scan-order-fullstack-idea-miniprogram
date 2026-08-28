# 扫码点餐系统 - 微信云托管 (WeChat CloudRun) + Spring Boot 3 + MySQL 后端工程

> 基于 **IntelliJ IDEA 2025** 与 **微信云原生框架 (WeChat Cloud Framework)** 打造的扫码点餐全功能服务端，支持一键发布到微信云托管，亦可直接部署于 Windows Server 2012+、CentOS/Ubuntu 服务器与本地环境。

---

## 🌟 核心特性与功能迁移一览

- ☁️ **微信云托管原生适配**：内置 `Dockerfile` 与 `container.config.json`，开箱即用支持微信云托管容器构建与免鉴权调用。
- 🔑 **微信用户免密识别**：通过 `WxCloudInterceptor` 自动拦截微信网关请求头（`x-wx-openid`、`x-wx-unionid`），无需维护复杂 Token 即可精准获取食客 OpenID 并进行会员积分沉淀。
- 📱 **小程序全双工通信**：同时支持微信小程序的 `wx.cloud.callContainer`（云托管容器免公网域名直连）与标准 HTTPS / HTTP RESTful API。
- 🗄️ **完整数据结构迁移**：
  - `wx_users`：微信用户表（OpenID、昵称头像、会员等级与消费积分）
  - `category`：菜品分类表（排序与启用开关）
  - `goods`：菜品商品表（菜品CRUD、上下架、售罄估清、标签）
  - `dining_table`：桌台管理表（桌号、容纳人数、二维码与实时就餐占用状态）
  - `orders` & `order_item`：订单主表与明细（堂食/外带、微信支付、接单/制作/出餐/结单/取消完整状态机）
  - `store_info`：门店配置（营业开关、公告、地址电话）
  - `merchant_user`：商家管理员账号密码校验（内置 `admin / admin123`）
  - `daily_revenue_stat`：每日营收报表与日历对账
- 🚀 **IntelliJ IDEA 2025 完美兼容**：预置 `.idea` 运行与编译配置，直接导入即可一键 Run。
- 🖥️ **Windows Server 2012+ 一键运维**：附赠 `start_springboot.bat` 批处理启动脚本。

---

## 🛠️ 环境要求

- **JDK**: OpenJDK 17 或 Oracle JDK 17+
- **数据库**: MySQL 8.0+ / 微信云托管内网 MySQL
- **IDE**: IntelliJ IDEA 2025.x / 2024.x
- **构建工具**: Maven 3.8+ (IDEA 内置)

---

## 🚀 方式一：IntelliJ IDEA 本地开发启动

1. **导入工程**：
   - 打开 IntelliJ IDEA，选择 **File -> Open...**，选中本工程目录 `scan-order-backend-springboot`。
2. **配置本地 MySQL 数据库**：
   - 打开 `src/main/resources/application-dev.yml`。
   - 修改 `spring.datasource.username` 与 `spring.datasource.password` 为你本地 MySQL 的账号密码。
   - 工程配置了 `spring.sql.init.mode: always`，启动时将**自动执行 `db/schema.sql` 与 `db/data.sql`**，自动建表并注入演示数据！
3. **一键运行**：
   - 找到 `src/main/java/com/scanorder/ScanOrderApplication.java`，点击绿色运行按钮。
   - 访问健康检查：`http://localhost:8080/api/health`。

---

## ☁️ 方式二：微信云托管 (WeChat CloudRun) 一键部署

1. 打开 [微信云托管控制台](https://cloud.weixin.qq.com/)。
2. 创建服务（例如服务名 `scan-order-service`）。
3. 选择 **本地代码包上传** 或绑定 GitHub / Gitee 仓库（选择本工程根目录）。
4. 云托管会自动识别工程根目录下的 `Dockerfile` 与 `container.config.json` 进行容器构建。
5. 在云托管【数据库】中创建 MySQL 实例，或在服务【环境变量】中配置：
   - `PORT` = `80`
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `MYSQL_ADDRESS` = 你的云托管MySQL内网地址
   - `MYSQL_USERNAME` = 你的数据库用户名
   - `MYSQL_PASSWORD` = 你的数据库密码
6. 发布成功后，微信小程序端可直接使用 `wx.cloud.callContainer` 免域名调用！

---

## 🖥️ 方式三：Windows Server 2012 服务器部署

1. 在本地或 IDEA 中执行 Maven 打包：
   ```bash
   mvn clean package -DskipTests
   ```
2. 将 `target/scan-order-backend-springboot-1.0.0.jar` 和 `start_springboot.bat` 复制到 Windows Server 服务器同一目录下。
3. 双击 `start_springboot.bat` 即可一键启动后台服务并自动输出日志。
