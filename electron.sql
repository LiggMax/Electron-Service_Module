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

 Date: 20/05/2025 15:37:25
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
INSERT INTO `admin_web_user` VALUES (11825954456, 'admin', 'lwz', '1241415i@gmail.com', '$2a$12$E5akMSyHVijdiACdBBTZ4.QHv6w3og8Bz5.DCKnozHzc.U.ZEDVwC', 1231513151, '2025-05-20 15:17:22', '127.0.0.1');

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
  `inviter_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '邀请人账号',
  `invitee_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '被邀请人账号',
  `invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '使用的邀请码Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `invitation_relations_invitation_code_index`(`invitation_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '邀请码关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invitation_relations
-- ----------------------------
INSERT INTO `invitation_relations` VALUES (1, 'qqqqqq', 'yyyyyy', '95e151202a4a4da4b586');

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
  `money` double NULL DEFAULT NULL COMMENT '号码价格',
  PRIMARY KEY (`phone_id`) USING BTREE,
  UNIQUE INDEX `uk_phone_number`(`phone_number` ASC) USING BTREE COMMENT '手机号码唯一索引',
  INDEX `fk_phone_region`(`region_id` ASC) USING BTREE,
  CONSTRAINT `fk_phone_region` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '手机号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phone
-- ----------------------------
INSERT INTO `phone` VALUES (14, 19971539844, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (15, 19971485431, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (16, 1348145965899, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (17, 1323124141, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (18, 11111111111, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (19, 19954459331, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (20, 12345345634, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (21, 12345445544, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (22, 23373737373, 1, '2025-05-17 18:19:22', 1, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (23, 21847581848, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (24, 12344441918, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);
INSERT INTO `phone` VALUES (25, 12894891918, 1, '2025-05-17 18:19:22', 0, 1, 1922883816214745090, NULL);

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
INSERT INTO `phone_project_relation` VALUES (60, 16, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (63, 17, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (64, 17, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (66, 18, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (68, 18, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (69, 19, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (70, 19, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (71, 19, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (73, 20, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (74, 20, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (75, 21, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (77, 21, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (78, 22, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (79, 22, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (80, 22, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (81, 23, 1, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (83, 23, 3, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (85, 24, 2, '2025-05-17 18:19:22');
INSERT INTO `phone_project_relation` VALUES (86, 24, 3, '2025-05-17 18:19:22');
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
  `project_update_at` datetime NULL DEFAULT NULL COMMENT '跟新时间',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, '测试项目1', 12.50, '2025-05-17 14:22:46', NULL);
INSERT INTO `project` VALUES (2, '测试项目3', 2.00, '2025-05-17 14:22:46', '2025-05-19 17:32:32');
INSERT INTO `project` VALUES (3, '测试项目', 2.50, '2025-05-17 14:22:46', '2025-05-19 17:59:58');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '用户项目收藏' ROW_FORMAT = Dynamic;

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
INSERT INTO `user_favorite` VALUES (12, 1924352187670147074, 3, '2025-05-19 15:43:10');

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `user_project_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL COMMENT '购买时间',
  `project_id` int NULL DEFAULT NULL,
  `phone_number` bigint NOT NULL COMMENT '号码',
  `money` double NOT NULL COMMENT '订单价格',
  `state` int NOT NULL DEFAULT 0 COMMENT '订单状态  0=未使用   1=已使用',
  `code` int NULL DEFAULT NULL COMMENT '验证码',
  PRIMARY KEY (`user_project_id`) USING BTREE,
  INDEX `user_order_user_project_id_index`(`user_project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 457 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购买的订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_order
-- ----------------------------
INSERT INTO `user_order` VALUES (452, 1924352187670147074, '2025-05-19 18:03:02', 1, 12344441918, 0, 1, NULL);
INSERT INTO `user_order` VALUES (453, 1924352187670147074, '2025-05-19 18:10:56', 3, 1323124141, 0, 1, NULL);
INSERT INTO `user_order` VALUES (454, 1924352187670147074, '2025-05-19 18:12:30', 2, 47548365, 0, 1, NULL);
INSERT INTO `user_order` VALUES (455, 1924352187670147074, '2025-05-19 18:15:41', 2, 47548177, 0, 1, NULL);
INSERT INTO `user_order` VALUES (456, 1924352187670147074, '2025-05-19 18:16:33', 3, 47567094, 2.5, 1, NULL);

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
  `money` float NULL DEFAULT 0 COMMENT '￥',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `users_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (8259543156, 'ligg', '111111', '12314', '', '2025-04-21 10:58:50', '2025-04-21 10:58:46', 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1, '2025-05-15 12:53:25', '', 0.2);
INSERT INTO `users` VALUES (8259543157, '123456', '$2a$12$XmeLiy0MCGgeMFJ95wENo.tg0/k3z.5ODNIrfm16qb9sgjWke8jAm', '123456', NULL, '2025-04-26 16:25:56', NULL, NULL, 1, '2025-05-15 11:37:35', '', 25.2);
INSERT INTO `users` VALUES (8259543158, '222222', '111111', 'qwe', NULL, '2025-04-26 16:29:54', NULL, NULL, 1, '2025-05-14 18:41:25', '', 13);
INSERT INTO `users` VALUES (8259543159, '333333', '$2a$12$BzRCp4wFd7pTO6Xv0cdaB.C3BBpcJb4RR2Bm6kYk0ag0E0ixPjlg.', '333333', '', '2025-04-26 16:31:31', NULL, 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1, '2025-05-17 17:03:21', '', 436.6);
INSERT INTO `users` VALUES (1923688101231960065, '555555', '$2a$12$V9ZradzXvnpE2RYgC4t80eg.I7LNjSUVuWTu3JH1o1oYHPWffRUni', NULL, NULL, '2025-05-17 18:32:38', NULL, NULL, 1, '2025-05-17 18:33:02', 'd21f41168ed64208b28c', 0);
INSERT INTO `users` VALUES (1923699357586264066, 'qqqqqq', '$2a$12$QsdkOKcJfEwpYAzdH7OfUOzcpUweh89bUG2TPDzpAmi26SK/dtt62', NULL, NULL, '2025-05-17 19:17:22', NULL, NULL, 0, NULL, '95e151202a4a4da4b586', 0);
INSERT INTO `users` VALUES (1924352187670147074, '111111', '$2a$12$bxKVlDRSfxguYnUxjgs.f.EnUKREDk61DjlxRtIHrWzvROKbNp/CW', 'ryryte', NULL, '2025-05-19 14:31:29', NULL, NULL, 1, '2025-05-20 14:48:39', 'c51ddeb799e845458a5a', 12.58);
INSERT INTO `users` VALUES (1924727456943812610, 'rrrrrr', '$2a$12$zfKYHkFURAC0fyK0PrZZFeVRPc/7IK.sAppV4fISnwZqruxduVGzu', NULL, NULL, '2025-05-20 15:22:40', NULL, NULL, 1, NULL, '8ecc6bc50a704a31bba3', 0);
INSERT INTO `users` VALUES (1924729756928159746, 'tttttt', '$2a$12$WoD5Qm4/623bWeyd8EIGKeQMz1nk7e23ElpGGSZYu1DNJNw4eRh7C', NULL, NULL, '2025-05-20 15:31:48', NULL, NULL, 1, NULL, 'dcadb9839c434e8fb883', 0);
INSERT INTO `users` VALUES (1924730966280220673, 'yyyyyy', '$2a$12$hNlR.L4ZQXzpOJGUFwK51eU4WA8rcvHj5gkn5FzFqCysUSUBaWYv.', NULL, NULL, '2025-05-20 15:36:36', NULL, NULL, 1, NULL, 'bdbc1730907e4a8fa484', 0);

-- ----------------------------
-- View structure for view_project_phone_count
-- ----------------------------
DROP VIEW IF EXISTS `view_project_phone_count`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_project_phone_count` AS select `p`.`project_id` AS `project_id`,`p`.`project_name` AS `project_name`,`p`.`project_price` AS `project_price`,count(distinct `ppr`.`phone_id`) AS `phone_count` from ((`project` `p` left join `phone_project_relation` `ppr` on((`p`.`project_id` = `ppr`.`project_id`))) left join `phone` `ph` on(((`ppr`.`phone_id` = `ph`.`phone_id`) and (`ph`.`usage_status` = 1) and (`ph`.`line_status` = 1)))) group by `p`.`project_id`,`p`.`project_name`,`p`.`project_price`;

SET FOREIGN_KEY_CHECKS = 1;
