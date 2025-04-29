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

 Date: 29/04/2025 09:59:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '账号',
  `nick_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `email` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '邮箱',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `user_avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  INDEX `admin_users_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8259543157 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for phone
-- ----------------------------
DROP TABLE IF EXISTS `phone`;
CREATE TABLE `phone`  (
  `phone_id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
  `phone_number` bigint NULL DEFAULT NULL COMMENT '手机号码',
  `line_status` int NULL DEFAULT 1 COMMENT '线路状态：1(在线) 、 0(离线)',
  `registration_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `usage_status` int NULL DEFAULT 1 COMMENT '状态：1(正常)、0(停用）',
  `phone_project_id` int NULL DEFAULT NULL COMMENT '项目id',
  `phone_region_id` int NULL DEFAULT NULL COMMENT '号码归属地区',
  PRIMARY KEY (`phone_id`) USING BTREE,
  UNIQUE INDEX `idx_phone_number`(`phone_number` ASC) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_tr_0900_as_cs ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` int NOT NULL AUTO_INCREMENT,
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `project_price` decimal(10, 2) NOT NULL COMMENT '价格',
  `project_created_at` datetime NOT NULL COMMENT '项目创建时间',
  PRIMARY KEY (`project_id`) USING BTREE,
  INDEX `project_project_id_index`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`  (
  `region_id` int NOT NULL AUTO_INCREMENT,
  `region_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
  `region_mark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '地区标识',
  PRIMARY KEY (`region_id`) USING BTREE,
  INDEX `region_region_id_index`(`region_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '地区' ROW_FORMAT = Dynamic;

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
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `user_project_id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `phone_number` bigint NOT NULL COMMENT '号码',
  `created_at` datetime NOT NULL COMMENT '购买时间',
  `project_id` int NOT NULL,
  PRIMARY KEY (`user_project_id`) USING BTREE,
  INDEX `user_order_user_project_id_index`(`user_project_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购买的订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL,
  `user_status` int NOT NULL DEFAULT 1 COMMENT '用户状态(1=‘正常’   0=‘已注销’)',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `users_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8259543160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
