# Plink支持的flink集群模式
Plink目前支持local(默认)、standalone、yarn三种flink集群模式
## 配置方式
进入 config 文夹，可以编辑 application-prod.yml(默认)，application-test.yml  等配置文件
将cluster.mode设置为对应的模式即可

```yaml
#'local' or 'yarn' or 'standalone'
cluster:
  mode: local
```

## local模式

- local模式是轻量级的flink部署模式，仅适合plink的功能体验
- local模式是在本地plink的jvm内启动flink程序，同时运行多个flink任务可能会导致oom
- local模式集成了flink1.12.1的依赖，因此local模式不需要配置flink客户端。


## standalone模式

- standalone模式适用于有flink standalone集群的用户

### 准备工作

1. 安装flink客户端(1.11+)，并配置FLINK_HOME环境变量到对应的flink客户端路径

## yarn模式

* yarn模式适用于有hadoop集群的用户
* yarn模式目前采用per-job的模式提交任务

### 准备工作

1. 安装flink客户端(1.11+)，并配置FLINK_HOME环境变量到对应的flink客户端路径
2. 安装hadoop客户端，并配置HADOOP_HOME环境变量到对应的hadoop客户端路径
