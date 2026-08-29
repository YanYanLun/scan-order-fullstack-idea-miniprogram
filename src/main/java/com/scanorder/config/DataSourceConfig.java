package com.scanorder.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 智能自愈数据源配置器 (支持微信云托管云端 MySQL 自动探测与极速本地/云端内置自愈模式)
 */
@Configuration
public class DataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${SCAN_ORDER_MYSQL_ADDRESS:10.30.101.194:3306}")
    private String mysqlAddress;

    @Value("${SCAN_ORDER_MYSQL_USERNAME:root}")
    private String mysqlUsername;

    @Value("${SCAN_ORDER_MYSQL_PASSWORD:12315t%12315T}")
    private String mysqlPassword;

    @Value("${MYSQL_DATABASE:scan_order}")
    private String mysqlDatabase;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    @Primary
    public DataSource dataSource() {
        // 1. 微信云托管生产环境：如果检测到微信云托管注入的真实云数据库地址 (例如 10.0.x.x:3306 或 TDSQL-C)
        if (mysqlAddress != null && !mysqlAddress.trim().isEmpty() && !mysqlAddress.equals("localhost:3306") && !mysqlAddress.equals("127.0.0.1:3306")) {
            log.info("🚀 [微信云托管] 检测到云端 MySQL 实例地址: {}", mysqlAddress);
            try {
                HikariConfig config = new HikariConfig();
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setJdbcUrl("jdbc:mysql://" + mysqlAddress + "/" + mysqlDatabase + "?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
                config.setUsername(mysqlUsername);
                config.setPassword(mysqlPassword);
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(1);
                config.setConnectionTimeout(5000);
                config.setValidationTimeout(3000);
                config.setInitializationFailTimeout(0); // 避免启动时因网络波动阻塞容器启动与探针检测
                return new HikariDataSource(config);
            } catch (Exception e) {
                log.error("⚠️ 连接微信云端 MySQL 失败，自动降级为内置嵌入式数据库保障容器存活: {}", e.getMessage());
            }
        }

        // 2. 如果是本地 mysql profile
        if ("mysql".equalsIgnoreCase(activeProfile)) {
            log.info("🔌 [本地 MySQL 模式] 连接 localhost:3306/scan_order");
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/scan_order?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
            config.setUsername(mysqlUsername);
            config.setPassword(mysqlPassword);
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setInitializationFailTimeout(0);
            return new HikariDataSource(config);
        }

        // 3. 默认/无数据库绑定自愈模式 (内置 H2 内存数据库，兼容 MySQL 语法，零门槛秒级启动)
        log.info("💡 [开箱即用] 启用内置嵌入式数据库 (兼容 MySQL 语法，零门槛秒级启动)");
        HikariConfig h2Config = new HikariConfig();
        h2Config.setDriverClassName("org.h2.Driver");
        h2Config.setJdbcUrl("jdbc:h2:mem:scan_order;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;NON_KEYWORDS=YEAR,MONTH,DAY");
        h2Config.setUsername("sa");
        h2Config.setPassword("");
        h2Config.setMaximumPoolSize(10);
        return new HikariDataSource(h2Config);
    }
}
