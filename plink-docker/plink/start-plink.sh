#!/bin/bash

# flink cluster
setsid /opt/flink/bin/start-cluster.sh >/var/log/flink-cluster.log 2>&1
## plink
sed -i 's/localhost:3306/mysql-docker:3306/g' /opt/plink/config/application-prod.yml
sed -i 's/localhost:3306/mysql-docker:3306/g' /opt/plink/config/application-local.yml
$PLINK_HOME/bin/run.sh start
