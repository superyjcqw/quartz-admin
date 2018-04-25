/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.16.193
 Source Server Type    : MariaDB
 Source Server Version : 100300
 Source Host           : 192.168.16.193
 Source Database       : quartz-admin

 Target Server Type    : MariaDB
 Target Server Version : 100300
 File Encoding         : utf-8

 Date: 08/16/2017 21:03:18 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE `quartz-admin` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

-- ----------------------------
--  Table structure for `alarm_contacts`
-- ----------------------------
DROP TABLE IF EXISTS `alarm_contacts`;
CREATE TABLE `alarm_contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `deleted_job`
-- ----------------------------
DROP TABLE IF EXISTS `deleted_job`;
CREATE TABLE `deleted_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(255) DEFAULT NULL COMMENT '集群名',
  `instance_name` varchar(50) NOT NULL COMMENT '集群名',
  `job_name` varchar(50) NOT NULL COMMENT '任务名',
  `job_desc` varchar(100) DEFAULT NULL COMMENT '任务描述',
  `trigger_type` varchar(20) NOT NULL COMMENT '触发器类型',
  `trigger_expression` varchar(50) NOT NULL COMMENT '触发器表达式',
  `time` timestamp NULL DEFAULT current_timestamp() COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `dispatch_log`
-- ----------------------------
DROP TABLE IF EXISTS `dispatch_log`;
CREATE TABLE `dispatch_log` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `cluster_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '集群名',
  `instance_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `begin_time` bigint(13) NOT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `job_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `status` int(1) NOT NULL,
  `execute_duration` int(9) DEFAULT NULL,
  `exception` text COLLATE utf8_bin DEFAULT NULL,
  `exception_notice_status` tinyint(11) DEFAULT 1 COMMENT '异常通知状态',
  `slow_execute_notice_status` tinyint(4) DEFAULT 1 COMMENT '慢执行通知状态',
  PRIMARY KEY (`id`),
  KEY `idx_cluster_name` (`cluster_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `job_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `job_monitor`;
CREATE TABLE `job_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cluster_name` varchar(255) DEFAULT NULL COMMENT '集群名',
  `instance_name` varchar(50) DEFAULT NULL COMMENT '实例名',
  `job_name` varchar(50) DEFAULT NULL COMMENT '任务名',
  `alarm_contacts_ids` varchar(500) DEFAULT NULL COMMENT '报警人id集合',
  `max_execute_time` int(11) DEFAULT NULL COMMENT '最大执行时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_clu_inst_job` (`cluster_name`,`instance_name`,`job_name`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `quartz_cluster`
-- ----------------------------
DROP TABLE IF EXISTS `quartz_cluster`;
CREATE TABLE `quartz_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `instance_name` varchar(50) NOT NULL COMMENT '实例名',
  `datasource` varchar(200) NOT NULL COMMENT '数据源',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` tinyint(20) DEFAULT NULL COMMENT '集群状态 0 正常 1 停用',
  `remote_node_host` varchar(100) DEFAULT NULL COMMENT '远程节点地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `login_name` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10025 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;




INSERT INTO `user` VALUES ('1', '管理员', 'admin', '94d93838bf68891a073ad9c64b7d31cc', '2017-07-13 14:40:25', '1');
