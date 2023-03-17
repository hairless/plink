FROM mysql:5.7.28

ENV MYSQL_ROOT_PASSWORD 123456

MAINTAINER hairless

# 将初始化脚本放入该目录即可自动执行(docker 特性)
COPY --chown=mysql:mysql plink_init.sql /docker-entrypoint-initdb.d/
RUN  echo "grant all privileges on *.* to 'root'@'%' identified by '${MYSQL_ROOT_PASSWORD}' with grant option;" >> /docker-entrypoint-initdb.d/plink_init.sql
RUN  echo "flush privileges;" >>/docker-entrypoint-initdb.d/plink_init.sql
RUN  echo "character-set-server=utf8" >>/etc/mysql/mysql.conf.d/mysqld.cnf


EXPOSE 3306

ENV JAVA_HOME=/opt/jdk8u282-b08 \
    PLINK_HOME=/opt/plink

ENV PATH=${PATH}:${JAVA_HOME}/bin
