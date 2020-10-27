CREATE DATABASE IF NOT EXISTS plink;

USE plink;

-- job
CREATE TABLE IF NOT EXISTS `job`
(
    `id`                BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`              VARCHAR(255)       NOT NULL COMMENT '作业名称',
    `description`       VARCHAR(1024)      NOT NULL DEFAULT '' COMMENT '作业描述',
    `type`              TINYINT(4)         NOT NULL COMMENT '作业类型',
    `client_version`    VARCHAR(20)        NOT NULL DEFAULT '' COMMENT '客户端版本',
    `flink_config_json` MEDIUMTEXT         NULL COMMENT '作业flink参数配置',
    `extra_config_json` MEDIUMTEXT         NULL COMMENT '作业额外配置',
    `isRetry`           BOOLEAN            NOT NULL DEFAULT FALSE COMMENT '是否重试',
    `last_instance_id`  BIGINT             NULL COMMENT '最新实例的ID',
    `last_status`       TINYINT(4)         NULL COMMENT '最新实例的状态',
    `last_app_id`       VARCHAR(100)       NULL COMMENT '最新实例的app_id',
    `last_start_time`   TIMESTAMP          NULL COMMENT '最新实例的开始时间',
    `last_stop_time`    TIMESTAMP          NULL COMMENT '最新实例的结束时间',
    `create_time`       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    `update_time`       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
    UNIQUE KEY `uniq_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='作业表';

-- job_instance
CREATE TABLE IF NOT EXISTS `job_instance`
(
    `id`                BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `job_id`            BIGINT             NOT NULL COMMENT '作业的ID, 即 job 表的 id',
    `flink_config_json` MEDIUMTEXT         NULL COMMENT '实例启动时的镜像flink参数配置',
    `extra_config_json` MEDIUMTEXT         NULL COMMENT '实例启动时的镜像额外配置',
    `status`            TINYINT(4)         NOT NULL COMMENT '实例的状态',
    `app_id`            VARCHAR(100)       NULL COMMENT '实例的集群任务id',
    `start_time`        TIMESTAMP          NULL COMMENT '实例的开始时间',
    `stop_time`         TIMESTAMP          NULL COMMENT '实例的结束时间',
    `create_time`       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    `update_time`       TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
    KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='作业实例表';