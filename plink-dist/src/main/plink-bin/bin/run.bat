@echo off
cd /d %~dp0

if "%1"=="start" (
    echo "Starting Plink ..."
    cd ..
    java -jar lib\plink-web-0.1-SNAPSHOT.jar --spring.config.location=config/ %--spring.profiles.active=local%
) ^
else if "%1"=="stop" (
    for /f %%i in ('jps -l ^| findstr plink-web') do (
        taskkill /f /pid %%i
    )
    echo "Stopped Plink !"
) ^
else (
    echo "Error! The expected parameters is start | stop ."
)

pause