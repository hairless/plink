# 在 linux 上部署 Plink
Plink 进行独立单机部署，可以在 linux 上进行部署，以下是部署的操作详情。

## 环境需求
1. 操作系统
    1. linux
2. 运行环境
    1. Java 1.8 +
    2. MySQL 5.7 +

## 安装 Java

* 版本: java 1.8+
* JAVA_HOME 配置
* 安装详情: 略 。。。

## 安装 Mysql

* 版本: mysql 5.7+
* 安装建议: 建议 Docker 安装，命令如下:
    ```shell
    docker run -p 3306:3306 --name plink-mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
    ```
* 初始化脚本

    进入 docker mysql 中，执行如下的 sql 语句，创建库和表。
    ```
    https://github.com/hairless/plink/blob/master/plink-web/src/main/resources/META-INF/sql/mysql/plink_init.sql
    ```
* 远程授权
    ```shell
    -- CREATE USER 'root'@'%' IDENTIFIED BY '1234567';
    GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
    FLUSH PRIVILEGES;
    ```

## 安装 Plink

### 获取plink二进制压缩包

可通过 自行编译 和 直接下载两种方式获取plink二进制安装包

#### 1、自行编译
    ```shell
    git clone https://github.com/hairless/plink.git
    cd plink
    mvn clean package -Dmaven.test.skip=true
    ```
    成功后在 plink/plink-dist/target/ 下会有一个 plink-${version}-bin.tar.gz 文件

#### 2、直接下载

* release地址 <https://github.com/hairless/plink/releases>
* 选择最新release中的bin.tar.gz结尾的文件进行下载

### 解压
- 找到上面的 plink-${version}-bin.tar.gz 文件，复制或移动到一个合适的目录进行解压
- 解压后会生成 plink-${version}-bin 的目录，该目录即为PLINK_HOME
- 进入PLINK_HOME目录，目录结构详见[部署目录结构说明](manual/manual-deployment-structure.md)
```shell
tar -zxvf plink-${version}-bin.tar.gz
cd plink-${version}-bin
```

### 配置
进入 config 文件夹，可以编辑 application-prod.yml(默认)，application-test.yml  等配置文件
    
1. 配置 mysql
    编辑 application-prod.yml，配置 spring.datasource.xxx 等属性。如: 默认的 mysql url 地址为 jdbc:mysql://localhost:3306/plink?useUnicode=true&characterEncoding=utf-8
2. 配置flink集群模式
    plink默认为local模式,开箱即用不需要安装flink客户端，详见[集群模式说明](manual/manual-cluster-mode.md)
3. 更多配置详见[plink配置说明](manual/manual-config.md)

### 启动plink
打开命令行，切换到 PLINK_HOME 目录

```shell
bin/run.sh start
```

访问默认地址: <http://127.0.0.1:8666>，可参考 [使用手册](manual/manual-home.md)

### 停止plink
打开命令行，切换到 PLINK_HOME 目录

```shell
bin/run.sh stop
```

## 示例
1. [运行 Word Count 作业](manual/manual-run-word-count.md): 在 Plink 上提交 Flink 自带的 $FLINK_HOME\examples\streaming\WordCount.jar，然后运行。
