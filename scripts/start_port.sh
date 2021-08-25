#!/bin/bash

VAR=""
cd /u01/ceirapp/
build="port_user_mapping-1.0.jar"

status=`ps -efww | grep "$build $1 $2" | grep java`
echo "$status"
if [ "$status" != "$VAR" ]
then
 echo "nothing"
 echo $status
else
 echo "to start"
 command="java -Dlog4j.configuration=file:./log4j.properties -jar $build -Dspring.config.location=:./application.properties  1> log.txt &"
 echo $command
 nohup $command
fi