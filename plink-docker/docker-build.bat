echo "--------- build start ---------" & ^
cd .. & ^
set jdk_name=\OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz
bitsadmin.exe /transfer JobName https://mirrors.tuna.tsinghua.edu.cn/AdoptOpenJDK/8/jdk/x64/linux/OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz %cd%\plink-docker\plink%jdk_name% & ^
mvn clean package -Dmaven.test.skip=true & ^
copy /Y plink-dist\target\plink*.tar.gz plink-docker\plink & ^
copy /Y config\plink_init.sql plink-docker\mysql & ^
docker build -t plink/plink:master plink-docker\plink & ^
docker build -t plink/mysql:latest plink-docker\mysql & ^
cd plink-docker & ^
echo "--------- build success ---------"