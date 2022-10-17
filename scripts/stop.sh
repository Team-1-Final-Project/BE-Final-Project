#!/usr/bin/env bash

PROJECT_ROOT=/home/ubuntu/app
cd $REPOSITORY

JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"
#JAR_FILE="java"

APP_NAME=spring-webapp.jar

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f $APP_NAME)

# 프로세스가 켜져 있으면 종료
kill -9 $CURRENT_PID