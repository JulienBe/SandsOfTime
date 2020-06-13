#!/bin/bash

filename=`echo "$1" | cut -d'/' -f3 | cut -d'.' -f1`
echo "processing $1 to $filename.txt"
ibt -b -a $1
cat "$filename.txt" | cut -d' ' -f1 > assets/processedmusic/"$filename"
