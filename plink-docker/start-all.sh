#!/bin/sh

## jdk

## flink local
setsid /opt/flink/bin/start-cluster.sh > /var/log/flink-cluster.log 2>&1

## mysql
echo "character-set-server=utf8" >> /etc/mysql/mysql.conf.d/mysqld.cnf
service mysql start > /var/log/service-mysql-start.log 2>&1
## mysql privileges
echo "grant all privileges on *.* to 'root'@'%' identified by '${MYSQL_ROOT_PASSWORD}' with grant option;" >> /opt/plink_init.sql
echo "flush privileges;" >> /opt/plink_init.sql
/usr/bin/mysql -uroot -p${MYSQL_ROOT_PASSWORD} < /opt/plink_init.sql > /var/log/mysql-init-plink.log 2>&1

## plink
java -jar /opt/plink.jar
