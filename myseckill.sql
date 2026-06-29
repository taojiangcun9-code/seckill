/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-05-29 17:29:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sk_goods
-- ----------------------------

CREATE Database myseckill;
use myseckill;
DROP TABLE IF EXISTS `sk_goods`;
CREATE TABLE `sk_goods` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                            `goods_name` varchar(30) DEFAULT NULL COMMENT '商品名称',
                            `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
                            `goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
                            `goods_detail` longtext COMMENT '商品详情',
                            `goods_price` decimal(10,2) DEFAULT NULL,
                            `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sk_goods
-- ----------------------------
INSERT INTO `sk_goods` VALUES ('1', 'iphoneX', 'Apple/苹果iPhone X 全网通4G手机苹果X 10', '/img/iphonex.png', 'Apple/苹果iPhone X 全网通4G手机苹果X 10', '7788.00', '100');
INSERT INTO `sk_goods` VALUES ('2', '华为 Mate 10', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', '/img/meta10.png', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', '4199.00', '50');
INSERT INTO `sk_goods` VALUES ('3', '小米14', 'Xiaomi/小米14 骁龙8 Gen3 全网通5G智能手机', '/img/xiaomi14.png', 'Xiaomi/小米14 骁龙8 Gen3 全网通5G智能手机', '4299.00', '80');
INSERT INTO `sk_goods` VALUES ('4', 'iPad Air', 'Apple iPad Air 10.9英寸 平板电脑 256G', '/img/ipadair.png', 'Apple iPad Air 10.9英寸 平板电脑 256G', '5999.00', '60');
INSERT INTO `sk_goods` VALUES ('5', '华为MatePad', '华为MatePad 11.5英寸 2K高清屏平板电脑', '/img/matepad.png', '华为MatePad 11.5英寸 2K高清屏平板电脑', '2499.00', '70');
INSERT INTO `sk_goods` VALUES ('6', 'AirPods Pro', 'Apple AirPods Pro 主动降噪无线蓝牙耳机', '/img/airpodspro.png', 'Apple AirPods Pro 主动降噪无线蓝牙耳机', '1899.00', '150');
INSERT INTO `sk_goods` VALUES ('7', '华为FreeBuds Pro', '华为FreeBuds Pro 无线降噪蓝牙耳机', '/img/freebuds.png', '华为FreeBuds Pro 无线降噪蓝牙耳机', '1299.00', '120');
INSERT INTO `sk_goods` VALUES ('8', 'MacBook Air', 'Apple MacBook Air 13.6英寸 M3芯片笔记本', '/img/macbookair.png', 'Apple MacBook Air 13.6英寸 M3芯片笔记本', '7999.00', '30');
INSERT INTO `sk_goods` VALUES ('9', '小米手环9', '小米手环9 智能运动手环 心率血氧监测', '/img/band9.png', '小米手环9 智能运动手环 心率血氧监测', '249.00', '200');
INSERT INTO `sk_goods` VALUES ('10', '华为Watch GT4', '华为Watch GT4 智能手表 长续航运动健康', '/img/watchgt4.png', '华为Watch GT4 智能手表 长续航运动健康', '1488.00', '90');

-- ----------------------------
-- Table structure for sk_goods_seckill
-- ----------------------------
DROP TABLE IF EXISTS `sk_goods_seckill`;
CREATE TABLE `sk_goods_seckill` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品id',
                                    `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
                                    `seckill_price` decimal(10,2) DEFAULT '0.00' COMMENT '秒杀价',
                                    `stock_count` int(11) DEFAULT NULL COMMENT '库存数量',
                                    `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
                                    `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
                                    `version` int(11) DEFAULT NULL COMMENT '并发版本控制',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sk_goods_seckill
-- ----------------------------
INSERT INTO `sk_goods_seckill` VALUES ('1', '1', '0.01', '8', '2018-05-22 17:22:52', '2018-05-22 18:23:00', '0');
INSERT INTO `sk_goods_seckill` VALUES ('2', '2', '0.01', '8', '2018-04-29 22:56:10', '2018-05-01 22:56:15', '0');


-- ----------------------------
-- Table structure for sk_order
-- ----------------------------
DROP TABLE IF EXISTS `sk_order`;
CREATE TABLE `sk_order` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `user_id` bigint(20) DEFAULT NULL,
                            `order_id` bigint(20) DEFAULT NULL,
                            `goods_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sk_order
-- ----------------------------
INSERT INTO `sk_order` VALUES ('10', '18718185897', '1', '1');

-- ----------------------------
-- Table structure for sk_order_info
-- ----------------------------
DROP TABLE IF EXISTS `sk_order_info`;
CREATE TABLE `sk_order_info` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 `goods_id` bigint(20) DEFAULT NULL,
                                 `delivery_addr_id` bigint(20) DEFAULT NULL,
                                 `goods_name` varchar(30) DEFAULT NULL,
                                 `goods_count` int(11) DEFAULT NULL,
                                 `goods_price` decimal(10,2) DEFAULT NULL,
                                 `order_channel` tinyint(4) DEFAULT NULL COMMENT '订单渠道，1在线，2android，3ios',
                                 `status` tinyint(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
                                 `create_date` datetime DEFAULT NULL,
                                 `pay_date` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sk_order_info
-- ----------------------------
INSERT INTO `sk_order_info` VALUES ('10', '18718185897', '1', null, 'iphoneX', '1', '7788.00', '1', '0', '2018-05-29 17:02:00', null);

-- ----------------------------
-- Table structure for sk_user
-- ----------------------------
CREATE TABLE `sk_user` (
                           `id` char(20) unsigned NOT NULL COMMENT '用户id',
                           `phone` char(11) NOT NULL COMMENT '手机号，11位',
                           `password` varchar(255) DEFAULT NULL COMMENT '登录密码，明文存储',
                           `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
                           `register_date` datetime DEFAULT NULL COMMENT '注册时间',
                           `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
                           `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_phone` (`phone`) COMMENT '手机号唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀用户表';

-- ----------------------------
-- Records of sk_user
-- ----------------------------
INSERT INTO `sk_user` VALUES
                          (1, '11111111111', '11111111111', NULL, NOW(), NOW(), 1),
                          (2, '22222222222', '22222222222', NULL, NOW(), NOW(), 1),
                          (3, '33333333333', '33333333333', NULL, NOW(), NOW(), 1),
                          (4, '44444444444', '44444444444', NULL, NOW(), NOW(), 1),
                          (5, '55555555555', '55555555555', NULL, NOW(), NOW(), 1);


-- ----------------------------
-- Records of sk_goods_seckill
-- ----------------------------
INSERT INTO `sk_goods_seckill` VALUES
                                   (1, 1, 6188.00, 50, '2025-06-16 00:00:00', '2028-06-16 00:00:00', 0),
                                   (2, 3, 3599.00, 40, '2025-06-16 00:00:00', '2028-06-16 00:00:00', 0),
                                   (3, 5, 1999.00, 50, '2025-06-16 00:00:00', '2028-06-16 00:00:00', 0),
                                   (4, 7,  899.00, 80, '2025-06-16 00:00:00', '2028-06-16 00:00:00', 0);
INSERT INTO `sk_goods_seckill` VALUES
                                   (5, 2, 3299.00, 30, '2010-06-16 00:00:00', '2020-06-16 00:00:00', 0),
                                   (6, 4, 4599.00, 30, '2010-06-16 00:00:00', '2020-06-16 00:00:00', 0),
                                   (7, 6, 1299.00, 60, '2010-06-16 00:00:00', '2020-06-16 00:00:00', 0);

