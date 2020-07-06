# 在 Windows 上部署 Plink
Plink 进行独立单机部署，可以在 Windows 上进行部署，以下是部署的操作详情。

## 环境需求
1. 操作系统
    1. Windows
2. 编译环境
    1. Java 1.8 +
    2. Maven 3.3 + （编译代码）
3. 运行环境
    1. Apache Flink 1.9 + （Standalone 模式）
    2. MySQL 5.7 +
    3. Java 1.8 +

## 安装 Java

* 版本: java 1.8+
* JAVA_HOME 配置
* 安装详情: 略 。。。

## 安装 Maven

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

## 安装 Apache Flink

* 版本: flink-1.9.3-bin-scala_2.11（建议: 这里建议使用 1.9.x 版本，因为 1.10+ 版本在 windows 系统上不能开箱即用）
* 下载: <https://mirror.bit.edu.cn/apache/flink/flink-1.9.3/flink-1.9.3-bin-scala_2.11.tgz>
* 解压到合适的目录，假设该目录为 FLINK_HOME
* 环境变量配置 FLINK_HOME（必须，Plink 会用到该环境变量）
* Flink 配置: 无需更改
* 启动: 执行 bin 目录下的脚本
    ```shell
    start-cluster.bat
    ```
* 测试: 访问 <http://127.0.0.1:8081> 正常即可。

## 安装 Plink

### 获取plink二进制压缩包

#### 直接下载

<https://github.com/hairless/plink/releases/download/release-0.1.0/plink-0.1.0-bin.tar.gz>

#### 项目编译
    ```shell
    git clone https://github.com/hairless/plink.git
    cd plink
    mvn clean package -Dmaven.test.skip=true
    ```
    成功后在 plink/plink-dist/target/ 下会有一个 plink-${version}-bin.tar.gz 文件，如: plink-0.1-bin.tar.gz 

### 解压
找到上面的 plink-${version}-bin.tar.gz 文件，找一个合适的目录，假设该目录为 PLINK_HOME 鼠标右键解压，然后切换到 PLINK_HOME 目录。

### 配置
进入 config 文件夹，可以编辑 application.yml, application-local.yml 等配置文件
    
1. 配置 mysql
    编辑 application-local.yml，配置 spring.datasource.xxx 等属性。如: 默认的 mysql url 地址为 jdbc:mysql://localhost:3306/plink?useUnicode=true&characterEncoding=utf-8

### 启动
打开 cmd 命令提示符，切换到 PLINK_HOME 目录

```shell
bin/run.bat start
```

访问默认地址: <http://127.0.0.1:8666>，可参考 [使用手册](manual/manual-home.md)

### 关闭
打开 cmd 命令提示符，切换到 PLINK_HOME 目录

```shell
bin/run.bat stop
```

## 示例
1. [运行 Word Count 作业](manual/manual-run-word-count.md): 在 Plink 上提交 Flink 自带的 flink-1.9.1\examples\streaming\WordCount.jar，然运行。
