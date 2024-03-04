/*
 Navicat Premium Data Transfer

 Source Server         : 开发-CES
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 172.16.3.80:3366
 Source Schema         : common_crud

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 25/02/2023 19:47:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE ` file `
(
    `
    id`                int(11) NOT NULL AUTO_INCREMENT, `
    file_path`         varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '文件地址', `
    original_filename` varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '源文件名', `
    file_name`         varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '文件名', `
    del ` bit(
    1
) NOT NULL DEFAULT b '0' COMMENT '0 正常 1删除', ` create_time ` datetime NOT NULL, ` create_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, ` update_time ` datetime NULL DEFAULT NULL, ` update_by`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL, PRIMARY KEY (` id `) USING BTREE
    ) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for info
-- ----------------------------
DROP TABLE IF EXISTS `info`;
CREATE TABLE ` info `
(
    `
    id`            int(11) NOT NULL AUTO_INCREMENT, `
    customer_name` varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '客户名称', `
    project_name`  varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '项目名称', `
    status ` tinyint(
    1
) NOT NULL COMMENT '0 在谈\r\n1 已签约\r\n', ` del ` bit(1)                                                  NOT NULL COMMENT '0 正常 1删除', ` create_time ` datetime NOT NULL, ` create_by`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, ` update_time ` datetime NULL DEFAULT NULL, ` update_by`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL, PRIMARY KEY (` id `) USING BTREE,
    INDEX ` create_by ` (` create_by `) USING BTREE,
    INDEX ` id ` (` customer_name `) USING BTREE,
    CONSTRAINT ` info_ibfk_1` FOREIGN KEY (` create_by `) REFERENCES ` t_user ` (` username `) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for info_accessory
-- ----------------------------
DROP TABLE IF EXISTS `info_accessory`;
CREATE TABLE ` info_accessory `
(
    `
    file_id` int(11) NOT NULL, `
    info_id` int(11) NOT NULL,
    INDEX  ` file_id ` (` file_id `) USING BTREE,
    INDEX  ` info_id ` (` info_id `) USING BTREE,
    CONSTRAINT ` info_accessory_ibfk_1` FOREIGN KEY (` file_id `) REFERENCES ` file ` (` id `) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ` info_accessory_ibfk_2` FOREIGN KEY (` info_id `) REFERENCES ` info ` (` id `) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE ` t_menu `
(
    `
    id`          int(11)   NOT NULL AUTO_INCREMENT, `
    name`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NULL
    DEFAULT
    NULL
    COMMENT
    '权限名称', `
    path`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    'Api地址', `
    code`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NULL
    DEFAULT
    NULL, `
    create_time ` datetime NULL DEFAULT
    NULL, `
    update_time ` datetime NULL DEFAULT
    NULL,
    PRIMARY KEY (` id `) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE ` t_role `
(
    `
    id`          int(11)   NOT NULL AUTO_INCREMENT, `
    role_name`   varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NULL
    DEFAULT
    NULL
    COMMENT
    '角色名称', `
    code`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NULL
    DEFAULT
    NULL
    COMMENT
    '角色代码', `
    create_time ` datetime NULL DEFAULT
    NULL, `
    update_time ` datetime NULL DEFAULT
    NULL,
    PRIMARY KEY (` id `) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE ` t_role_menu `
(
    `
    menu_id` int(11) NOT NULL, `
    role_id` int(11) NOT NULL,
    PRIMARY KEY (` menu_id `, ` role_id `) USING BTREE,
    INDEX  ` t_role_menu_ibfk_2 ` (` role_id `) USING BTREE,
    CONSTRAINT ` t_role_menu_ibfk_1` FOREIGN KEY (` menu_id `) REFERENCES ` t_menu ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT ` t_role_menu_ibfk_2` FOREIGN KEY (` role_id `) REFERENCES ` t_role ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_token
-- ----------------------------
DROP TABLE IF EXISTS `t_token`;
CREATE TABLE ` t_token `
(
    `
    user_id`     int(11)   NOT NULL COMMENT
    '用户id', `
    token`       varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    'token值', `
    create_time ` datetime NULL DEFAULT
    NULL
    COMMENT
    '创建时间', `
    expiration`  timestamp NOT NULL DEFAULT
    CURRENT_TIMESTAMP
    ON
    UPDATE
    CURRENT_TIMESTAMP
    COMMENT
    '过期时间戳',
    PRIMARY KEY (` token `) USING BTREE,
    INDEX ` t_token_ibfk_1 ` (` user_id `) USING BTREE,
    CONSTRAINT ` t_token_ibfk_1` FOREIGN KEY (` user_id `) REFERENCES ` t_user ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC STORAGE MEMORY;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE ` t_user `
(
    `
    id`              int(11)   NOT NULL AUTO_INCREMENT, `
    username`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '用户名', `
    password`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NOT
    NULL
    COMMENT
    '密码', `
    nickname`        varchar(255) CHARACTER
    SET
    utf8
    COLLATE
    utf8_general_ci
    NULL
    DEFAULT
    NULL
    COMMENT
    '昵称', `
    last_login_time ` datetime NULL DEFAULT
    NULL
    COMMENT
    '最后登录时间', `
    valid ` tinyint(
    4
) NOT NULL DEFAULT 1 COMMENT '是否有效(0停用，1启用)', ` del ` bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否已删除(0未删除，1已删除)', ` locked ` tinyint(4)                                              NOT NULL DEFAULT 0 COMMENT '是否已锁定(0未锁定，1已锁定)', ` public_key ` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '公钥', ` private_key ` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '私钥', ` create_time ` datetime NOT NULL, ` create_by`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, ` update_time ` datetime NULL DEFAULT NULL, ` update_by`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL, PRIMARY KEY (` id `) USING BTREE,
    INDEX ` username ` (` username `) USING BTREE
    ) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_lock
-- ----------------------------
DROP TABLE IF EXISTS `t_user_lock`;
CREATE TABLE ` t_user_lock `
(
    `
    id`          int(11)   NOT NULL, `
    user_id`     int(11)   NOT NULL COMMENT
    '用户账号', `
    unlock_time ` datetime NULL DEFAULT
    NULL
    COMMENT
    '解锁时间', `
    create_time ` datetime NULL DEFAULT
    NULL, `
    update_time ` datetime NULL DEFAULT
    NULL,
    PRIMARY KEY (` id `) USING BTREE,
    INDEX ` t_user_lock_ibfk_1 ` (` user_id `) USING BTREE,
    CONSTRAINT ` t_user_lock_ibfk_1` FOREIGN KEY (` user_id `) REFERENCES ` t_user ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE ` t_user_role `
(
    `
    user_id` int(11) NOT NULL, `
    role_id` int(11) NOT NULL,
    PRIMARY KEY (` user_id `, ` role_id `) USING BTREE,
    INDEX  ` t_user_role_ibfk_2 ` (` role_id `) USING BTREE,
    CONSTRAINT ` t_user_role_ibfk_1` FOREIGN KEY (` user_id `) REFERENCES ` t_user ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT ` t_user_role_ibfk_2` FOREIGN KEY (` role_id `) REFERENCES ` t_role ` (` id `) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER
SET = utf8
    COLLATE = utf8_general_ci
    ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
