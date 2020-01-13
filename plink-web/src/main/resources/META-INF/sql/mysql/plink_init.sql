CREATE DATABASE IF NOT EXISTS plink;

USE plink;

DROP TABLE IF EXISTS plink_test;
CREATE TABLE `plink_test`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `message` VARCHAR(100) NOT NULL COMMENT '信息',
  `ctime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本条记录生成时间',
  `mtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后修改时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='测试数据表';

INSERT INTO `plink_test`(`message`) VALUES ('hello plink');
INSERT INTO `plink_test`(`message`) VALUES ('hello plink plink');

-- job
CREATE TABLE job (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    name TEXT COMMENT '作业名称',
    description TEXT COMMENT '作业描述',
    type TEXT COMMENT '作业类型',
    client_version TEXT COMMENT '客户端版本. eg. 1.9',
    config TEXT COMMENT '作业配置',
    last_status TEXT COMMENT '最新实例的状态',
    last_app_id TEXT COMMENT '最新实例的app_id',
    last_start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新实例的开始时间',
    last_stop_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新实例的结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='作业表';

-- job_instance
CREATE TABLE job_instance (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    job_id BIGINT NOT NULL COMMENT '作业的ID, 即 job 表的 id',
    config TEXT COMMENT '实例的配置',
    status TEXT COMMENT '实例的状态',
    app_id TEXT COMMENT '实例的 app_id',
    start_time TEXT COMMENT '实例的开始时间',
    stop_time TEXT COMMENT '实例的结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='作业实例表';