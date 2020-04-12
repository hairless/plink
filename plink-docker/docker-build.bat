echo "--------- build start ---------" & ^
cd .. & ^
mvn clean package -Dmaven.test.skip=true & ^
copy /Y plink-web\target\*.jar plink-docker & ^
copy /Y plink-web\src\main\resources\META-INF\sql\mysql\plink_init.sql plink-docker & ^
docker build -t hairless/plink:master plink-docker & ^
cd plink-docker & ^
echo "--------- build success ---------"
