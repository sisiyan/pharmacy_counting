# Table of Contents
1. [Problem](README.md#problem)
2. [Input and Output file format](README.md#Input and Output file format)
5. [Approach](README.md#Approach)
6. [Run instructions](README.md#Run instructions)
7. [Performance and tests](README.md#Performance and tests)

# Problem

This project is to process the records with information on prescribers (their ID, last name, and first name) and specific prescriptions with drug name and the cost of the medication, to generate a list of all drugs in the input file containing the total number of UNIQUE individuals who prescribed the medication, and the total drug cost, which must be listed in descending order based on the total drug cost and if there is a tie on the total cost, then using based on the descending order of drug name.


# Input and Output file format

The output file, `top_cost_drug.txt`, that contains comma (`,`) separated fields in each line.

Each line of this file should contain these fields:
* drug_name: the exact drug name as shown in the input dataset
* num_prescriber: the number of unique prescribers who prescribed the drug. For the purposes of this challenge, a prescriber is considered the same person if two lines share the same prescriber first and last names
* total_cost: total cost of the drug across all prescribers

For example

If your input data, **`itcont.txt`**, is
```
id,prescriber_last_name,prescriber_first_name,drug_name,drug_cost
1000000001,Smith,James,AMBIEN,100
1000000002,Garcia,Maria,AMBIEN,200
1000000003,Johnson,James,CHLORPROMAZINE,1000
1000000004,Rodriguez,Maria,CHLORPROMAZINE,2000
1000000005,Smith,David,BENZTROPINE MESYLATE,1500
```

then your output file, **`top_cost_drug.txt`**, would contain the following lines
```
drug_name,num_prescriber,total_cost
CHLORPROMAZINE,2,3000
BENZTROPINE MESYLATE,1,1500
AMBIEN,2,300
```

# Approach
## Language and Libraries
My code was written for and tested by Java 8. As the instructions required, only the default libraries java.io and java.util were used.

## Algorithm and step
1. Check whether a input filename is given from the command line. If not, stop and print a warning telling the user to give a input file.
2. Create one HashMap<String, HashSet<String>> to store the pair of <drug_name, set of unique prescriber names>. Create one HashMap<String, Float> to store the pair of <drug_name, total cost of that drug>. Create a TreeMap to keep the processed results for each drug in the descending order of total cost. Since there might be duplicate keys if two drugs have the same total cost, each value associated a key is a list of drug cost lines.
3. For the input file with 24 Million records, if I process all the records at once, the memory might be exhausted for weak laptops. Therefore, I process the input records in separate batches. The code will process of all the records with the drug name in alphabet order. So it processes starting with 'A' first. After adding all records with drug name starting with 'A' into the HashMaps and then generate lines and put to the TreeMap, the contents in the two HashMaps will be cleared. Then, records starting with 'B', 'C',...'Z' are processed. At last, the code processes the record with drug name starting with other characters. In this way, not too much data kept in memory, which avoids the memory exhaustion.
4. When all the result lines are added to the TreeMap, the contents in the TreeMap will be written to the output file. When writing, for those values in the TreeMap with multiple lines (drugs with the same total costs), the lines in each value list will be sorted according to the descending order of the drug name. Otherwise, write the only line in the value list to the output file.

# Run instructions
## To run it from the run.sh
Add the following two commands under #!/bin/bash
javac ./src/Pharmacy_Counting.java
java -cp ./src Pharmacy_Counting ./input/itcont.txt ./output/top_cost_drug.txt
Feel free to change the input file name and output file name.
If no file name is given, a warning message will be shown and tells the user to give a input file.
If only one file name is given, that file will be treat as the input file. An output file with default name output.txt will be generated in the ./output directory.
If all records of the input file are not in the correct format, a warning message will be shown, and no output file will be generated.


# Performance and tests
1. The Pharmacy_Counting.java passes the test in insight_testsuite.
2. It correctly processes the 24-million-records file de_cc_data.txt. It takes about 13 mins on my laptop to generate the sorted output file. Since the output total costs are required to be integer values, as long as the final rounded integer costs are the same (though there total cost in float number may slightly different), two or more drugs are considered as a tie of having the same and they are listed according to descending order of their names. (It is easy to sort according to float value, but it might be confusing if the output are the same integer but drug name not in order.)
3. If wrong input filename are given, it will throw exceptions.
4. There is a trade off between time complexity and space complexity. To avoid the memory exhaustion, I sacrifice the running time. In this approach, the input file will be read 27 times.
The performance can be improved with more sophisticated technique of IO and processing technique. But the current solution can guarantee the code works for most basic environment.
