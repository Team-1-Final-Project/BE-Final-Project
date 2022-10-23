#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/backend-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

ENCRYPT_PASSWORD=$(cat /home/ubuntu/app/EncPw)

# build 파일 복사
#echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
#cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
#sudo nohup java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log -jar -Xmx1024M -Djasypt.encryptor.password=$ENCRYPT_PASSWORD $JAR_FILE > $APP_LOG 2> $ERROR_LOG &
sudo nohup java -javaagent:/home/ubuntu/scouter/agent.java/conf/scouter.agent.jar -DScouter.config=/home/ubuntu/scouter/agent.java/conf/earth-us.conf -jar -Xmx1024M -Djasypt.encryptor.password=$ENCRYPT_PASSWORD $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG