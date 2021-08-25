#!/bin/bash

VAR=""
cd /u01/ceirapp/cdrpreprocessor/$1/$2/
pwd
echo $1 $2
build="FileReaderHash-0.0.1-SNAPSHOT.jar"

status=`ps -efww | grep "$build $1 $2" | grep -v vi | grep java`
echo "$status"
if [ "$status" != "$VAR" ]
then
 echo "nothing"
 echo $status
else
 echo "to start"
 command="java -Dlog4j.configuration=file:./log4j.properties -jar $build "$1" "$2" -Dspring.config.location=:./application.properties  1> ss.txt &"
 echo $command
 nohup $command
fi
