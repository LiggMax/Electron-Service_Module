/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : electron

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 19/05/2025 14:37:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `user_id` bigint NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '账号',
  `nick_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `email` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '邮箱',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `user_avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `login_time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `phone_number` bigint UNSIGNED NULL DEFAULT NULL COMMENT '号码',
  INDEX `admin_users_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin_users
-- ----------------------------
INSERT INTO `admin_users` VALUES (8259543156, 'ligg', 'lwz', '$2a$12$rRwG1eIEuCgLeIQyArWWz.wwsLozNFWNfR0Rfy8hPlwR/v1kKbzX6', '29544@qq.com', '2025-03-25 16:55:12', '2025-03-25 16:55:08', 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', NULL, NULL);
INSERT INTO `admin_users` VALUES (8259512156, 'lwz', '啊我给发发发', '$2a$12$tTjN1uZIuPEGxQU27fdBR.aUfLDjPabq//15Dt3UNERaLfngl3Qgm', '1241415i@gmail.com', '2025-05-12 18:33:52', '2025-05-12 18:33:55', 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', NULL, NULL);
INSERT INTO `admin_users` VALUES (1922882817093738498, '123456', '123', '$2a$12$GRVcugbFnVC90rcZsLRpkeXSSja3ZVX.6CePwmK21OYH6s/LMMHxe', '', '2025-05-15 13:12:43', NULL, NULL, '2025-05-15 17:07:22', NULL);
INSERT INTO `admin_users` VALUES (1922883816214745090, '11111111', '123123', '$2a$12$YweFJJizjhtvhCFZ/WtNJO71X9P73DoL8bsr8gjEC1IkYRHrz4rRO', '2954494754@qq.com', '2025-05-15 13:16:41', NULL, NULL, '2025-05-17 18:19:08', 12312313);

-- ----------------------------
-- Table structure for admin_web_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_web_user`;
CREATE TABLE `admin_web_user`  (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `email` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `phone_number` bigint NULL DEFAULT NULL COMMENT '手机号码',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11825954457 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_web_user
-- ----------------------------
INSERT INTO `admin_web_user` VALUES (11825954456, 'admin', 'lwz', '1241415i@gmail.com', '$2a$12$E5akMSyHVijdiACdBBTZ4.QHv6w3og8Bz5.DCKnozHzc.U.ZEDVwC', 1231513151, '2025-05-16 17:08:01', '127.0.0.1');

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `create_time` datetime NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `announcement_id_index`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '132', '2025-05-10 18:30:59');
INSERT INTO `announcement` VALUES (2, '132', '2025-05-10 18:31:03');
INSERT INTO `announcement` VALUES (3, '132', '2025-05-10 18:31:29');
INSERT INTO `announcement` VALUES (4, '132', '2025-05-10 18:31:59');
INSERT INTO `announcement` VALUES (5, '1', '2025-05-10 18:32:47');
INSERT INTO `announcement` VALUES (6, '11233131', '2025-05-10 18:36:10');
INSERT INTO `announcement` VALUES (7, '1321', '2025-05-10 18:36:41');
INSERT INTO `announcement` VALUES (8, '12313', '2025-05-10 18:37:04');
INSERT INTO `announcement` VALUES (9, '13212212412222222222222222222222222222222222', '2025-05-10 18:40:43');
INSERT INTO `announcement` VALUES (10, '12313212414', '2025-05-14 18:58:40');

-- ----------------------------
-- Table structure for invitation_relations
-- ----------------------------
DROP TABLE IF EXISTS `invitation_relations`;
CREATE TABLE `invitation_relations`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `inviter_id` bigint NOT NULL COMMENT '邀请人ID',
  `invitee_id` bigint NOT NULL COMMENT '被邀请人Id',
  `invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '使用的邀请码Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `invitation_relations_invitation_code_index`(`invitation_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '邀请码关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invitation_relations
-- ----------------------------

-- ----------------------------
-- Table structure for phone
-- ----------------------------
DROP TABLE IF EXISTS `phone`;
CREATE TABLE `phone`  (
  `phone_id` bigint NOT NULL AUTO_INCREMENT COMMENT '手机号ID',
  `phone_number` bigint NOT NULL COMMENT '手机号码',
  `line_status` tinyint NULL DEFAULT 1 COMMENT '线路状态：1-正常，0-异常',
  `registration_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `usage_status` tinyint NULL DEFAULT 1 COMMENT '使用状态：1-可用，0-不可用',
  `region_id` int NULL DEFAULT NULL COMMENT '所属地区ID',
  `admin_user_id` bigint NULL DEFAULT NULL COMMENT '管理员用户ID',
  PRIMARY KEY (`phone_id`) USING BTREE,
  UNIQUE INDEX `uk_phone_number`(`phone_number` ASC) USING BTREE COMMENT '手机号码唯一索引',
  INDEX `fk_phone_region`(`region_id` ASC) USING BTREE,
  CONSTRAINT `fk_phone_region` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '手机号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phone
-- ----------------------------
INSERT INTO `phone` VALUES (14, 19971539844, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (15, 19971485431, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (16, 1348145965899, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (17, 1323124141, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (18, 11111111111, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (19, 19954459331, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (20, 12345345634, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (21, 12345445544, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (22, 23373737373, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (23, 21847581848, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (24, 12344441918, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);
INSERT INTO `phone` VALUES (25, 12894891918, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090);

-- ----------------------------
-- Table structure for phone_project_relation
-- ----------------------------
DROP TABLE IF EXISTS `phone_project_relation`;
CREATE TABLE `phone_project_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `phone_id` bigint NOT NULL COMMENT '手机号ID',
  `project_id` int NOT NULL COMMENT '项目ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone_project`(`phone_id` ASC, `project_id` ASC) USING BTREE COMMENT '手机号和项目的唯一关联',
  INDEX `fk_relation_project`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '手机号与项目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phone_project_relation
-- ----------------------------
INSERT INTO `phone_project_relation` VALUES (54, 14, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (55, 14, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (56, 14, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (57, 15, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (58, 15, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (59, 15, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (60, 16, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (61, 16, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (62, 16, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (63, 17, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (64, 17, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (65, 17, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (66, 18, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (67, 18, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (68, 18, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (69, 19, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (70, 19, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (71, 19, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (72, 20, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (73, 20, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (74, 20, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (75, 21, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (76, 21, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (77, 21, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (78, 22, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (79, 22, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (80, 22, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (81, 23, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (82, 23, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (83, 23, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (84, 24, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (85, 24, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (86, 24, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (87, 25, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (88, 25, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (89, 25, 3, '2025-05-17 18:19:22');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目ID，使用自增长整数',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '项目名称',
  `project_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '项目价格',
  `project_created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`project_id`) USING BTREE,
  UNIQUE INDEX `uk_project_name`(`project_name` ASC) USING BTREE COMMENT '项目名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, '测试项目1', 100.00, '2025-05-17 14:22:46');
INSERT INTO `project` VALUES (2, '测试项目2', 200.00, '2025-05-17 14:22:46');
INSERT INTO `project` VALUES (3, '测试项目3', 300.00, '2025-05-17 14:22:46');

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`  (
  `region_id` int NOT NULL AUTO_INCREMENT COMMENT '地区ID',
  `region_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '地区名称',
  `region_mark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '地区标识',
  PRIMARY KEY (`region_id`) USING BTREE,
  UNIQUE INDEX `uk_region_name`(`region_name` ASC) USING BTREE COMMENT '地区名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '地区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of region
-- ----------------------------
INSERT INTO `region` VALUES (1, '北京', '京');
INSERT INTO `region` VALUES (2, '上海', '沪');
INSERT INTO `region` VALUES (3, '广州', '粤');

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`  (
  `favorite_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `project_id` bigint NOT NULL COMMENT '项目id',
  `created_at` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  INDEX `user_favorite_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '用户项目收藏' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (1, 8259543156, 2, '2025-04-24 18:52:36');
INSERT INTO `user_favorite` VALUES (2, 8259543156, 1, '2025-04-24 18:53:44');
INSERT INTO `user_favorite` VALUES (3, 8259543156, 3, '2025-04-25 15:53:51');
INSERT INTO `user_favorite` VALUES (4, 8259543156, 4, '2025-04-25 15:53:54');
INSERT INTO `user_favorite` VALUES (5, 8259543156, 0, '2025-04-26 09:50:38');
INSERT INTO `user_favorite` VALUES (6, 8259543157, 1, '2025-04-26 16:26:09');
INSERT INTO `user_favorite` VALUES (7, 8259543157, 2, '2025-04-26 16:26:12');
INSERT INTO `user_favorite` VALUES (8, 8259543158, 1, '2025-04-26 16:30:02');
INSERT INTO `user_favorite` VALUES (9, 8259543158, 2, '2025-04-26 16:30:04');
INSERT INTO `user_favorite` VALUES (10, 8259543159, 1, '2025-04-26 16:31:40');
INSERT INTO `user_favorite` VALUES (11, 8259543159, 2, '2025-04-26 16:31:42');

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `user_project_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `phone_id` bigint NULL DEFAULT NULL COMMENT '手机号ID',
  `created_at` datetime NOT NULL COMMENT '购买时间',
  `project_id` int NULL DEFAULT NULL,
  `phone_number` bigint NOT NULL COMMENT '号码',
  PRIMARY KEY (`user_project_id`) USING BTREE,
  INDEX `user_order_user_project_id_index`(`user_project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 444 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购买的订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_order
-- ----------------------------
INSERT INTO `user_order` VALUES (1, 8259543156, NULL, '2025-04-26 10:26:36', 0, 0);
INSERT INTO `user_order` VALUES (2, 8259543156, NULL, '2025-04-26 10:27:11', 0, 0);
INSERT INTO `user_order` VALUES (3, 8259543156, NULL, '2025-04-26 11:17:45', 0, 0);
INSERT INTO `user_order` VALUES (4, 8259543156, NULL, '2025-04-26 11:24:11', 0, 0);
INSERT INTO `user_order` VALUES (5, 8259543156, NULL, '2025-04-26 13:29:53', 0, 0);
INSERT INTO `user_order` VALUES (6, 8259543156, NULL, '2025-04-26 13:32:29', 0, 0);
INSERT INTO `user_order` VALUES (7, 8259543157, NULL, '2025-04-26 16:28:45', 0, 0);
INSERT INTO `user_order` VALUES (8, 8259543158, NULL, '2025-04-26 16:30:08', 0, 0);
INSERT INTO `user_order` VALUES (9, 8259543158, NULL, '2025-04-26 16:30:11', 0, 0);
INSERT INTO `user_order` VALUES (10, 8259543159, NULL, '2025-04-26 16:31:45', 2, 0);
INSERT INTO `user_order` VALUES (11, 8259543159, NULL, '2025-04-26 16:46:17', 2, 0);
INSERT INTO `user_order` VALUES (12, 8259543159, NULL, '2025-04-26 16:59:22', 2, 0);
INSERT INTO `user_order` VALUES (13, 8259543159, NULL, '2025-04-26 17:15:35', 2, 0);
INSERT INTO `user_order` VALUES (14, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (15, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (16, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (17, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (18, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (19, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (20, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (21, 1, NULL, '2025-04-29 10:41:16', 1, 0);
INSERT INTO `user_order` VALUES (22, 1, NULL, '2025-04-29 10:41:51', 1, 0);
INSERT INTO `user_order` VALUES (23, 1, NULL, '2025-04-29 10:41:51', 1, 0);
INSERT INTO `user_order` VALUES (24, 1, NULL, '2025-04-29 10:41:51', 1, 0);
INSERT INTO `user_order` VALUES (25, 1, NULL, '2025-04-29 10:41:51', 1, 0);
INSERT INTO `user_order` VALUES (26, 1, NULL, '2025-04-29 10:41:51', 1, 0);
INSERT INTO `user_order` VALUES (27, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (28, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (29, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (30, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (31, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (32, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (33, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (34, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (35, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (36, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (37, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (38, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (39, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (40, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (41, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (42, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (43, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (44, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (45, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (46, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (47, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (48, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (49, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (50, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (51, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (52, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (53, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (54, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (55, 1, NULL, '2025-04-29 11:50:14', 1, 0);
INSERT INTO `user_order` VALUES (56, 1, NULL, '2025-04-29 11:50:14', 2, 0);
INSERT INTO `user_order` VALUES (57, 1, NULL, '2025-04-29 11:50:14', 3, 0);
INSERT INTO `user_order` VALUES (58, 1, NULL, '2025-04-29 11:50:14', 4, 0);
INSERT INTO `user_order` VALUES (59, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (60, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (61, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (62, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (63, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (64, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (65, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (66, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (67, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (68, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (69, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (70, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (71, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (72, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (73, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (74, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (75, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (76, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (77, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (78, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (79, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (80, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (81, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (82, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (83, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (84, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (85, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (86, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (87, 1, NULL, '2025-04-29 11:54:33', 1, 0);
INSERT INTO `user_order` VALUES (88, 1, NULL, '2025-04-29 11:54:33', 2, 0);
INSERT INTO `user_order` VALUES (89, 1, NULL, '2025-04-29 11:54:33', 3, 0);
INSERT INTO `user_order` VALUES (90, 1, NULL, '2025-04-29 11:54:33', 4, 0);
INSERT INTO `user_order` VALUES (91, 1, NULL, '2025-04-29 11:56:52', 1, 0);
INSERT INTO `user_order` VALUES (92, 1, NULL, '2025-04-29 11:56:52', 2, 0);
INSERT INTO `user_order` VALUES (93, 1, NULL, '2025-04-29 11:56:52', 3, 0);
INSERT INTO `user_order` VALUES (94, 1, NULL, '2025-04-29 11:56:52', 4, 0);
INSERT INTO `user_order` VALUES (95, 1, NULL, '2025-04-29 11:56:52', 1, 0);
INSERT INTO `user_order` VALUES (96, 1, NULL, '2025-04-29 11:56:52', 2, 0);
INSERT INTO `user_order` VALUES (97, 1, NULL, '2025-04-29 11:56:52', 3, 0);
INSERT INTO `user_order` VALUES (98, 1, NULL, '2025-04-29 11:56:52', 4, 0);
INSERT INTO `user_order` VALUES (99, 1, NULL, '2025-04-29 11:56:52', 1, 0);
INSERT INTO `user_order` VALUES (100, 1, NULL, '2025-04-29 11:56:52', 2, 0);
INSERT INTO `user_order` VALUES (101, 1, NULL, '2025-04-29 11:56:52', 3, 0);
INSERT INTO `user_order` VALUES (102, 1, NULL, '2025-04-29 11:56:52', 4, 0);
INSERT INTO `user_order` VALUES (103, 1, NULL, '2025-04-29 11:56:52', 1, 0);
INSERT INTO `user_order` VALUES (104, 1, NULL, '2025-04-29 11:56:52', 2, 0);
INSERT INTO `user_order` VALUES (105, 1, NULL, '2025-04-29 11:56:52', 3, 0);
INSERT INTO `user_order` VALUES (106, 1, NULL, '2025-04-29 11:56:52', 4, 0);
INSERT INTO `user_order` VALUES (107, 1, NULL, '2025-04-29 11:56:52', 1, 0);
INSERT INTO `user_order` VALUES (108, 1, NULL, '2025-04-29 11:56:52', 2, 0);
INSERT INTO `user_order` VALUES (109, 1, NULL, '2025-04-29 11:56:52', 3, 0);
INSERT INTO `user_order` VALUES (110, 1, NULL, '2025-04-29 11:56:52', 4, 0);
INSERT INTO `user_order` VALUES (111, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (112, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (113, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (114, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (115, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (116, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (117, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (118, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (119, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (120, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (121, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (122, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (123, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (124, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (125, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (126, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (127, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (128, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (129, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (130, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (131, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (132, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (133, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (134, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (135, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (136, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (137, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (138, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (139, 1, NULL, '2025-04-29 12:43:25', 1, 0);
INSERT INTO `user_order` VALUES (140, 1, NULL, '2025-04-29 12:43:25', 2, 0);
INSERT INTO `user_order` VALUES (141, 1, NULL, '2025-04-29 12:43:25', 3, 0);
INSERT INTO `user_order` VALUES (142, 1, NULL, '2025-04-29 12:43:25', 4, 0);
INSERT INTO `user_order` VALUES (143, 8259543159, NULL, '2025-05-10 17:32:57', 1, 0);
INSERT INTO `user_order` VALUES (144, 1, NULL, '2025-05-14 16:37:38', 1, 0);
INSERT INTO `user_order` VALUES (145, 1, NULL, '2025-05-14 16:37:38', 2, 0);
INSERT INTO `user_order` VALUES (146, 1, NULL, '2025-05-14 16:37:38', 3, 0);
INSERT INTO `user_order` VALUES (147, 1, NULL, '2025-05-14 16:37:38', 4, 0);
INSERT INTO `user_order` VALUES (148, 1, NULL, '2025-05-14 16:38:11', 1, 0);
INSERT INTO `user_order` VALUES (149, 1, NULL, '2025-05-14 16:38:11', 2, 0);
INSERT INTO `user_order` VALUES (150, 1, NULL, '2025-05-14 16:38:11', 3, 0);
INSERT INTO `user_order` VALUES (151, 1, NULL, '2025-05-14 16:38:11', 4, 0);
INSERT INTO `user_order` VALUES (152, 1, NULL, '2025-05-14 16:38:11', 1, 0);
INSERT INTO `user_order` VALUES (153, 1, NULL, '2025-05-14 16:38:11', 2, 0);
INSERT INTO `user_order` VALUES (154, 1, NULL, '2025-05-14 16:38:11', 3, 0);
INSERT INTO `user_order` VALUES (155, 1, NULL, '2025-05-14 16:38:11', 4, 0);
INSERT INTO `user_order` VALUES (156, 1, NULL, '2025-05-14 16:38:11', 1, 0);
INSERT INTO `user_order` VALUES (157, 1, NULL, '2025-05-14 16:38:11', 2, 0);
INSERT INTO `user_order` VALUES (158, 1, NULL, '2025-05-14 16:38:11', 3, 0);
INSERT INTO `user_order` VALUES (159, 1, NULL, '2025-05-14 16:38:11', 4, 0);
INSERT INTO `user_order` VALUES (160, 1, NULL, '2025-05-14 16:38:11', 1, 0);
INSERT INTO `user_order` VALUES (161, 1, NULL, '2025-05-14 16:38:11', 2, 0);
INSERT INTO `user_order` VALUES (162, 1, NULL, '2025-05-14 16:38:11', 3, 0);
INSERT INTO `user_order` VALUES (163, 1, NULL, '2025-05-14 16:38:11', 4, 0);
INSERT INTO `user_order` VALUES (164, 1, NULL, '2025-05-14 16:38:11', 1, 0);
INSERT INTO `user_order` VALUES (165, 1, NULL, '2025-05-14 16:38:11', 2, 0);
INSERT INTO `user_order` VALUES (166, 1, NULL, '2025-05-14 16:38:11', 3, 0);
INSERT INTO `user_order` VALUES (167, 1, NULL, '2025-05-14 16:38:11', 4, 0);
INSERT INTO `user_order` VALUES (168, 8259543159, NULL, '2025-05-14 16:42:25', 1, 0);
INSERT INTO `user_order` VALUES (169, 1, NULL, '2025-05-14 17:17:25', 1, 0);
INSERT INTO `user_order` VALUES (170, 1, NULL, '2025-05-14 17:17:25', 2, 0);
INSERT INTO `user_order` VALUES (171, 1, NULL, '2025-05-14 17:17:25', 3, 0);
INSERT INTO `user_order` VALUES (172, 1, NULL, '2025-05-14 17:17:25', 4, 0);
INSERT INTO `user_order` VALUES (173, 8259543159, NULL, '2025-05-14 17:22:46', 1, 0);
INSERT INTO `user_order` VALUES (174, 8259543159, NULL, '2025-05-14 17:22:59', 1, 0);
INSERT INTO `user_order` VALUES (175, 1922935194752610306, NULL, '2025-05-15 17:11:11', 1, 0);
INSERT INTO `user_order` VALUES (176, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (177, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (178, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (179, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (180, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (181, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (182, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (183, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (184, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (185, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (186, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (187, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (188, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (189, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (190, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (191, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (192, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (193, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (194, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (195, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (196, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (197, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (198, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (199, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (200, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (201, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (202, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (203, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (204, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (205, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (206, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (207, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (208, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (209, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (210, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (211, 1, NULL, '2025-05-15 17:44:56', 1, 0);
INSERT INTO `user_order` VALUES (212, 1, NULL, '2025-05-15 17:44:56', 2, 0);
INSERT INTO `user_order` VALUES (213, 1, NULL, '2025-05-15 17:44:56', 3, 0);
INSERT INTO `user_order` VALUES (214, 1, NULL, '2025-05-15 17:44:56', 4, 0);
INSERT INTO `user_order` VALUES (215, 1, NULL, '2025-05-15 17:44:56', 1146335216, 0);
INSERT INTO `user_order` VALUES (216, 1, NULL, '2025-05-16 17:57:15', 1, 0);
INSERT INTO `user_order` VALUES (217, 1, NULL, '2025-05-16 17:57:15', 2, 0);
INSERT INTO `user_order` VALUES (218, 1, NULL, '2025-05-16 17:57:15', 3, 0);
INSERT INTO `user_order` VALUES (219, 1, NULL, '2025-05-16 17:57:15', 4, 0);
INSERT INTO `user_order` VALUES (220, 1, NULL, '2025-05-16 17:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (221, 1, NULL, '2025-05-16 17:57:15', 1, 0);
INSERT INTO `user_order` VALUES (222, 1, NULL, '2025-05-16 17:57:15', 2, 0);
INSERT INTO `user_order` VALUES (223, 1, NULL, '2025-05-16 17:57:15', 3, 0);
INSERT INTO `user_order` VALUES (224, 1, NULL, '2025-05-16 17:57:15', 4, 0);
INSERT INTO `user_order` VALUES (225, 1, NULL, '2025-05-16 17:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (226, 1, NULL, '2025-05-16 17:57:15', 1, 0);
INSERT INTO `user_order` VALUES (227, 1, NULL, '2025-05-16 17:57:15', 2, 0);
INSERT INTO `user_order` VALUES (228, 1, NULL, '2025-05-16 17:57:15', 3, 0);
INSERT INTO `user_order` VALUES (229, 1, NULL, '2025-05-16 17:57:15', 4, 0);
INSERT INTO `user_order` VALUES (230, 1, NULL, '2025-05-16 17:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (231, 1, NULL, '2025-05-16 17:57:15', 1, 0);
INSERT INTO `user_order` VALUES (232, 1, NULL, '2025-05-16 17:57:15', 2, 0);
INSERT INTO `user_order` VALUES (233, 1, NULL, '2025-05-16 17:57:15', 3, 0);
INSERT INTO `user_order` VALUES (234, 1, NULL, '2025-05-16 17:57:15', 4, 0);
INSERT INTO `user_order` VALUES (235, 1, NULL, '2025-05-16 17:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (236, 1, NULL, '2025-05-16 18:37:21', 1, 0);
INSERT INTO `user_order` VALUES (237, 1, NULL, '2025-05-16 18:37:21', 2, 0);
INSERT INTO `user_order` VALUES (238, 1, NULL, '2025-05-16 18:37:21', 3, 0);
INSERT INTO `user_order` VALUES (239, 1, NULL, '2025-05-16 18:37:21', 4, 0);
INSERT INTO `user_order` VALUES (240, 1, NULL, '2025-05-16 18:37:21', 638787560, 0);
INSERT INTO `user_order` VALUES (241, 1, NULL, '2025-05-16 18:37:21', 1, 0);
INSERT INTO `user_order` VALUES (242, 1, NULL, '2025-05-16 18:37:21', 2, 0);
INSERT INTO `user_order` VALUES (243, 1, NULL, '2025-05-16 18:37:21', 3, 0);
INSERT INTO `user_order` VALUES (244, 1, NULL, '2025-05-16 18:37:21', 4, 0);
INSERT INTO `user_order` VALUES (245, 1, NULL, '2025-05-16 18:37:21', 638787560, 0);
INSERT INTO `user_order` VALUES (246, 1, NULL, '2025-05-16 18:37:21', 1, 0);
INSERT INTO `user_order` VALUES (247, 1, NULL, '2025-05-16 18:37:21', 2, 0);
INSERT INTO `user_order` VALUES (248, 1, NULL, '2025-05-16 18:37:21', 3, 0);
INSERT INTO `user_order` VALUES (249, 1, NULL, '2025-05-16 18:37:21', 4, 0);
INSERT INTO `user_order` VALUES (250, 1, NULL, '2025-05-16 18:37:21', 638787560, 0);
INSERT INTO `user_order` VALUES (251, 1, NULL, '2025-05-16 18:37:21', 1, 0);
INSERT INTO `user_order` VALUES (252, 1, NULL, '2025-05-16 18:37:21', 2, 0);
INSERT INTO `user_order` VALUES (253, 1, NULL, '2025-05-16 18:37:21', 3, 0);
INSERT INTO `user_order` VALUES (254, 1, NULL, '2025-05-16 18:37:21', 4, 0);
INSERT INTO `user_order` VALUES (255, 1, NULL, '2025-05-16 18:37:21', 638787560, 0);
INSERT INTO `user_order` VALUES (256, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (257, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (258, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (259, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (260, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (261, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (262, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (263, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (264, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (265, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (266, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (267, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (268, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (269, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (270, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (271, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (272, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (273, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (274, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (275, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (276, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (277, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (278, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (279, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (280, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (281, 1, NULL, '2025-05-16 19:17:11', 1, 0);
INSERT INTO `user_order` VALUES (282, 1, NULL, '2025-05-16 19:17:11', 2, 0);
INSERT INTO `user_order` VALUES (283, 1, NULL, '2025-05-16 19:17:11', 3, 0);
INSERT INTO `user_order` VALUES (284, 1, NULL, '2025-05-16 19:17:11', 4, 0);
INSERT INTO `user_order` VALUES (285, 1, NULL, '2025-05-16 19:17:11', 638787560, 0);
INSERT INTO `user_order` VALUES (286, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (287, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (288, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (289, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (290, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (291, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (292, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (293, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (294, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (295, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (296, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (297, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (298, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (299, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (300, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (301, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (302, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (303, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (304, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (305, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (306, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (307, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (308, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (309, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (310, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (311, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (312, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (313, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (314, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (315, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (316, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (317, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (318, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (319, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (320, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (321, 1, NULL, '2025-05-17 10:26:32', 1, 0);
INSERT INTO `user_order` VALUES (322, 1, NULL, '2025-05-17 10:26:32', 2, 0);
INSERT INTO `user_order` VALUES (323, 1, NULL, '2025-05-17 10:26:32', 3, 0);
INSERT INTO `user_order` VALUES (324, 1, NULL, '2025-05-17 10:26:32', 4, 0);
INSERT INTO `user_order` VALUES (325, 1, NULL, '2025-05-17 10:26:32', 638787560, 0);
INSERT INTO `user_order` VALUES (326, 8259543159, NULL, '2025-05-17 11:06:04', NULL, 0);
INSERT INTO `user_order` VALUES (327, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (328, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (329, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (330, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (331, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (332, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (333, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (334, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (335, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (336, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (337, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (338, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (339, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (340, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (341, 1, NULL, '2025-05-17 11:09:34', 1, 0);
INSERT INTO `user_order` VALUES (342, 1, NULL, '2025-05-17 11:09:34', 2, 0);
INSERT INTO `user_order` VALUES (343, 8259543159, NULL, '2025-05-17 11:13:42', NULL, 0);
INSERT INTO `user_order` VALUES (344, 8259543159, NULL, '2025-05-17 11:16:57', NULL, 0);
INSERT INTO `user_order` VALUES (345, 8259543159, NULL, '2025-05-17 11:32:34', NULL, 0);
INSERT INTO `user_order` VALUES (346, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (347, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (348, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (349, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (350, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (351, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (352, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (353, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (354, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (355, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (356, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (357, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (358, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (359, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (360, 1, NULL, '2025-05-17 11:43:20', 1, 0);
INSERT INTO `user_order` VALUES (361, 1, NULL, '2025-05-17 11:43:20', 3, 0);
INSERT INTO `user_order` VALUES (362, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (363, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (364, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (365, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (366, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (367, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (368, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (369, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (370, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (371, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (372, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (373, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (374, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (375, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (376, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (377, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (378, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (379, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (380, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (381, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (382, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (383, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (384, 1, NULL, '2025-05-17 11:46:23', 638787560, 0);
INSERT INTO `user_order` VALUES (385, 1, NULL, '2025-05-17 11:46:23', 638795360, 0);
INSERT INTO `user_order` VALUES (386, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (387, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (388, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (389, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (390, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (391, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (392, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (393, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (394, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (395, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (396, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (397, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (398, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (399, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (400, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (401, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (402, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (403, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (404, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (405, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (406, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (407, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (408, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (409, 1, NULL, '2025-05-17 11:49:48', 638787560, 0);
INSERT INTO `user_order` VALUES (410, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (411, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (412, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (413, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (414, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (415, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (416, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (417, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (418, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (419, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (420, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (421, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (422, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (423, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (424, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (425, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (426, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (427, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (428, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (429, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (430, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (431, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (432, 1, NULL, '2025-05-17 11:57:15', 638787560, 0);
INSERT INTO `user_order` VALUES (433, 1, NULL, '2025-05-17 11:57:15', 638795360, 0);
INSERT INTO `user_order` VALUES (435, 8259543159, 1, '2025-05-17 15:39:19', 1, 0);
INSERT INTO `user_order` VALUES (437, 8259543159, NULL, '2025-05-17 15:51:58', 1, 0);
INSERT INTO `user_order` VALUES (438, 8259543159, NULL, '2025-05-17 15:53:01', 1, 0);
INSERT INTO `user_order` VALUES (439, 8259543159, NULL, '2025-05-17 15:57:11', 1, 0);
INSERT INTO `user_order` VALUES (440, 8259543159, NULL, '2025-05-17 17:10:55', 1, 12345445544);
INSERT INTO `user_order` VALUES (441, 8259543159, NULL, '2025-05-17 17:11:07', 1, 13800000001);
INSERT INTO `user_order` VALUES (442, 8259543159, NULL, '2025-05-17 17:11:16', 1, 11111111111);
INSERT INTO `user_order` VALUES (443, 8259543159, NULL, '2025-05-17 18:02:40', 1, 12344441918);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` bigint NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `user_status` int NOT NULL DEFAULT 1 COMMENT '用户状态(1=‘正常’   0=‘已注销’)',
  `login_time` datetime NULL DEFAULT NULL COMMENT '的登陆时间',
  `invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '邀请码',
  `money` double NULL DEFAULT 0 COMMENT '￥',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `users_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (8259543156, 'ligg', '111111', '12314', '121412@qq.com', '2025-04-21 10:58:50', '2025-04-21 10:58:46', 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1, '2025-05-15 12:53:25', '', 0);
INSERT INTO `users` VALUES (8259543157, '123456', '$2a$12$XmeLiy0MCGgeMFJ95wENo.tg0/k3z.5ODNIrfm16qb9sgjWke8jAm', NULL, NULL, '2025-04-26 16:25:56', NULL, NULL, 1, '2025-05-15 11:37:35', '', 0);
INSERT INTO `users` VALUES (8259543158, '222222', '111111', 'qwe', NULL, '2025-04-26 16:29:54', NULL, NULL, 1, '2025-05-14 18:41:25', '', 0);
INSERT INTO `users` VALUES (8259543159, '333333', '$2a$12$BzRCp4wFd7pTO6Xv0cdaB.C3BBpcJb4RR2Bm6kYk0ag0E0ixPjlg.', NULL, NULL, '2025-04-26 16:31:31', NULL, 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1, '2025-05-17 17:03:21', '', 0);
INSERT INTO `users` VALUES (1923688101231960065, '555555', '$2a$12$V9ZradzXvnpE2RYgC4t80eg.I7LNjSUVuWTu3JH1o1oYHPWffRUni', NULL, NULL, '2025-05-17 18:32:38', NULL, NULL, 1, '2025-05-17 18:33:02', 'd21f41168ed64208b28c', 0);
INSERT INTO `users` VALUES (1923699357586264066, 'qqqqqq', '$2a$12$QsdkOKcJfEwpYAzdH7OfUOzcpUweh89bUG2TPDzpAmi26SK/dtt62', NULL, NULL, '2025-05-17 19:17:22', NULL, NULL, 1, NULL, '95e151202a4a4da4b586', 0);
INSERT INTO `users` VALUES (1924352187670147074, '111111', '$2a$12$bxKVlDRSfxguYnUxjgs.f.EnUKREDk61DjlxRtIHrWzvROKbNp/CW', NULL, NULL, '2025-05-19 14:31:29', NULL, NULL, 1, '2025-05-19 14:31:48', 'c51ddeb799e845458a5a', 0.004);

-- ----------------------------
-- View structure for view_project_phone_count
-- ----------------------------
DROP VIEW IF EXISTS `view_project_phone_count`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_project_phone_count` AS select `p`.`project_id` AS `project_id`,`p`.`project_name` AS `project_name`,`p`.`project_price` AS `project_price`,count(distinct `ppr`.`phone_id`) AS `phone_count` from ((`project` `p` left join `phone_project_relation` `ppr` on((`p`.`project_id` = `ppr`.`project_id`))) left join `phone` `ph` on(((`ppr`.`phone_id` = `ph`.`phone_id`) and (`ph`.`usage_status` = 1) and (`ph`.`line_status` = 1)))) group by `p`.`project_id`,`p`.`project_name`,`p`.`project_price`;

SET FOREIGN_KEY_CHECKS = 1;
