/*
 Navicat Premium Data Transfer

 Source Server         : LOCAL_80
 Source Server Type    : MySQL
 Source Server Version : 80018 (8.0.18)
 Source Host           : localhost:3306
 Source Schema         : data_test

 Target Server Type    : MySQL
 Target Server Version : 80018 (8.0.18)
 File Encoding         : 65001

 Date: 24/03/2023 12:21:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_table
-- ----------------------------
DROP TABLE IF EXISTS `test_table`;
CREATE TABLE `test_table`
(
    `id`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `age`         int(11)                                                       NULL DEFAULT NULL,
    `create_time` datetime                                                      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
