FROM centos

ENV MYSQL_ROOT_PASSWORD 123456

ARG flink_version=1.12.1
ARG scala_version=2.11
ARG plink_version=0.2.0-SNAPSHOT

MAINTAINER hairless

COPY flink-${flink_version}-bin-scala_${scala_version}.tgz  plink-${plink_version}-bin.tar.gz OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz  start-plink.sh /tmp/

# RUN apk add --no-cache bash \
#  && /bin/bash \
RUN cp /tmp/start-plink.sh /opt \
 && cp /tmp/plink-${plink_version}-bin.tar.gz /opt/   \
 && tar xf /tmp/plink-${plink_version}-bin.tar.gz -C /opt  \
 && ln -s /opt/plink-${plink_version}-bin /opt/plink     \
 && tar xf /tmp/OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz -C /opt      \
 && tar xf /tmp/flink-${flink_version}-bin-scala_${scala_version}.tgz -C /opt      \
 && ln -s /opt/flink-${flink_version} /opt/flink        \
 && chmod +x /opt/flink/bin/*.sh    \
 && rm -rf /tmp/*

EXPOSE 8081
EXPOSE 8666

ENV JAVA_HOME=/opt/jdk8u282-b08 \
    FLINK_HOME=/opt/flink \
    PLINK_HOME=/opt/plink

ENV PATH=${PATH}:${JAVA_HOME}/bin:${FLINK_HOME}/bin
CMD ["/bin/bash", "/opt/start-plink.sh"]
