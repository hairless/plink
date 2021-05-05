# 使用 Docker 部署 Plink
通过 Docker 进行部署 Plink，分为两种方式：
1. 预构建Docker.(不保证代码为最新)
2. 用户自行Build docker.


## 1. 预构建Docker
> 由于网路原因，当前镜像只上传到了国内云。
### 环境

1. Java 1.8(openjdk) 
2. plink:master
3. MySQL 5.7.28

### 概述
为了可以独立使用，将 plink 环境和  mysql 拆分为2个镜像

### 拉取镜像
```shell
docker pull ccr.ccs.tencentyun.com/plink/mysql:latest  
docker tag ccr.ccs.tencentyun.com/plink/mysql plink/mysql 
docker pull ccr.ccs.tencentyun.com/plink/plink:master    
docker tag ccr.ccs.tencentyun.com/plink/mysql plink/master
```

### 启动镜像
```shell
cd plink-docker
docker-compose up -d
```

### 访问
1. Plink: <http://127.0.0.1:8666>


## 运行 WordCount 作业示例
[运行 Word Count 作业示例](manual/manual-run-word-count.md)

## 查看日志
docker logs ${CONTAINER_ID}

## 自行构建docker 镜像

### 环境需要
1. **openjdk8**
2. maven 3.6 (过低版本会导致build 失败)

### build 方式

- windows
```
plink-docker/docker-build.bat  
docker-compose up -d
```

- linux
```
plink-docker/docker-build.sh 
docker-compose up -d
```