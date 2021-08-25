
set -x

strtType=$1
serverId="Sig-01"
process_name="seatel"
DATE=$(date "+%Y-%m-%d")
dbStatusUpdate(){ 
p3_query="insert into cdr_process_status (CREATED_ON ,process_name , START_TIME, status ,SERVER_ID ,OPERATOR , modified_on, start_type  )  values( to_date('$DATE','YYYY-MM-DD'), '$1' ,  '$2',   '$3' , '$4' , '$5' ,  to_date('$DATE','YYYY-MM-DD') , '$6' )  "
`sqlplus -s CRESTELCEIR/CRESTELCEIR@//dmc-prod-db:1521/dmcproddb << EOF
   ${p3_query};
    commit 
EOF
        `
}

start_date_time=$(date "+%Y-%m-%d-%H:%M:%S")

dbStatusUpdate "scriptV2"  "$start_date_time"   "Start" "$serverId" "$process_name"  "$strtType"

if [ "$strtType" == "main" ]
then
cd /u01/ceirapp/cdrRecoveryProcess;
./additionRecoverScript.sh $process_name
fi

cd  /u01/ceirapp/cdrpreprocessor/$process_name/
nohup ./script_v2.sh $process_name st_p_gw &
