# 使用 Docker 部署 Plink
可以使用 Docker 进行部署 Plink，以下是部署的操作详情。

> 由于网路原因，当前镜像只上传到了阿里云。

## 环境
1. Java 1.8
2. Apache Flink 1.10.0
3. MySQL 5.7.28
4. plink:master

> 镜像大小: 1.46G

## 概述
当前为了快速获得测试效果，将 JDK，Flink，MySQL，Plink 全部打入到一个镜像了 - - 、

## 拉取镜像
```shell
docker pull registry.cn-hangzhou.aliyuncs.com/hairless/plink:master
```

## 启动镜像
```shell
docker run -ti -p 8666:8666 -p 8081:8081 --name plink -d registry.cn-hangzhou.aliyuncs.com/hairless/plink:master
```

> 本地 FLINK_HOME 覆盖 Docker 镜像中的 Flink，需在 docker 启动时加入参数 : -v FLINK_HOME:/opt/flink

## 访问
1. Plink: <http://127.0.0.1:8666>
2. Flink: <http://127.0.0.1:8081>

## 运行 WordCount 作业示例
[运行 Word Count 作业示例](manual/manual-run-word-count.md)

## 查看日志
docker logs ${CONTAINER_ID}
