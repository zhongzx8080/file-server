/*
Navicat MySQL Data Transfer

Source Server         : local_mysql
Source Server Version : 50638
Source Host           : localhost:3306
Source Database       : images

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2018-01-17 15:51:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `gmt_upload` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES ('1e03ed0a77324a829355de68fdc551ca', '1e03ed0a77324a829355de68fdc551ca.jpg', '2018-01-17 15:33:13');
INSERT INTO `image` VALUES ('919c382b82ff4e83a7167c2b55577a0e', '919c382b82ff4e83a7167c2b55577a0e.jpg', '2018-01-17 15:35:32');
INSERT INTO `image` VALUES ('1b27fae614514274ac1b44f22f79b60c', '1b27fae614514274ac1b44f22f79b60c.jpg', '2018-01-17 15:37:01');
SET FOREIGN_KEY_CHECKS=1;
