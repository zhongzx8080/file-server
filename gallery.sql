/*
Navicat MySQL Data Transfer

Source Server         : local_mysql
Source Server Version : 50638
Source Host           : localhost:3306
Source Database       : gallery

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2018-01-18 13:50:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` varchar(255) DEFAULT NULL COMMENT '图片ID',
  `filename` varchar(255) DEFAULT NULL COMMENT '存储在磁盘的文件名',
  `gmt_upload` varchar(255) DEFAULT NULL COMMENT '图片上传时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES ('5ff5f981a1f94899a751d9826d38f451', '5ff5f981a1f94899a751d9826d38f451.png', '2018-01-18 13:25:38');
INSERT INTO `image` VALUES ('8680822ded684ef2ad0b8e897b7c5651', '8680822ded684ef2ad0b8e897b7c5651.jpg', '2018-01-18 13:49:24');
SET FOREIGN_KEY_CHECKS=1;
