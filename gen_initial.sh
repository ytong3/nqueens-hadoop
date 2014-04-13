#/bin/bash

dim=$1
end=`expr $dim - 1`
for i in $(seq 0 $end) 
	do
		echo "$dim|$i|T"
	done
