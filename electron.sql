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

 Date: 14/06/2025 13:35:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `money` float NOT NULL DEFAULT 0 COMMENT '资金',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11825954457 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_web_user
-- ----------------------------
INSERT INTO `admin_web_user`
VALUES (11825954456, 'admin', 'lwz', '1241415i@gmail.com',
        '$2a$12$E5akMSyHVijdiACdBBTZ4.QHv6w3og8Bz5.DCKnozHzc.U.ZEDVwC', 1231513151, '2025-06-14 10:04:14', '127.0.0.1',
        132.59);

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '公告内容',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '公告标题',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `announcement_id_index`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (5, '1', '2025-05-10 18:32:47', '');
INSERT INTO `announcement` VALUES (6, '11233131', '2025-05-10 18:36:10', '');
INSERT INTO `announcement` VALUES (7, '1321', '2025-05-10 18:36:41', '');
INSERT INTO `announcement` VALUES (8, '12313', '2025-05-10 18:37:04', '');

-- ----------------------------
-- Table structure for app_version
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version`
(
    `id`            int                                                           NOT NULL AUTO_INCREMENT,
    `version`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci  NOT NULL COMMENT '版本号',
    `release_notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '更新信息',
    `download_url`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '下载地址',
    `upload_time`   datetime                                                      NOT NULL COMMENT '更新时间',
    `file_size`     bigint                                                        NOT NULL COMMENT '安装包大小',
    `app`           int                                                           NOT NULL COMMENT '0=客户端  1=卡商端',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 32
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '版本更新信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_version
-- ----------------------------
INSERT INTO `app_version`
VALUES (1, '1.1.0', '123', '123', '2025-06-02 17:35:43', 1241, 0);
INSERT INTO `app_version`
VALUES (27, '1.2.0', '新增功能', 'http://test.com/1.2.0.exe', '2025-06-02 17:56:49', 2048000, 0);
INSERT INTO `app_version`
VALUES (28, '1.3.0', '重大更新', 'http://test.com/1.3.0.exe', '2025-06-02 17:56:56', 3048000, 0);
INSERT INTO `app_version`
VALUES (29, '1.27', '版本更新', 'http://123.51.208.249:9000/downloadapp/kehu/2025/06/03JetBrainsMono-2.304.zip',
        '2025-06-03 12:02:46', 5622857, 0);
INSERT INTO `app_version`
VALUES (30, '1.27', '版本更新', 'http://123.51.208.249:9000/downloadapp/kehu/2025/06/03JetBrainsMono-2.304.zip',
        '2025-06-03 12:04:25', 5622857, 0);
INSERT INTO `app_version`
VALUES (31, '1.27', '版本更新', 'http://123.51.208.249:9000/downloadapp/kashang/2025/06/03JetBrainsMono-2.304.zip',
        '2025-06-03 13:38:37', 5622857, 1);

-- ----------------------------
-- Table structure for customer_bill
-- ----------------------------
DROP TABLE IF EXISTS user_bill;
CREATE TABLE `customer_bill`
(
    `id`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '账单编辑号',
    `bill_type`     tinyint                                                      NOT NULL COMMENT '账单类型(1:增加,2:减少)',
    `amount`        float                                                        NULL DEFAULT 0 COMMENT '账单金额',
    `user_id`       bigint                                                       NOT NULL COMMENT '用户id',
    `is_user_type`  int                                                          NOT NULL COMMENT '0=客户 ，1=卡商',
    `purchase_time` datetime                                                     NOT NULL COMMENT '账单时间',
    `remark`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '账单备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '客户账单'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_bill
-- ----------------------------
INSERT INTO user_bill
VALUES ('1931884071186833410', 2, 12.5, 1924352187670147074, 0, '2025-06-09 09:20:30', '购买项目');
INSERT INTO user_bill
VALUES ('1931892088313700354', 2, 2, 1924352187670147074, 0, '2025-06-09 09:52:05', '购买项目');
INSERT INTO user_bill
VALUES ('1931892642679054337', 2, 12.5, 1924352187670147074, 0, '2025-06-09 09:54:08', '购买项目');
INSERT INTO user_bill
VALUES ('1931895866010042369', 2, 2.5, 1924352187670147074, 0, '2025-06-09 10:07:22', '购买项目');
INSERT INTO user_bill
VALUES ('1931897276256731138', 2, 2.5, 1924352187670147074, 0, '2025-06-09 10:12:56', '购买项目');
INSERT INTO user_bill
VALUES ('1931898786386485250', 2, 2.5, 1924352187670147074, 0, '2025-06-09 10:18:58', '购买项目');
INSERT INTO user_bill
VALUES ('1931899038191525890', 1, 2.5, 1924352187670147074, 0, '2025-06-09 10:19:58', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1931900789250580482', 2, 2.5, 1924352187670147074, 0, '2025-06-09 10:26:56', '购买项目');
INSERT INTO user_bill
VALUES ('1931901040992706561', 1, 2.5, 1924352187670147074, 0, '2025-06-09 10:27:56', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1931917254485852162', 2, 2, 1924352187670147074, 0, '2025-06-09 11:32:21', '购买项目');
INSERT INTO user_bill
VALUES ('1931917506286698498', 1, 2, 1924352187670147074, 0, '2025-06-09 11:33:21', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1931917615401517057', 2, 2.5, 1924352187670147074, 0, '2025-06-09 11:33:47', '购买项目');
INSERT INTO user_bill
VALUES ('1931918164939227137', 2, 2.5, 1924352187670147074, 0, '2025-06-09 11:35:58', '购买项目');
INSERT INTO user_bill
VALUES ('1931919560371556354', 2, 2.5, 1924352187670147074, 0, '2025-06-09 11:41:31', '购买项目');
INSERT INTO user_bill
VALUES ('1931949187693522945', 2, 2, 1924352187670147074, 0, '2025-06-09 13:39:15', '购买项目');
INSERT INTO user_bill
VALUES ('1931976380305776641', 2, 2, 1924352187670147074, 0, '2025-06-09 15:27:18', '购买项目');
INSERT INTO user_bill
VALUES ('1931981414821142529', 1, 2, 1924352187670147074, 0, '2025-06-09 15:47:18', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1932282675344953345', 2, 2, 1924352187670147074, 0, '2025-06-10 11:44:24', '购买项目');
INSERT INTO user_bill
VALUES ('1932283214187188226', 2, 12.5, 1924352187670147074, 0, '2025-06-10 11:46:33', '购买项目');
INSERT INTO user_bill
VALUES ('1932283249369010178', 2, 2.5, 1924352187670147074, 0, '2025-06-10 11:46:41', '购买项目');
INSERT INTO user_bill
VALUES ('1932288248186654722', 1, 12.5, 1924352187670147074, 0, '2025-06-10 12:06:33', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1932288282680610817', 1, 2.5, 1924352187670147074, 0, '2025-06-10 12:06:41', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1932295694795902977', 2, 12.5, 1924352187670147074, 0, '2025-06-10 12:36:09', '购买项目');
INSERT INTO user_bill
VALUES ('1932297161896304641', 2, 2, 1924352187670147074, 0, '2025-06-10 12:41:58', '购买项目');
INSERT INTO user_bill
VALUES ('1932324678623526914', 2, 2.5, 1924352187670147074, 0, '2025-06-10 14:31:19', '购买项目');
INSERT INTO user_bill
VALUES ('1932325516616105986', 2, 2, 1924352187670147074, 0, '2025-06-10 14:34:39', '购买项目');
INSERT INTO user_bill
VALUES ('1932720957547573249', 2, 12.5, 1924352187670147074, 0, '2025-06-11 16:45:59', '购买项目');
INSERT INTO user_bill
VALUES ('1932725990905311234', 1, 12.5, 1924352187670147074, 0, '2025-06-11 17:05:59', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933047374336421890', 1, 200, 8259543156, 0, '2025-06-12 14:23:03', '后台充值');
INSERT INTO user_bill
VALUES ('1933047563843465218', 2, 20, 8259543156, 0, '2025-06-12 14:23:48', '后台扣款');
INSERT INTO user_bill
VALUES ('1933047960523960322', 2, 10, 1924352187670147074, 0, '2025-06-12 14:25:23', '后台扣款');
INSERT INTO user_bill
VALUES ('1933361892941836289', 1, 10, 1925129632954384386, 1, '2025-06-13 11:12:50', '后台充值');
INSERT INTO user_bill
VALUES ('1933363358054166530', 1, 5, 1924352187670147074, 0, '2025-06-13 11:18:39', '后台充值');
INSERT INTO user_bill
VALUES ('1933363393114353665', 1, 10, 1924352187670147074, 0, '2025-06-13 11:18:48', '后台充值');
INSERT INTO user_bill
VALUES ('1933364418290356225', 1, 10, 1925129632954384386, 1, '2025-06-13 11:22:52', '后台充值');
INSERT INTO user_bill
VALUES ('1933364788890669057', 2, 10, 1925129632954384386, 1, '2025-06-13 11:24:21', '后台扣款');
INSERT INTO user_bill
VALUES ('1933371063477190658', 2, 123.51, 1925129632954384386, 1, '2025-06-13 11:49:16', '后台扣款');
INSERT INTO user_bill
VALUES ('1933371117193641985', 2, 123.51, 1925129632954384386, 1, '2025-06-13 11:49:29', '后台扣款');
INSERT INTO user_bill
VALUES ('1933371882729570305', 2, 123.51, 1925129632954384386, 1, '2025-06-13 11:52:19', '后台扣款');
INSERT INTO user_bill
VALUES ('1933372007178797058', 2, 123.51, 1925129632954384386, 1, '2025-06-13 11:52:52', '后台扣款');
INSERT INTO user_bill
VALUES ('1933372895435915265', 2, 1444, 1928033125549719553, 1, '2025-06-13 11:56:31', '后台扣款');
INSERT INTO user_bill
VALUES ('1933373071261138945', 2, 154.5, 1928033125549719553, 1, '2025-06-13 11:57:13', '后台扣款');
INSERT INTO user_bill
VALUES ('1933718929202720770', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:51:34', '购买项目');
INSERT INTO user_bill
VALUES ('1933718929202720771', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:51:34', '购买项目');
INSERT INTO user_bill
VALUES ('1933718929202720772', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:51:34', '购买项目');
INSERT INTO user_bill
VALUES ('1933718929202720773', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:51:34', '购买项目');
INSERT INTO user_bill
VALUES ('1933718929202720774', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:51:34', '购买项目');
INSERT INTO user_bill
VALUES ('1933719165623054337', 2, 2.5, 1924352187670147074, 0, '2025-06-14 10:52:31', '购买项目');
INSERT INTO user_bill
VALUES ('1933719274310053890', 2, 2, 1924352187670147074, 0, '2025-06-14 10:52:56', '购买项目');
INSERT INTO user_bill
VALUES ('1933719274310053891', 2, 2, 1924352187670147074, 0, '2025-06-14 10:52:56', '购买项目');
INSERT INTO user_bill
VALUES ('1933719274310053892', 2, 2, 1924352187670147074, 0, '2025-06-14 10:52:56', '购买项目');
INSERT INTO user_bill
VALUES ('1933719274310053893', 2, 2, 1924352187670147074, 0, '2025-06-14 10:52:56', '购买项目');
INSERT INTO user_bill
VALUES ('1933719274310053894', 2, 2, 1924352187670147074, 0, '2025-06-14 10:52:56', '购买项目');
INSERT INTO user_bill
VALUES ('1933719365137707009', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:53:18', '购买项目');
INSERT INTO user_bill
VALUES ('1933719894093971457', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:55:24', '购买项目');
INSERT INTO user_bill
VALUES ('1933719894093971458', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:55:24', '购买项目');
INSERT INTO user_bill
VALUES ('1933719894093971459', 2, 12.5, 1924352187670147074, 0, '2025-06-14 10:55:24', '购买项目');
INSERT INTO user_bill
VALUES ('1933719928600510466', 2, 2, 1924352187670147074, 0, '2025-06-14 10:55:32', '购买项目');
INSERT INTO user_bill
VALUES ('1933719928600510467', 2, 2, 1924352187670147074, 0, '2025-06-14 10:55:32', '购买项目');
INSERT INTO user_bill
VALUES ('1933719956874313730', 2, 2, 1924352187670147074, 0, '2025-06-14 10:55:39', '购买项目');
INSERT INTO user_bill
VALUES ('1933723962530992130', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:11:34', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933723962530992131', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:11:34', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933723962530992132', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:11:34', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933723962803621890', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:11:34', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933723962870730754', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:11:34', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724199391727617', 1, 2.5, 1924352187670147074, 0, '2025-06-14 11:12:31', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724307634130945', 1, 2, 1924352187670147074, 0, '2025-06-14 11:12:56', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724307697045506', 1, 2, 1924352187670147074, 0, '2025-06-14 11:12:56', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724307697045507', 1, 2, 1924352187670147074, 0, '2025-06-14 11:12:56', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724307697045508', 1, 2, 1924352187670147074, 0, '2025-06-14 11:12:56', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724309391544322', 1, 2, 1924352187670147074, 0, '2025-06-14 11:12:57', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724398377897985', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:13:18', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724927539679234', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:15:24', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724927996858369', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:15:24', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724928898633730', 1, 12.5, 1924352187670147074, 0, '2025-06-14 11:15:25', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724961974915074', 1, 2, 1924352187670147074, 0, '2025-06-14 11:15:32', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724961974915075', 1, 2, 1924352187670147074, 0, '2025-06-14 11:15:32', '订单超时未使用退款');
INSERT INTO user_bill
VALUES ('1933724990357770241', 1, 2, 1924352187670147074, 0, '2025-06-14 11:15:39', '订单超时未使用退款');

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
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`
(
    `user_id`      bigint                                                 NOT NULL,
    `account`      varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin  NOT NULL COMMENT '账号',
    `nick_name`    varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin  NULL     DEFAULT NULL COMMENT '昵称',
    `password`     varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
    `email`        varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin  NULL     DEFAULT NULL COMMENT '邮箱',
    `created_at`   datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `updated_at`   datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `user_avatar`  varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL     DEFAULT NULL,
    `login_time`   datetime                                               NULL     DEFAULT NULL COMMENT '登录时间',
    `phone_number` bigint UNSIGNED                                        NULL     DEFAULT NULL COMMENT '号码',
    `money`        float                                                  NOT NULL DEFAULT 0 COMMENT '卡商资金',
    `divide_into`  int                                                    NOT NULL DEFAULT 10 COMMENT '分成',
    INDEX `admin_users_user_id_index` (`user_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT = '卡商'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of merchant
-- ----------------------------
INSERT INTO `merchant`
VALUES (8259543156, 'ligg', 'lwz', '$2a$12$rRwG1eIEuCgLeIQyArWWz.wwsLozNFWNfR0Rfy8hPlwR/v1kKbzX6', '29544@qq.com',
        '2025-03-25 16:55:12', '2025-03-25 16:55:08',
        'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', NULL, NULL, 0, 17);
INSERT INTO `merchant`
VALUES (8259512156, 'lwz', '啊我给发发发', '$2a$12$tTjN1uZIuPEGxQU27fdBR.aUfLDjPabq//15Dt3UNERaLfngl3Qgm',
        '1241415i@gmail.com', '2025-05-12 18:33:52', '2025-05-12 18:33:55',
        'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', NULL, NULL, 0, 34);
INSERT INTO `merchant`
VALUES (1922882817093738498, '123456', '123', '$2a$12$GRVcugbFnVC90rcZsLRpkeXSSja3ZVX.6CePwmK21OYH6s/LMMHxe', '',
        '2025-05-15 13:12:43', NULL, NULL, '2025-05-15 17:07:22', NULL, 0, 50);
INSERT INTO `merchant`
VALUES (1922883816214745090, '11111111', '123123', '$2a$12$RSUtwhQTyfDQR39NPNF97e.rjRQPC1X6QnTFZ9J4F0QoOVUAOGyxy',
        '2954494754@qq.com', '2025-05-15 13:16:41', NULL,
        'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', '2025-05-17 18:19:08', 12312313, 0,
        29);
INSERT INTO `merchant`
VALUES (1925129632954384386, '111111', '测试卡商', '$2a$12$YOTMwVVdHM/Cu.xUl9opHuxNogjZTLaaf3tgGgBECxifesi4p5AWm', '',
        '2025-05-21 18:00:46', NULL, 'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1',
        '2025-06-13 16:23:27', NULL, 0, 12);
INSERT INTO `merchant`
VALUES (1928033125549719553, 'eeeeee', 'eeeeee', '$2a$12$AHEmiDXrpFRpuO2K7sZys.FjZ72FUbSilXjxM763RnTX81s1F.jC2', '',
        '2025-05-29 18:18:13', NULL, NULL, NULL, NULL, 0, 10);

-- ----------------------------
-- Table structure for order_bill
-- ----------------------------
DROP TABLE IF EXISTS `order_bill`;
CREATE TABLE `order_bill`
(
    `id`                int                                                           NOT NULL AUTO_INCREMENT,
    `order_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '订单id',
    `merchant_id`       bigint                                                        NOT NULL COMMENT '卡商id',
    `admin_id`          bigint                                                        NOT NULL COMMENT '平台id',
    `divide_into`       int                                                           NOT NULL COMMENT '抽成比例',
    `remaining_amount`  float                                                         NOT NULL COMMENT '卡商收益',
    `commission_amount` float                                                         NOT NULL COMMENT '平台收益(抽成金额)',
    `start_time`        datetime                                                      NOT NULL COMMENT '结算时间',
    `order_money`       float                                                         NOT NULL COMMENT '订单总价',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '订单账单'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_bill
-- ----------------------------
INSERT INTO `order_bill`
VALUES (1, 'f4226df0-4c9f-4b10-97ce-cb12cd69d79f', 1925129632954384386, 11825954456, 12, 2.2, 0.3,
        '2025-06-05 15:19:15', 2.5);
INSERT INTO `order_bill`
VALUES (2, 'a49a415c-c1ba-47bd-a229-7fb4b86dfa33', 1925129632954384386, 11825954456, 12, 11, 1.5, '2025-06-05 16:28:43',
        12.5);
INSERT INTO `order_bill`
VALUES (3, '31d3d156-2c22-4c60-bd3f-37ebadba7c06', 1925129632954384386, 11825954456, 12, 11, 1.5, '2025-06-05 16:28:45',
        12.5);
INSERT INTO `order_bill`
VALUES (4, '7a699f6d-2a16-42ca-a652-c52626fc39f6', 1925129632954384386, 11825954456, 12, 11, 1.5, '2025-06-05 16:28:48',
        12.5);
INSERT INTO `order_bill`
VALUES (5, '977f353f-e3b0-4e79-af12-c796808ed506', 1925129632954384386, 11825954456, 12, 11, 1.5, '2025-06-05 16:28:51',
        12.5);
INSERT INTO `order_bill`
VALUES (6, '552d11d0-0b47-4340-9ab6-cd925ead7c79', 1925129632954384386, 11825954456, 12, 11, 1.5, '2025-06-10 14:47:49',
        12.5);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `orders_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime NOT NULL COMMENT '购买时间',
  `project_id` int NULL DEFAULT NULL,
  `phone_number` bigint NOT NULL COMMENT '号码',
  `project_money` float NOT NULL COMMENT '项目价格',
  `state` int NOT NULL DEFAULT 0 COMMENT '订单状态  0=未使用   1=已使用 2=已结算',
  `code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '验证码',
  `merchant_id` bigint NULL DEFAULT NULL COMMENT '卡商id',
  `region_id` int NULL DEFAULT NULL COMMENT '地区id',
  PRIMARY KEY (`orders_id`) USING BTREE,
  INDEX `user_order_user_project_id_index`(`orders_id` ASC) USING BTREE,
                           INDEX `user_order_admin_id_index` (`merchant_id` ASC) USING BTREE,
  INDEX `user_order_user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户购买的订单'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders`
VALUES ('278c51f4-c3b4-4a61-b96b-4a4cb1506537', 1924352187670147074, '2025-06-10 14:31:18', 3, 59783955, 2.5, 1, '9718',
        1925129632954384386, 2);
INSERT INTO `orders`
VALUES ('27ec549b-14da-4989-bbb7-b39200576809', 1924352187670147074, '2025-06-10 14:34:38', 2, 59783955, 2, 1, '4484',
        1925129632954384386, 2);
INSERT INTO `orders`
VALUES ('552d11d0-0b47-4340-9ab6-cd925ead7c79', 1924352187670147074, '2025-06-10 12:36:08', 1, 47562095, 12.5, 2,
        '651229', 1925129632954384386, 2);
INSERT INTO `orders`
VALUES ('d3c7331b-a632-4505-af18-49c9822ee6b1', 1924352187670147074, '2025-06-10 12:41:58', 2, 47562095, 2, 1, '8888',
        1925129632954384386, 2);

-- ----------------------------
-- Table structure for phone
-- ----------------------------
DROP TABLE IF EXISTS `phone`;
CREATE TABLE `phone`  (
  `phone_id` bigint NOT NULL AUTO_INCREMENT COMMENT '手机号ID',
  `phone_number` bigint NOT NULL COMMENT '手机号码',
  `registration_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `admin_user_id` bigint NULL DEFAULT NULL COMMENT '管理员用户ID',
  PRIMARY KEY (`phone_id`) USING BTREE,
  UNIQUE INDEX `uk_phone_number`(`phone_number` ASC) USING BTREE COMMENT '手机号码唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 75
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '手机号表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phone
-- ----------------------------
INSERT INTO `phone`
VALUES (67, 98160752, '2025-05-29 18:15:53', 1925129632954384386);
INSERT INTO `phone`
VALUES (68, 47562095, '2025-05-30 11:54:18', 1925129632954384386);
INSERT INTO `phone`
VALUES (69, 59783955, '2025-05-30 11:54:18', 1925129632954384386);
INSERT INTO `phone`
VALUES (70, 47567094, '2025-06-02 10:32:55', 1925129632954384386);
INSERT INTO `phone`
VALUES (71, 12112515, '2025-06-02 10:32:55', 1925129632954384386);
INSERT INTO `phone`
VALUES (72, 12542646, '2025-06-02 10:32:55', 1925129632954384386);
INSERT INTO `phone`
VALUES (73, 216476247, '2025-06-02 10:32:55', 1925129632954384386);
INSERT INTO `phone`
VALUES (74, 37234, '2025-06-02 10:32:55', 1925129632954384386);

-- ----------------------------
-- Table structure for phone_project_relation
-- ----------------------------
DROP TABLE IF EXISTS `phone_project_relation`;
CREATE TABLE `phone_project_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `phone_id` bigint NOT NULL COMMENT '手机号ID',
  `project_id` int NOT NULL COMMENT '项目ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `region_id` int NOT NULL COMMENT '地区ID',
  `is_available` tinyint(1) NOT NULL COMMENT '0 = 未使用  1 = 被购买',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_relation_project`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 377
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '手机号与项目关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of phone_project_relation
-- ----------------------------
INSERT INTO `phone_project_relation`
VALUES (344, 68, 1, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (345, 68, 2, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (346, 68, 3, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (347, 68, 4, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (348, 69, 1, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (349, 69, 2, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (350, 69, 3, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (351, 69, 4, '2025-06-04 15:43:53', 2, 0);
INSERT INTO `phone_project_relation`
VALUES (352, 70, 1, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (353, 70, 2, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (354, 70, 3, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (355, 70, 4, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (356, 70, 10, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (357, 71, 1, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (358, 71, 2, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (359, 71, 3, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (360, 71, 4, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (361, 71, 10, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (362, 72, 1, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (363, 72, 2, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (364, 72, 3, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (365, 72, 4, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (366, 72, 10, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (367, 73, 1, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (368, 73, 2, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (369, 73, 3, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (370, 73, 4, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (371, 73, 10, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (372, 74, 1, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (373, 74, 2, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (374, 74, 3, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (375, 74, 4, '2025-06-13 14:50:34', 1, 0);
INSERT INTO `phone_project_relation`
VALUES (376, 74, 10, '2025-06-13 14:50:34', 1, 0);

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目ID，使用自增长整数',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '项目名称',
  `project_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '项目价格',
  `project_created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `project_update_at` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
  `keyword`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci  NULL DEFAULT NULL COMMENT '解析关键字',
  `code_length`       int                                                           NULL DEFAULT NULL COMMENT '指定关键字后的验证码位数',
  `icon`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '项目图标',
  `keyword_id`        int                                                           NOT NULL COMMENT '关键词id',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '项目表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project`
VALUES (1, '京东', 12.50, '2025-05-17 14:22:46', '2025-06-10 13:58:26', '【京东】请确认本人操作，切勿泄露给他人。验证码为',
        6, 'http://123.51.208.249:9000/image/2025/06/11ddf8d8ce-19e2-418f-81db-eb36e8fea727-京东.png', 0);
INSERT INTO `project`
VALUES (2, '抖音1', 2.00, '2025-05-17 14:22:46', '2025-06-12 14:47:25', '[抖音] 你正在一台新设备上登录账号，验证码', 4,
        'http://123.51.208.249:9000/image/2025/06/1163ade3ab-5d27-49d2-a306-65050fa863ce-抖音.png', 0);
INSERT INTO `project`
VALUES (3, '阿里巴巴', 2.50, '2025-05-17 14:22:46', '2025-06-10 14:33:33', '【阿里巴巴】您的验证码:', 4,
        'http://123.51.208.249:9000/image/2025/06/11537b9499-fff2-45a0-99f6-a2ed8754da7f-淘宝.png', 0);
INSERT INTO `project`
VALUES (4, '高德', 132.00, '2025-05-22 14:48:10', '2025-06-10 14:01:41', '抖音你正在一台新设备上登录账号，验证码', 6,
        'http://123.51.208.249:9000/image/2025/06/1151a1c899-8dc8-4336-a6bb-65095fb5fe68-高德.png', 0);
INSERT INTO `project`
VALUES (10, '阿里巴巴1', 12.00, '2025-06-11 13:53:16', '2025-06-12 14:48:01', '阿里巴巴', 6,
        'http://123.51.208.249:9000/image/2025/06/118d734f53-26ba-42a8-bbbf-5edfd4ef5399-淘宝.png', 0);

-- ----------------------------
-- Table structure for project_keywords
-- ----------------------------
DROP TABLE IF EXISTS `project_keywords`;
CREATE TABLE `project_keywords`
(
    `id`          int                                                          NOT NULL AUTO_INCREMENT,
    `keyword`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '关键词',
    `code_length` int                                                          NOT NULL COMMENT '验证码位数',
    `update_at`   datetime                                                     NOT NULL COMMENT '最后更新时间',
    `project_id`  int                                                          NOT NULL COMMENT '项目id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `poeject_keyWords_id_index` (`id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '项目关键词'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_keywords
-- ----------------------------
INSERT INTO `project_keywords`
VALUES (1, '123', 12, '2025-06-13 18:21:28', 3);
INSERT INTO `project_keywords`
VALUES (2, 'qwe', 12, '2025-06-13 18:21:56', 3);
INSERT INTO `project_keywords`
VALUES (3, '12', 12, '2025-06-13 18:21:58', 3);
INSERT INTO `project_keywords`
VALUES (4, '1212', 12, '2025-06-13 18:22:04', 3);
INSERT INTO `project_keywords`
VALUES (5, '12123', 12, '2025-06-13 18:22:08', 3);
INSERT INTO `project_keywords`
VALUES (6, '1233', 12, '2025-06-13 18:22:10', 3);
INSERT INTO `project_keywords`
VALUES (7, '1231', 12, '2025-06-13 18:22:12', 3);
INSERT INTO `project_keywords`
VALUES (8, '121', 12, '2025-06-13 18:22:14', 3);

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`  (
  `region_id` int NOT NULL AUTO_INCREMENT COMMENT '地区ID',
  `region_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL COMMENT '地区名称',
  `region_mark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '地区标识',
  `region_created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NULL DEFAULT NULL COMMENT '地区图标',
  PRIMARY KEY (`region_id`) USING BTREE,
  UNIQUE INDEX `uk_region_name`(`region_name` ASC) USING BTREE COMMENT '地区名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_ci COMMENT = '地区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of region
-- ----------------------------
INSERT INTO `region`
VALUES (1, '北京', '京', '2025-05-26 11:59:50',
        'http://123.51.208.249:9000/image/2025/06/11156e6364-1c1f-4927-b36d-9515da913ab0-淘宝.png');
INSERT INTO `region`
VALUES (2, '上海1', '沪', '2025-06-12 14:54:10',
        'http://123.51.208.249:9000/image/2025/06/11b878cd06-6c7f-4d89-b05f-63f48802626c-02.png');
INSERT INTO `region`
VALUES (3, '广州市2', '粤', '2025-06-12 14:56:46',
        'http://123.51.208.249:9000/image/2025/06/1135af70a8-f639-4240-b70d-fac7d9e874a4-02.png');

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
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '用户项目收藏'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite`
VALUES (15, 1924352187670147074, 1, '2025-06-04 13:46:24');

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
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_as_ci COMMENT = '客户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES (8259543156, 'ligg', '111111', '12314', '12314@qq.com', '2025-04-21 10:58:50', '2025-04-21 10:58:46',
        'https://lain.bgm.tv/pic/user/l/000/91/64/916400.jpg?r=1726915584&hd=1', 1, '2025-05-15 12:53:25', '1', 440);
INSERT INTO `users`
VALUES (1924352187670147074, '111111', '$2a$12$bxKVlDRSfxguYnUxjgs.f.EnUKREDk61DjlxRtIHrWzvROKbNp/CW', 'ryryte', NULL,
        '2025-05-19 14:31:29', NULL,
        'http://123.51.208.249:9000/useravatar/2025/06/031b6dfe3a-c75c-4935-a8d8-a0c172cda4b5-av.jpg', 1,
        '2025-06-14 10:51:30', 'c51ddeb799e845458a5a', 207);

-- ----------------------------
-- View structure for view_project_phone_count
-- ----------------------------
DROP VIEW IF EXISTS `view_project_phone_count`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_project_phone_count` AS select `p`.`project_id` AS `project_id`,`p`.`project_name` AS `project_name`,`p`.`project_price` AS `project_price`,count(distinct `ppr`.`phone_id`) AS `phone_count` from ((`project` `p` left join `phone_project_relation` `ppr` on((`p`.`project_id` = `ppr`.`project_id`))) left join `phone` `ph` on(((`ppr`.`phone_id` = `ph`.`phone_id`) and (`ph`.`usage_status` = 1) and (`ph`.`line_status` = 1)))) group by `p`.`project_id`,`p`.`project_name`,`p`.`project_price`;

SET FOREIGN_KEY_CHECKS = 1;
