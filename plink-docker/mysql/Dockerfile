FROM mysql:5.7.28

ENV MYSQL_ROOT_PASSWORD 123456

MAINTAINER plink/mysql

# 将初始化脚本放入改目录即可自动执行(docker 特性)
COPY --chown=mysql:mysql plink_init.sql /docker-entrypoint-initdb.d/
RUN  echo "grant all privileges on *.* to 'root'@'%' identified by '${MYSQL_ROOT_PASSWORD}' with grant option;" >> /docker-entrypoint-initdb.d/plink_init.sql
RUN  echo "flush privileges;" >>/docker-entrypoint-initdb.d/plink_init.sql
RUN  echo "character-set-server=utf8" >>/etc/mysql/mysql.conf.d/mysqld.cnf


EXPOSE 8081
EXPOSE 8666

ENV JAVA_HOME=/opt/jdk8u282-b08 \
    FLINK_HOME=/opt/flink \
    PLINK_HOME=/opt/plink

ENV PATH=${PATH}:${JAVA_HOME}/bin:${FLINK_HOME}/bin
