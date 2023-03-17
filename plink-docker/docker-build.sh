echo "--------- build start ---------"
cd ..
wget https://mirrors.tuna.tsinghua.edu.cn/AdoptOpenJDK/8/jdk/x64/linux/OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz
mv OpenJDK8U-jdk_x64_linux_openj9_linuxXL_8u282b08_openj9-0.24.0.tar.gz ./plink-docker/plink/
mvn clean package -Dmaven.test.skip=true
cp plink-dist/target/plink*.tar.gz plink-docker/plink
cp config/plink_init.sql plink-docker/mysql
docker build -t plink/plink:master plink-docker/plink
docker build -t plink/mysql:latest plink-docker/mysql
cd plink-docker
echo "--------- build success ---------"
