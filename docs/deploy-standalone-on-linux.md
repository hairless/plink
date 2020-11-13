# 在 linux 上部署 Plink
Plink 进行独立单机部署，可以在 linux 上进行部署，以下是部署的操作详情。

## 环境需求
1. 操作系统
    1. linux
2. 编译环境(非必须)
    1. Java 1.8 +
    2. Maven 3.3 + （编译代码）
3. 运行环境
    1. Apache Flink 1.11 + （Standalone 模式）
    2. MySQL 5.7 +
    3. Java 1.8 +

## 安装 Java

* 版本: java 1.8+
* JAVA_HOME 配置
* 安装详情: 略 。。。

## 安装 Maven(自行编译时需要安装)

* 版本: maven 3.3 +
* MAVEN_HOME 配置
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

## 安装 Apache Flink(已安装的可跳过)

* 版本: flink 1.11+(老版本只能提交flink jar，不能使用flink sql任务)
* 下载: <https://flink.apache.org/downloads.html>
* 解压到合适的目录，假设该目录为 FLINK_HOME
* 环境变量配置 FLINK_HOME（必须，Plink 会用到该环境变量）

### 使用flink standalone

* Flink 配置: jobManager默认地址为<http://127.0.0.1:8081>，可根据需要自行修改
* 启动: 执行 bin 目录下的脚本
    ```shell
    start-cluster.sh
    ```
* 测试: 访问standalone集群(jobManager)正常即可，默认地址<http://127.0.0.1:8081> 

### 使用flink on yarn

* hadoop版本: 2.x+
* 配置环境变量HADOOP_HOME

## 安装 Plink

### 获取plink二进制压缩包

#### 直接下载

* release地址 <https://github.com/hairless/plink/releases>
* 选择最新release中的bin.tar.gz结尾的文件进行下载

#### 自行编译
    ```shell
    git clone https://github.com/hairless/plink.git
    cd plink
    mvn clean package -Dmaven.test.skip=true
    ```
    成功后在 plink/plink-dist/target/ 下会有一个 plink-${version}-bin.tar.gz 文件

### 解压
找到上面的 plink-${version}-bin.tar.gz 文件，找一个合适的目录，假设该目录为 PLINK_HOME 鼠标右键解压，然后切换到 PLINK_HOME 目录。

### 配置
进入 config 文件夹，可以编辑 application-prod.yml(默认)，application-test.yml  等配置文件
    
1. 配置 mysql
    编辑 application-prod.yml，配置 spring.datasource.xxx 等属性。如: 默认的 mysql url 地址为 jdbc:mysql://localhost:3306/plink?useUnicode=true&characterEncoding=utf-8

### 启动
打开 cmd 命令提示符，切换到 PLINK_HOME 目录

```shell
bin/run.sh start
```

访问默认地址: <http://127.0.0.1:8666>，可参考 [使用手册](manual/manual-home.md)

### 关闭
打开 cmd 命令提示符，切换到 PLINK_HOME 目录

```shell
bin/run.sh stop
```

## 示例
1. [运行 Word Count 作业](manual/manual-run-word-count.md): 在 Plink 上提交 Flink 自带的 $FLINK_HOME\examples\streaming\WordCount.jar，然后运行。
