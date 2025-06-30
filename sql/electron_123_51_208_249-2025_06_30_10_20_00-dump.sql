-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 123.51.208.249    Database: electron
-- ------------------------------------------------------
-- Server version	8.0.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `admin_web_user`
--

DROP TABLE IF EXISTS `admin_web_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_web_user`
(
    `admin_id`     bigint                                                       NOT NULL AUTO_INCREMENT,
    `account`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
    `nick_name`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci          DEFAULT NULL,
    `email`        varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci          DEFAULT NULL,
    `password`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci         DEFAULT NULL,
    `phone_number` bigint                                                                DEFAULT NULL COMMENT '手机号码',
    `login_time`   datetime                                                              DEFAULT NULL COMMENT '最后登录时间',
    `login_ip`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci          DEFAULT NULL COMMENT '登录ip',
    `money`        float                                                        NOT NULL DEFAULT '0' COMMENT '资金',
    PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11825954457
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_web_user`
--

LOCK TABLES `admin_web_user` WRITE;
/*!40000 ALTER TABLE `admin_web_user`
    DISABLE KEYS */;
INSERT INTO `admin_web_user`
VALUES (11825954456, 'admin', 'lwz', '1241415i@gmail.com',
        '$2a$12$E5akMSyHVijdiACdBBTZ4.QHv6w3og8Bz5.DCKnozHzc.U.ZEDVwC', 1231513151, '2025-06-23 10:18:21',
        '14.116.71.89', 67.99);
/*!40000 ALTER TABLE `admin_web_user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcement`
(
    `id`          int                                                          NOT NULL AUTO_INCREMENT,
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci        NOT NULL COMMENT '公告内容',
    `create_time` datetime                                                     NOT NULL COMMENT '发布时间',
    `title`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '公告标题',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `announcement_id_index` (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='公告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

LOCK TABLES `announcement` WRITE;
/*!40000 ALTER TABLE `announcement`
    DISABLE KEYS */;
INSERT INTO `announcement`
VALUES (12, '客服：✈+886917446962', '2025-05-29 17:41:21', '客服：✈+886917446962');
/*!40000 ALTER TABLE `announcement`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_version`
--

DROP TABLE IF EXISTS `app_version`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_version`
(
    `id`            int                                     NOT NULL AUTO_INCREMENT,
    `version`       varchar(10) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '版本号',
    `release_notes` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新信息',
    `download_url`  varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '下载地址',
    `upload_time`   datetime                                NOT NULL COMMENT '更新时间',
    `file_size`     bigint DEFAULT NULL COMMENT '安装包大小',
    `app`           int                                     NOT NULL COMMENT '0=客户端 1=卡商端',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 47
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_version`
--

LOCK TABLES `app_version` WRITE;
/*!40000 ALTER TABLE `app_version`
    DISABLE KEYS */;
INSERT INTO `app_version`
VALUES (31, '1.2.6', '应用内自动更新',
        'http://123.51.208.249:9000/downloadapp/kashang/2025/06/03changhong-ks-1.2.6.exe', '2025-06-03 15:50:08',
        79087268, 1),
       (32, '1.2.6', '应用内自动更新', 'http://123.51.208.249:9000/downloadapp/kehu/2025/06/03changhong-1.2.6.exe',
        '2025-06-03 15:54:27', 88482590, 0),
       (33, '1.2.7', '优化订单状态显示效果',
        'http://123.51.208.249:9000/downloadapp/kehu/2025/06/05changhong-1.2.7.exe', '2025-06-05 19:11:21', 88483039,
        0),
       (34, '1.2.7', '优化订单交易记录页面内容展示效果',
        'http://123.51.208.249:9000/downloadapp/kashang/2025/06/05changhong-ks-1.2.7.exe', '2025-06-05 19:20:58',
        79091947, 1),
       (39, '1.2.8', '验证码短信添加过期时间',
        'http://123.51.208.249:9000/downloadapp/kehu/2025/06/09changhong-1.2.8.exe', '2025-06-09 15:48:11', 88483868,
        0),
       (41, '1.2.9', '优化项目图标、地区图标展示效果',
        'http://123.51.208.249:9000/downloadapp/kehu/2025/06/11changhong-1.2.9.exe', '2025-06-11 16:42:33', 88466847,
        0),
       (42, '1.2.8', '修复条件查询问题',
        'http://123.51.208.249:9000/downloadapp/kashang/2025/06/13changhong-ks-1.2.8.exe', '2025-06-13 15:23:31',
        79087135, 1),
       (43, '1.2.9', 'bug补丁', 'http://123.51.208.249:9000/downloadapp/kashang/2025/06/13changhong-ks-1.2.9.exe',
        '2025-06-13 15:53:33', 79087649, 1),
       (44, '1.2.10', '修复号码无法条件搜索问题',
        'http://123.51.208.249:9000/downloadapp/kashang/2025/06/13changhong-ks-1.2.10.exe', '2025-06-13 16:49:36',
        79092286, 1),
       (45, '1.3.0', '优化用户信息不同步问题',
        'http://123.51.208.249:9000/downloadapp/kehu/2025/06/20changhong-1.3.0.exe', '2025-06-20 10:51:02', 88467174,
        0);
/*!40000 ALTER TABLE `app_version`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation_relations`
--

DROP TABLE IF EXISTS `invitation_relations`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invitation_relations`
(
    `id`              int                                                          NOT NULL AUTO_INCREMENT,
    `inviter_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '邀请人账号',
    `invitee_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '被邀请人账号',
    `invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '使用的邀请码Id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `invitation_relations_invitation_code_index` (`invitation_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='邀请码关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation_relations`
--

LOCK TABLES `invitation_relations` WRITE;
/*!40000 ALTER TABLE `invitation_relations`
    DISABLE KEYS */;
INSERT INTO `invitation_relations`
VALUES (1, 'qqqqqq', 'yyyyyy', '95e151202a4a4da4b586');
/*!40000 ALTER TABLE `invitation_relations`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `merchant`
--

DROP TABLE IF EXISTS `merchant`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant`
(
    `user_id`      bigint                                           NOT NULL,
    `account`      varchar(20) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT '账号',
    `nick_name`    varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT '昵称',
    `password`     varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
    `email`        varchar(20) CHARACTER SET utf8 COLLATE utf8_bin           DEFAULT NULL COMMENT '邮箱',
    `created_at`   datetime                                                  DEFAULT NULL COMMENT '创建时间',
    `updated_at`   datetime                                                  DEFAULT NULL COMMENT '更新时间',
    `user_avatar`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin          DEFAULT NULL,
    `login_time`   datetime                                                  DEFAULT NULL COMMENT '登录时间',
    `phone_number` bigint unsigned                                           DEFAULT NULL COMMENT '号码',
    `money`        float                                                     DEFAULT '0' COMMENT '卡商资金',
    `divide_into`  int                                              NOT NULL DEFAULT '10' COMMENT '分成',
    KEY `admin_users_user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8_bin
  ROW_FORMAT = DYNAMIC COMMENT ='卡商';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `merchant`
--

LOCK TABLES `merchant` WRITE;
/*!40000 ALTER TABLE `merchant`
    DISABLE KEYS */;
INSERT INTO `merchant`
VALUES (1925129632954384386, '111111', '测试卡商', '$2a$12$YOTMwVVdHM/Cu.xUl9opHuxNogjZTLaaf3tgGgBECxifesi4p5AWm', '',
        '2025-05-21 18:00:46', NULL, 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1',
        '2025-06-18 15:17:12', NULL, 104.31, 50),
       (1928019298395983873, 'qqqqqq', 'qqqqqq', '$2a$12$UETURZSoWnoHVufoUsULdu9yhg.N/Qiq4b1QORTAlTKjrSnH7ERYW', '',
        '2025-05-29 17:23:16', NULL, NULL, '2025-05-29 17:27:08', NULL, 0, 10),
       (1933795631193395202, 'donggua123', 'DG', '$2a$12$ZOHuonw3gVVW/uSSxT8.Te9TOHCEUd8oSRmDmNYR.bGhgWwkc7VA.', '',
        '2025-06-14 15:56:21', NULL, NULL, '2025-06-14 17:06:15', NULL, 0, 10);
/*!40000 ALTER TABLE `merchant`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_bill`
--

DROP TABLE IF EXISTS `order_bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_bill`
(
    `id`                int                                     NOT NULL AUTO_INCREMENT,
    `order_id`          varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单di',
    `merchant_id`       bigint                                  NOT NULL COMMENT '卡商id',
    `admin_id`          bigint                                  NOT NULL COMMENT '平台账号id',
    `divide_into`       int                                     NOT NULL COMMENT '抽成比例',
    `remaining_amount`  float                                   NOT NULL COMMENT '卡商收益',
    `commission_amount` float                                   NOT NULL COMMENT '平台收益',
    `start_time`        datetime                                NOT NULL COMMENT '结算时间',
    `order_money`       float                                   NOT NULL COMMENT '订单总价',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='订单账单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_bill`
--

LOCK TABLES `order_bill` WRITE;
/*!40000 ALTER TABLE `order_bill`
    DISABLE KEYS */;
INSERT INTO `order_bill`
VALUES (1, '519', 1925129632954384386, 11825954456, 10, 4.5, 0.5, '2025-06-05 18:57:54', 5),
       (2, '5f8806e4-dc56-438a-aa86-965f6be31f15', 1925129632954384386, 11825954456, 50, 0.5, 0.5,
        '2025-06-19 09:22:36', 1);
/*!40000 ALTER TABLE `order_bill`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders`
(
    `orders_id`     varchar(255) NOT NULL,
    `user_id`       bigint       NOT NULL,
    `created_at`    datetime     NOT NULL COMMENT '购买时间',
    `project_id`    int                                                           DEFAULT NULL,
    `phone_number`  bigint       NOT NULL COMMENT '号码',
    `project_money` float        NOT NULL COMMENT '项目价格',
    `state`         int          NOT NULL                                         DEFAULT '0' COMMENT '订单状态  0=未使用   1=已使用 2=已结算',
    `code`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '验证码',
    `merchant_id`   bigint                                                        DEFAULT NULL COMMENT '卡商id',
    `region_id`     int                                                           DEFAULT NULL COMMENT '地区id',
    PRIMARY KEY (`orders_id`) USING BTREE,
    KEY `user_order_user_project_id_index` (`orders_id`) USING BTREE,
    KEY `user_order_admin_id_index` (`merchant_id`) USING BTREE,
    KEY `user_order_user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户购买的订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders`
    DISABLE KEYS */;
INSERT INTO `orders`
VALUES ('1b621e61-edf8-432a-b2fe-91ccb0487c28', 1924352187670147074, '2025-06-18 15:20:20', 5, 47513331, 20, 2, NULL,
        1925129632954384386, 6),
       ('5f8806e4-dc56-438a-aa86-965f6be31f15', 1924352187670147074, '2025-06-16 18:25:44', 10, 44692904, 1, 3, NULL,
        1925129632954384386, 6);
/*!40000 ALTER TABLE `orders`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phone`
(
    `phone_id`          bigint NOT NULL AUTO_INCREMENT COMMENT '手机号ID',
    `phone_number`      bigint NOT NULL COMMENT '手机号码',
    `registration_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `admin_user_id`     bigint   DEFAULT NULL COMMENT '管理员用户ID',
    `money`             double   DEFAULT NULL COMMENT '号码价格',
    PRIMARY KEY (`phone_id`) USING BTREE,
    UNIQUE KEY `uk_phone_number` (`phone_number`) USING BTREE COMMENT '手机号码唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 188
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='手机号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone`
    DISABLE KEYS */;
INSERT INTO `phone`
VALUES (183, 47567094, '2025-06-18 15:19:43', 1925129632954384386, NULL),
       (184, 12112515, '2025-06-18 15:19:43', 1925129632954384386, NULL),
       (187, 37234, '2025-06-18 15:19:43', 1925129632954384386, NULL);
/*!40000 ALTER TABLE `phone`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_project_relation`
--

DROP TABLE IF EXISTS `phone_project_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phone_project_relation`
(
    `id`           bigint     NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `phone_id`     bigint     NOT NULL COMMENT '手机号ID',
    `project_id`   int        NOT NULL COMMENT '项目ID',
    `created_at`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `region_id`    int        NOT NULL COMMENT '地区ID',
    `is_available` tinyint(1) NOT NULL COMMENT '0 = 未使用  1 = 被购买',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `fk_relation_project` (`project_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 123160
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='手机号与项目关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_project_relation`
--

LOCK TABLES `phone_project_relation` WRITE;
/*!40000 ALTER TABLE `phone_project_relation`
    DISABLE KEYS */;
INSERT INTO `phone_project_relation`
VALUES (123124, 182, 5, '2025-06-18 15:19:43', 6, 0),
       (123125, 182, 6, '2025-06-18 15:19:43', 6, 0),
       (123126, 182, 7, '2025-06-18 15:19:43', 6, 0),
       (123127, 182, 8, '2025-06-18 15:19:43', 6, 0),
       (123128, 182, 9, '2025-06-18 15:19:43', 6, 0),
       (123129, 182, 10, '2025-06-18 15:19:43', 6, 0),
       (123130, 183, 5, '2025-06-18 15:19:43', 6, 0),
       (123131, 183, 6, '2025-06-18 15:19:43', 6, 0),
       (123132, 183, 7, '2025-06-18 15:19:43', 6, 0),
       (123133, 183, 8, '2025-06-18 15:19:43', 6, 0),
       (123134, 183, 9, '2025-06-18 15:19:43', 6, 0),
       (123135, 183, 10, '2025-06-18 15:19:43', 6, 0),
       (123136, 184, 5, '2025-06-18 15:19:43', 6, 1),
       (123137, 184, 6, '2025-06-18 15:19:43', 6, 1),
       (123138, 184, 7, '2025-06-18 15:19:43', 6, 1),
       (123139, 184, 8, '2025-06-18 15:19:43', 6, 1),
       (123140, 184, 9, '2025-06-18 15:19:43', 6, 1),
       (123141, 184, 10, '2025-06-18 15:19:43', 6, 1),
       (123142, 185, 5, '2025-06-18 15:19:43', 6, 1),
       (123143, 185, 6, '2025-06-18 15:19:43', 6, 0),
       (123144, 185, 7, '2025-06-18 15:19:43', 6, 0),
       (123145, 185, 8, '2025-06-18 15:19:43', 6, 0),
       (123146, 185, 9, '2025-06-18 15:19:43', 6, 0),
       (123147, 185, 10, '2025-06-18 15:19:43', 6, 0),
       (123148, 186, 5, '2025-06-18 15:19:43', 6, 0),
       (123149, 186, 6, '2025-06-18 15:19:43', 6, 0),
       (123150, 186, 7, '2025-06-18 15:19:43', 6, 0),
       (123151, 186, 8, '2025-06-18 15:19:43', 6, 0),
       (123152, 186, 9, '2025-06-18 15:19:43', 6, 0),
       (123153, 186, 10, '2025-06-18 15:19:43', 6, 0),
       (123154, 187, 5, '2025-06-18 15:19:43', 6, 0),
       (123155, 187, 6, '2025-06-18 15:19:43', 6, 0),
       (123156, 187, 7, '2025-06-18 15:19:43', 6, 0),
       (123157, 187, 8, '2025-06-18 15:19:43', 6, 0),
       (123158, 187, 9, '2025-06-18 15:19:43', 6, 0),
       (123159, 187, 10, '2025-06-18 15:19:43', 6, 0);
/*!40000 ALTER TABLE `phone_project_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project`
(
    `project_id`         int                                                           NOT NULL AUTO_INCREMENT COMMENT '项目ID，使用自增长整数',
    `project_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '项目名称',
    `project_price`      decimal(10, 2)                                                         DEFAULT '0.00' COMMENT '项目价格',
    `project_created_at` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `project_update_at`  datetime                                                               DEFAULT NULL COMMENT '跟新时间',
    `icon`               varchar(255) COLLATE utf8mb4_0900_as_ci                                DEFAULT NULL COMMENT '图标',
    `expiration_time`    int                                                           NOT NULL DEFAULT '20' COMMENT '过期时间',
    PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project`
    DISABLE KEYS */;
INSERT INTO `project`
VALUES (5, '京东', 20.00, '2025-05-22 14:48:25', '2025-05-29 16:15:19',
        'http://123.51.208.249:9000/image/2025/06/11d7ad8f8f-6c99-40c3-a32e-82e66b9f0637-京东.png', 20),
       (6, '抖音', 10.00, '2025-05-29 17:31:14', '2025-06-10 15:06:44',
        'http://123.51.208.249:9000/image/2025/06/116196ff76-b12d-4df2-84e9-200148145832-抖音.png', 20),
       (7, '阿里巴巴', 5.00, '2025-05-29 17:31:35', '2025-05-29 17:43:44',
        'http://123.51.208.249:9000/image/2025/06/11ecae1c28-83b3-4d0e-9931-5718f06d4d65-淘宝.png', 1),
       (8, 'TikTok', 11.00, '2025-06-13 11:28:07', NULL, NULL, 20),
       (9, '澳门微信', 25.00, '2025-06-14 16:00:36', NULL, NULL, 20),
       (13, '澳门叮叮', 15.00, '2025-06-18 15:20:19', '2025-06-18 19:53:24', NULL, 20);
/*!40000 ALTER TABLE `project`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_keywords`
--

DROP TABLE IF EXISTS `project_keywords`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_keywords`
(
    `id`          int                                    NOT NULL AUTO_INCREMENT,
    `keyword`     varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `code_length` int                                    NOT NULL COMMENT '验证码位数',
    `update_at`   datetime DEFAULT NULL,
    `project_id`  int      DEFAULT NULL COMMENT '项目id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_keywords`
--

LOCK TABLES `project_keywords` WRITE;
/*!40000 ALTER TABLE `project_keywords`
    DISABLE KEYS */;
INSERT INTO `project_keywords`
VALUES (2, '【京东】註冊驗證碼：', 6, '2025-06-16 17:58:50', 5),
       (4, '【阿里巴巴】您的验证码:', 4, '2025-06-16 18:14:47', 7),
       (5, '【阿里巴巴】您的验证码:', 4, '2025-06-16 18:26:16', 10),
       (6, '123', 12, '2025-06-18 15:10:31', 11),
       (7, '抖音', 6, '2025-06-18 15:19:08', 6),
       (8, '[DingTalk]Verification Code:', 4, '2025-06-18 15:20:40', 13),
       (10, '【京东】请确认本人操作，切勿泄露给他人。验证码为', 6, '2025-06-18 15:28:36', 5);
/*!40000 ALTER TABLE `project_keywords`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region`
(
    `region_id`         int                                                          NOT NULL AUTO_INCREMENT COMMENT '地区ID',
    `region_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '地区名称',
    `region_mark`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci DEFAULT NULL COMMENT '地区标识',
    `region_created_at` datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `icon`              varchar(255) COLLATE utf8mb4_0900_as_ci                      DEFAULT NULL COMMENT '地区图标',
    PRIMARY KEY (`region_id`) USING BTREE,
    UNIQUE KEY `uk_region_name` (`region_name`) USING BTREE COMMENT '地区名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='地区表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region`
    DISABLE KEYS */;
INSERT INTO `region`
VALUES (6, '香港', '', '2025-05-29 17:31:00',
        'http://123.51.208.249:9000/image/2025/06/110f560657-504f-4e46-a9cf-cdfe0ef62455-HongKong.png'),
       (7, '澳门', '', '2025-06-14 16:22:17', NULL);
/*!40000 ALTER TABLE `region`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_bill`
--

DROP TABLE IF EXISTS `user_bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_bill`
(
    `id`            varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账单编号',
    `bill_type`     tinyint                                NOT NULL COMMENT '账单类型(1:充值，2:消费)',
    `amount`        float                                  NOT NULL COMMENT '账单金额',
    `user_id`       bigint                                 NOT NULL COMMENT '用户id',
    `is_user_type`  int                                    DEFAULT NULL COMMENT '1=客户 ，2=卡商',
    `purchase_time` datetime                               NOT NULL COMMENT '购买时间',
    `remark`        varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `customer_bill_id_index` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_bill`
--

LOCK TABLES `user_bill` WRITE;
/*!40000 ALTER TABLE `user_bill`
    DISABLE KEYS */;
INSERT INTO `user_bill`
VALUES ('1932721751708143618', 2, 20, 1924352187670147074, 0, '2025-06-11 16:49:08', '购买项目'),
       ('1932726785351094274', 1, 20, 1924352187670147074, 0, '2025-06-11 17:09:09', '订单超时未使用退款'),
       ('1933060866894987266', 1, 12, 8259543158, 0, '2025-06-12 15:16:40', '后台充值'),
       ('1933061074227822594', 2, 10, 8259543158, 0, '2025-06-12 15:17:29', '后台扣款'),
       ('1933061249306460162', 1, 300, 8259543158, 0, '2025-06-12 15:18:11', '后台充值'),
       ('1933080379195654145', 1, 5, 8259543158, 0, '2025-06-12 16:34:12', '后台充值'),
       ('1933367358592385025', 1, 5, 8259543158, 0, '2025-06-13 11:34:33', '后台充值'),
       ('1933431769201909761', 2, 10, 1928023018026405889, 0, '2025-06-13 15:50:30', '后台扣款'),
       ('1934550236223279105', 2, 20, 1924352187670147074, 0, '2025-06-16 17:54:53', '购买项目'),
       ('1934555269887201281', 1, 20, 1924352187670147074, 0, '2025-06-16 18:14:53', '订单超时未使用退款'),
       ('1934555454151364610', 2, 5, 1924352187670147074, 0, '2025-06-16 18:15:37', '购买项目'),
       ('1934555510996766721', 2, 5, 1924352187670147074, 0, '2025-06-16 18:15:51', '购买项目'),
       ('1934557999229865985', 2, 1, 1924352187670147074, 0, '2025-06-16 18:25:44', '购买项目');
/*!40000 ALTER TABLE `user_bill`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite`
--

DROP TABLE IF EXISTS `user_favorite`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite`
(
    `favorite_id` int    NOT NULL AUTO_INCREMENT,
    `user_id`     bigint NOT NULL COMMENT '用户id',
    `project_id`  bigint NOT NULL COMMENT '项目id',
    `created_at`  datetime DEFAULT NULL COMMENT '收藏时间',
    PRIMARY KEY (`favorite_id`) USING BTREE,
    KEY `user_favorite_user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户项目收藏';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite`
--

LOCK TABLES `user_favorite` WRITE;
/*!40000 ALTER TABLE `user_favorite`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `user_favorite`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users`
(
    `user_id`         bigint                                                        NOT NULL,
    `account`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci  NOT NULL,
    `password`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
    `nick_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci           DEFAULT NULL,
    `email`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci           DEFAULT NULL,
    `created_at`      datetime                                                               DEFAULT NULL,
    `updated_at`      datetime                                                               DEFAULT NULL,
    `user_avatar`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci          DEFAULT NULL,
    `user_status`     int                                                           NOT NULL DEFAULT '1' COMMENT '用户状态(1=‘正常’   0=‘已注销’)',
    `login_time`      datetime                                                               DEFAULT NULL COMMENT '的登陆时间',
    `invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci           DEFAULT NULL COMMENT '邀请码',
    `money`           float                                                                  DEFAULT '0' COMMENT '￥',
    PRIMARY KEY (`user_id`) USING BTREE,
    KEY `users_user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users`
    DISABLE KEYS */;
INSERT INTO `users`
VALUES (8259543158, '222222', '111111', 'qwe', NULL, '2025-04-26 16:29:54', NULL, NULL, 1, '2025-05-14 18:41:25', '',
        325),
       (1923699357586264066, 'qqqqqq', '$2a$12$U9MZvhHo/aGB6MHL6BUpq.mBb3FmaMqjPg5FYSouAaKQqs87uVFK2', 'qqqqqq', NULL,
        '2025-05-17 19:17:22', NULL, NULL, 1, '2025-05-29 17:36:13', '95e151202a4a4da4b586', 9999),
       (1924352187670147074, '111111', '$2a$12$bxKVlDRSfxguYnUxjgs.f.EnUKREDk61DjlxRtIHrWzvROKbNp/CW', 'ryryte', NULL,
        '2025-05-19 14:31:29', NULL, 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1,
        '2025-06-21 10:27:39', 'c51ddeb799e845458a5a', 8993),
       (1928019445720911874, 'May123', '$2a$12$HC2R6jNUHKsI4rZRTFETS.44zTcqR0r68gB2f0CxKQjaoxpVzoYdC', 'May123', NULL,
        '2025-05-29 17:23:51', NULL, NULL, 1, '2025-06-03 16:34:09', '4ed110e42d834387afe3', 892.4),
       (1928023018026405889, '123123', '$2a$12$mMwDQk1brZGPCSlhtT9Y8OwnlMMnqZyeM3Ct9ndD2QdC69WigSkfm', '123123', NULL,
        '2025-05-29 17:38:03', NULL, NULL, 1, '2025-06-03 16:34:26', '9ca48bfaf35444f19bbb', 953),
       (1928023490401505281, 'admin123', '$2a$12$.u/lxapqh.O9VAkeGm2RL.SkazakrvlfNsdKpvbyfhsHBp5mcyo1.', 'admin123', '',
        '2025-05-29 17:39:56', NULL, NULL, 1, '2025-06-24 15:21:31', NULL, 100),
       (1933795522657390593, '404134600', '$2a$12$cV1tazZ3NwHhfpYJqXv56.hRDsFvwOqSyL7vVfxCOGnqcW/QfUKau', NULL, NULL,
        '2025-06-14 15:55:55', NULL, NULL, 1, '2025-06-14 15:56:04', 'fac69bc697', 0),
       (1934897513777393666, '858303348', '$2a$12$Yvd6FHao0BtpRFf8ekd.geem6KbQwWDy1kCTw5jwp02uKjPQk33J2', NULL, NULL,
        '2025-06-17 16:54:50', NULL, NULL, 1, '2025-06-18 10:16:59', '456bc420b4', 0);
/*!40000 ALTER TABLE `users`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-06-30 10:20:10
