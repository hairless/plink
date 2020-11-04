#!/bin/sh

# shellcheck disable=SC2164
# shellcheck disable=SC2034
SERVER_ROOT_PATH=$(
  cd "$(dirname "$0")"
  cd ..
  pwd
)

cd "$SERVER_ROOT_PATH"

APP_LOG_DIR="${SERVER_ROOT_PATH}/log"
LOG_FILE="$APP_LOG_DIR/console.log"

profile=""
if [ ! -z $2 ]; then
  profile="--spring.profiles.active=$2"
else
  profile="--spring.profiles.active=prod"
fi

JARFILE=$(find "$SERVER_ROOT_PATH/lib/" -name "plink-web-*.jar" -print)

case "$1" in

start)
  if [ $PLINK_HOME ]; then
    echo "PLINK_HOME is $PLINK_HOME"
  else
    export PLINK_HOME=$SERVER_ROOT_PATH
    echo "PLINK_HOME is auto set $PLINK_HOME"
  fi
  if [ ! -d "${APP_LOG_DIR}" ]; then
    mkdir -p "${APP_LOG_DIR}"
  fi
  touch "$LOG_FILE"
  exec java -jar "$JARFILE" "$profile" --spring.config.location=$SERVER_ROOT_PATH/config/ >>"${LOG_FILE}" 2>&1 &
  echo "server starting log to ${LOG_FILE}"
  tail -f -n 0 "${LOG_FILE}"
  ;;

stop)
  pid=$(ps -ef | grep plink-web | grep -v grep | awk '{print $2}')
  if [ -n "$pid" ]; then
    echo "plink-web pid is ${pid}"
    kill "$pid"
    echo "stop plink success"
  else
    echo "stop plink failed,plink is not running"
  fi
  ;;

*)
  echo "Usage: sh run.sh {start} {local|test|prod(default)}"
  ;;

esac

exit 0
