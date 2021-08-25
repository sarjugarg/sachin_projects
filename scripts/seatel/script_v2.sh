set -x
argc=$#
argv=("$@")
op_name=$1
VAR=""
serverId="Sig-01"
build="FileReaderHash-0.0.1-SNAPSHOT.jar"
alarm="alarm-1.0.jar"
alertCode="alert006"
alarm_jar_path="/u01/ceirapp/scripts/alarm/"
process_name="${argv[0]} pre processor script"
base_path="/u01/ceirapp/cdrpreprocessor/${argv[0]}"
base_path_output="/u02/ceirdata/processed_cdr/${argv[0]}"
output_folder="output"
input_path="/u02/ceirdata/raw_cdr/${argv[0]}/all/"
move_split_files="/u01/ceirapp/scripts/move_final_split_files/"
move_split_files_script_name="move_final_split_files"
all_folder="all"
month=$(date "+%B")
year=$(date "+%Y")
day=$(date "+%d")
Month=${month^^}
count=$((argc - 1))
status_script="/u01/ceirapp/scripts/status/"
output_path="/u01/ceirapp/cdrpreprocessor"
DATE=$(date "+%Y-%m-%d")
p1_query="select count(*) from cdr_pre_processing_report where file_type='O' and created_on>=to_date('$DATE','YYYY-MM-DD') and operator_name='${argv[0]}'"
p2_query="select count(*) from cdr_pre_processing_report where file_type='O' and created_on>=to_date('$DATE','YYYY-MM-DD') and operator_name='${argv[0]}' and source_name='all'"


dbStatusUpdate(){ 
p3_query="insert into cdr_process_status (CREATED_ON ,process_name , START_TIME, status ,SERVER_ID ,OPERATOR , modified_on  )  values( to_date('$DATE','YYYY-MM-DD'), '$1' ,  '$2',   '$3' , '$4' , '$5' ,  to_date('$DATE','YYYY-MM-DD')  )  "
`sqlplus -s CRESTELCEIR/CRESTELCEIR@//dmc-prod-db:1521/dmcproddb << EOF
   ${p3_query};
    commit 
EOF
        `
}


validate(){
query=$1
filename=$2

`sqlplus -s CRESTELCEIR/CRESTELCEIR@//dmc-prod-db:1521/dmcproddb << EOF
   SET ECHO OFF
   SET FEEDBACK OFF
   SET PAGES 0
   SET SERVEROUTPUT ON
   SET VERIFY OFF

   SET head on
   SET COLSEP ,
   SET TRIMSPOOL ON
   set trimout on
   set linesize 1000

   spool "$output_path/${filename}"

   ${query}

   spool off 
EOF
        `

}

# scriptV2
          dbStatusUpdate "scriptV2"  "$start_date_time"   "Start" "$serverId" "${argv[0]}"

# Cleans Up files

#                            /u01/ceirapp/scripts/recovery/cleanUpFiles.sh $op_name $year $Month $day &

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "start at $start_date_time"; popd
echo "start at $start_date_time"
#dbStatusUpdate ""
if [ "$argc" -le 0 ]
then
	echo "No Arguments Found"
	cd $alarm_jar_path
	./start.sh "$alertCode" "No Arguments Found" "$process_name" &
	cd $base_path/
	exit 0
fi

status=`ps -ef | grep $build | grep -v vi|  grep $1 | wc -l`
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
	cd $base_path/
done

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "P1_started at $start_date_time"; popd
echo "P1_started at $start_date_time"
               dbStatusUpdate "P1"  "$start_date_time"   "Start" "$serverId" "${argv[0]}"
echo "waiting for instances to end"
status_final=`ps -ef | grep $build |grep -v vi|  grep $1 | wc -l`
while [ "$status_final" -gt 0 ]
do
   echo "instances running"
   status_final=`ps -ef | grep $build | grep $1 |grep -v vi|  wc -l`
   sleep 15
done

validate "$p1_query" "P1.txt"
wait $!
cd $output_path
result=`cat P1.txt| tr -d " \t\n\r" `

if [ "$result" != "$count" ]
then
	rm P1.txt
#	exit
fi

if [ -f P1.txt ] ; then
    rm P1.txt
fi

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "P1_ended at $start_date_time at $start_date_time"; popd
echo "P1_ended at $start_date_time at $start_date_time"
dbStatusUpdate "P1"  "$start_date_time"   "End" "$serverId" "${argv[0]}"

start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "moving_to_all_folder_started at $start_date_time"; popd
echo "moving_to_all_folder_started at $start_date_time"
	
echo "going to move files to all folder"
for (( j=1; j<argc; j++ )); do
	mv "$base_path_output"/${argv[j]}/"$output_folder"/* "$input_path"
	if [ $? != 0 ]
	then
		echo "Failed to move output of file ${argv[j]}"
		pushd $status_script && ./status.sh "$op_name" "Failed to move output of file ${argv[j]}"; popd
		cd $alarm_jar_path
		./start.sh "$alertCode" "Failed to move output of file ${argv[j]}" "$process_name" &
		cd $base_path/
	else
		echo "output file for ${argv[j]} sucessfully moved to all folder"
	fi
done

#exit 0 ;

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "moving_to_all_folder_ended at $start_date_time"; popd
echo "moving_to_all_folder_ended at $start_date_time"
#dbStatusUpdate "P1"  "$start_date_time"   "Start" "$serverId" "${argv[0]}"

echo "going to start P2"
cd $base_path/$all_folder
./start.sh "${argv[0]}" "$all_folder" &
t1=$?
sleep 5
cd $base_path/
if [ "$t1" != 0 ]
then
	echo "P2 failed to start"
	cd $alarm_jar_path
	./start.sh "$alertCode" "Failed to start P2" "$process_name" &
	cd $base_path/
	exit
else
	echo "P2 started"
fi

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "P2_started at $start_date_time"; popd
echo "P2_started at $start_date_time"
                   dbStatusUpdate "P2"  "$start_date_time"   "Start" "$serverId" "${argv[0]}"

echo "waiting for P2 to end"
status_P2=`ps -ef | grep $build | grep $all_folder`
while [ "$status_P2" != "$VAR" ]
do
	echo "P2 running"
	status_P2=`ps -ef | grep $build |grep -v vi |  grep $all_folder`
	sleep 15
done

validate "$p2_query" "P2.txt"
wait $!
cd $output_path
result=`cat P2.txt| tr -d " \t\n\r" `

if [ "$result" -lt 1 ]
then
	rm P2.txt
#	exit
fi

if [ -f P2.txt ] ; then
    rm P2.txt
fi


#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "P2_ended at $start_date_time"; popd
echo "P2_ended at $start_date_time"
                  dbStatusUpdate "P2"  "$start_date_time"   "End" "$serverId" "${argv[0]}"
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "moving_split_files_started at $start_date_time"; popd
echo "moving_split_files_started at $start_date_time"

echo "going to move split files in cdrprocessor"
cd $move_split_files
./$move_split_files_script_name.sh "${argv[0]}"
if [ $? != 0 ]
	then
		echo "Failed to move split files to cdrprocessor"
		cd $alarm_jar_path
		./start.sh "$alertCode" "Failed to move split files to cdrprocessor" "$process_name" &
		cd $base_path/
		exit 0
	else
		echo "split files moved to cdrprocessor"
fi
cd $base_path/

#milestone
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "moving_split_files_ended at $start_date_time"; popd
echo "moving_split_files_ended at $start_date_time"
start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")
pushd $status_script && ./status.sh "$op_name" "done at $start_date_time"; popd
echo "done at $start_date_time"

#mStone
cd /u01/ceirapp/cdr_process/scripts/
./allOpertorCdr.sh $op_name &


#scriptV2
end_date_timeScriptV2=$(date "+%Y-%m-%d-%H:%M:%S")

#dbStatusUpdate "scriptV2"  "$end_date_timeScriptV2"   "End" "$serverId" "${argv[0]}"

exit 0
