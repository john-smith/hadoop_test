#!/bin/sh

data_get() {
	if [ ! -e $2$1 ]; then
		wget $3$1 -O $2$1
		unzip $2$1 -d $2
		rm -f $2$1
	fi
}

data_dir=src/main/resources/mahout/web_data/

if [ -d $data_dir ]; then
	rm -rf $data_dir
fi
mkdir $data_dir

data_get ml-100k.zip $data_dir http://www.grouplens.org/system/files/
data_get ml-1m.zip $data_dir http://www.grouplens.org/system/files/
data_get libimseti-complete.zip $data_dir http://www.occamslab.com/petricek/data/

