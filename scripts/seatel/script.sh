#!/bin/bash

argc=$#
argv=("$@")
VAR=""
build="FileReaderHash-0.0.1-SNAPSHOT.jar"
alarm="alarm-1.0.jar"
alertCode="alert006"
alarm_jar_path="/u01/ceirapp/scripts/alarm"
process_name="${argv[0]} pre processor script"
base_path="/u01/ceirapp/cdrpreprocessor/${argv[0]}"
base_path_output="/u01/ceirdata/processed_cdr/${argv[0]}"
output_folder="output"
input_path="/u01/ceirdata/raw_cdr/${argv[0]}/all"
move_split_files="/u01/ceirapp/scripts/move_final_split_files"
move_split_files_script_name="move_final_split_files"
all_folder="all"
count=$((argc - 1))

if [ "$argc" -le 0 ]
then
	echo "No Arguments Found"
	cd $alarm_jar_path
	./start.sh "$alertCode" "No Arguments Found" "$process_name" &
	cd $base_path
	exit 0
fi

status=`ps -ef | grep $build | grep $1 | wc -l`
if [ "$status" -gt 0 ]
then
	echo "process already running"
	exit 0
fi

for (( j=1; j<argc; j++ )); do
    echo "for ${argv[j]}"
	cd $base_path/${argv[j]}
	./start.sh "${argv[0]}" "${argv[j]}" &
	sleep 5
	cd $base_path
done

echo "waiting for instances to end"
status_final=`ps -ef | grep $build | grep $1 | wc -l`
while [ "$status_final" -gt 0 ]
do
   echo "instances running"
   status_final=`ps -ef | grep $build | grep $1 | wc -l`
   sleep 15
done

echo "going to move files to all folder"
for (( j=1; j<argc; j++ )); do
	mv "$base_path_output"/${argv[j]}/"$output_folder"/* "$input_path"
	if [ $? != 0 ]
	then
		echo "Failed to move output of file ${argv[j]}"
		cd $alarm_jar_path
		./start.sh "$alertCode" "Failed to move output of file ${argv[j]}" "$process_name" &
		cd $base_path
	else
		echo "output file for ${argv[j]} sucessfully moved to all folder"
	fi
done

echo "going to start P2"
cd $base_path/$all_folder
./start.sh "${argv[0]}" "$all_folder" &
sleep 5
t1=$?
cd $base_path
if [ "$t1" != 0 ]
then
	echo "P2 failed to start"
	cd $alarm_jar_path
	./start.sh "$alertCode" "Failed to start P2" "$process_name" &
	cd $base_path
	exit
else
	echo "P2 started"
fi

echo "waiting for P2 to end"
status_P2=`ps -ef | grep $build | grep $all_folder`
while [ "$status_P2" != "$VAR" ]
do
	echo "P2 running"
	status_P2=`ps -ef | grep $build | grep $all_folder`
	sleep 15
done

echo "going to move split files in cdrprocessor"
cd $move_split_files
./$move_split_files_script_name.sh "${argv[0]}"
if [ $? != 0 ]
	then
		echo "Failed to move split files to cdrprocessor"
		cd $alarm_jar_path
		./start.sh "$alertCode" "Failed to move split files to cdrprocessor" "$process_name" &
		cd $base_path
		exit 0
	else
		echo "split files moved to cdrprocessor"
fi
cd $base_path

exit 0