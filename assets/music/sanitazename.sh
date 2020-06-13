#!/bin/bash

input=`echo "$1" | sed 's/\\\//g'`
goodname=`echo $input | sed 's/ //g' | sed 's/\[//g' | sed 's/\]//g'`
echo "moving $input to $goodname"
mv "$input" "$goodname"
