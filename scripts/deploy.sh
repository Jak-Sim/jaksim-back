#!/bin/bash

REPOSITORY=/home/ec2-user/app/step
PROJECT_NAME=Jak-Sim

cd $REPOSITORY

echo "> Check the current running application pid"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*jar)

if [ -z "$CURRENT_PID" ]; then
    echo "> It looks like there are no applications running right now, so there’s nothing to worry about—everything is safe and sound! X)"
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> new application deploy"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
echo "> Add Permissions to $JAR_NAME"
chmod +x $JAR_NAME

echo "> Execute $JAR_NAME"
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &