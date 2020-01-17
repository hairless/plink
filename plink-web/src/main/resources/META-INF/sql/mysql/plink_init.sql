CREATE DATABASE IF NOT EXISTS plink;

USE plink;

-- 数据库测试表，后期删除
DROP TABLE IF EXISTS plink_test;
CREATE TABLE `plink_test`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `message` VARCHAR(100) NOT NULL COMMENT '信息',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='测试数据表';

INSERT INTO `plink_test`(`message`) VALUES ('hello plink');
INSERT INTO `plink_test`(`message`) VALUES ('hello plink plink');

-- job
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
    `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(255) NOT NULL COMMENT '作业名称',
    `description` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '作业描述',
    `type` TINYINT(4) NOT NULL COMMENT '作业类型',
    `client_version` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '客户端版本',
    `config_json` MEDIUMTEXT NULL COMMENT '作业配置',
    `last_status` TINYINT(4) NULL COMMENT '最新实例的状态',
    `last_app_id` VARCHAR(100) NULL COMMENT '最新实例的app_id',
    `last_start_time` TIMESTAMP NULL COMMENT '最新实例的开始时间',
    `last_stop_time` TIMESTAMP NULL COMMENT '最新实例的结束时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
    UNIQUE KEY `uniq_name` (`name`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='作业表';

-- job_instance
DROP TABLE IF EXISTS `job_instance`;
CREATE TABLE job_instance (
    `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `job_id` BIGINT NOT NULL COMMENT '作业的ID, 即 job 表的 id',
    `config_json` MEDIUMTEXT NULL COMMENT '实例启动时的镜像配置',
    `status` TINYINT(4) NOT NULL COMMENT '实例的状态',
    `app_id` VARCHAR(100) NULL COMMENT '实例的集群任务id',
    `start_time` TIMESTAMP NULL COMMENT '实例的开始时间',
    `stop_time` TIMESTAMP NULL COMMENT '实例的结束时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='作业实例表';