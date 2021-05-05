FROM centos:centos7

ENV MYSQL_ROOT_PASSWORD 123456

ARG scala_version=2.11
ARG plink_version=0.2.0-SNAPSHOT

MAINTAINER hairless

COPY   plink-${plink_version}-bin.tar.gz OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz  start-plink.sh /tmp/

# RUN apk add --no-cache bash \
#  && /bin/bash \
RUN cp /tmp/start-plink.sh /opt \
 && cp /tmp/plink-${plink_version}-bin.tar.gz /opt/   \
 && tar xf /tmp/plink-${plink_version}-bin.tar.gz -C /opt  \
 && ln -s /opt/plink-${plink_version}-bin /opt/plink     \
 && tar xf /tmp/OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz -C /opt      \
 && rm -rf /tmp/*

EXPOSE 8081
EXPOSE 8666

ENV JAVA_HOME=/opt/jdk8u282-b08 \
    PLINK_HOME=/opt/plink

ENV PATH=${PATH}:${JAVA_HOME}/bin
CMD ["/bin/bash", "/opt/start-plink.sh"]
