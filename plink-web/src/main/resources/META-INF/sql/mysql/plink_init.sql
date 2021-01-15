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
    `is_retry`           BOOLEAN            NOT NULL DEFAULT FALSE COMMENT '是否重试',
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

--job_state_info
CREATE TABLE IF NOT EXISTS `job_state_info`
(
  `id`                  BIGINT(20) PRIMARY KEY  NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id`              BIGINT(20)              NOT NULL DEFAULT '-1' COMMENT '任务编号',
  `instance_id`         BIGINT(20)              NOT NULL DEFAULT '-1' COMMENT '任务实例编号',
  `type`                TINYINT(4)              NOT NULL DEFAULT '0' COMMENT '类型 0:checkpoint;1:savepoint',
  `duration`            BIGINT(20)              NOT NULL DEFAULT '0' COMMENT '状态保存耗时，单位毫秒',
  `size`                BIGINT(20)              NOT NULL DEFAULT '0' COMMENT '状态大小，单位字节',
  `external_path`       VARCHAR(500)            NOT NULL DEFAULT '' COMMENT '状态持久化路径',
  `report_timestamp`    BIGINT(20)              NOT NULL DEFAULT '0' COMMENT '上报时间戳',
  `create_time`         TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`         TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
  KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE=INNODB
  DEFAULT CHARSET=utf8 COMMENT ='任务状态信息表';
