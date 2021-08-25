#!/bin/bash

bin_folder="/u01/bin"
base_path="/u01/ceirapp/cdrpreprocessor"

cd $bin_folder

echo "Initiating Copy"

cp * "$base_path/smart/all/"
cp * "$base_path/smart/sm_ims/" 
cp * "$base_path/smart/sm_msc01/" 
cp * "$base_path/smart/sm_msc02/" 
cp * "$base_path/smart/sm_sgsn_scdr/" 
cp * "$base_path/smart/sm_sgsn_sgwcdr/" 
cp * "$base_path/seatel/all/" 
cp * "$base_path/seatel/st_p_gw" 
cp * "$base_path/cellcard/all/"
cp * "$base_path/cellcard/cc_ggsn" 
cp * "$base_path/cellcard/cc_zmsc71" 
cp * "$base_path/cellcard/cc_zmsc72" 
cp * "$base_path/cellcard/cc_zmsc73" 
cp * "$base_path/metfone/all/" 
cp * "$base_path/metfone/mf_msc09" 
cp * "$base_path/metfone/mf_msc10" 
cp * "$base_path/metfone/mf_msc11"
cp * "$base_path/metfone/mf_msc14" 
cp * "$base_path/metfone/mf_msc15" 
cp * "$base_path/metfone/mf_msc16" 
cp * "$base_path/metfone/mf_sgsn1"

echo "Files Successfully Copied"

while true
do
	echo "Do you want to clear the bin folder (y/n)"
	read response
	if [ "$response" == "y" ]
	then
		rm *
		exit 0
	else if [ "$response" == "n" ]
	then
		exit 0
	else
		echo "Wrong response"
	fi
	fi
done