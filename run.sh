#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python
#
#python ./src/pharmacy_counting.py ./input/itcont.txt ./output/top_cost_drug.txt

javac ./src/Pharmacy_Counting.java
java -cp ./src Pharmacy_Counting ./input/itcont.txt ./output/top_cost_drug.txt
#java -cp ./src Pharmacy_Counting ./input/test2.txt ./output/test2_output.txt
