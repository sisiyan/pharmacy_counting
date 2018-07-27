import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.FileReader;
//import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class Pharmacy_Counting {
    public static void main(String[] args) {
        //Require user input at least the input file.
        if(args.length <1) {
            System.out.println("Please give an input file!");
            return;
        }


        long startTime = System.nanoTime();
        HashMap<String, HashSet<String>> map_dp = new HashMap<>(); //map to store <drug name, prescriber name list for that drug>
        HashMap<String, Float> map_dc = new HashMap<>(); //map to store <drug name, drug total cost>
        Map<Integer, ArrayList<String>> map = new TreeMap<>(Collections.reverseOrder()); //treemap to keep the processed record in order of total cost


        //Process the drug name start with each character.
        for(char ch = 'A'; ch <= 'Z'; ch++) {

            try (Scanner sc = new Scanner(new File(args[0]), "UTF-8")) {

                //long freeMemoryBefore = Runtime.getRuntime().freeMemory();
                //read in records
                sc.nextLine(); //skip the first line (column names)
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] tokens = line.split(",");  //Read each line convert string to a string array
                    if(tokens.length != 5) continue; //Skip the bad inputs.
                    //It is safer add a line of code to check that the last column is number
                    if(tokens[4] != null && !tokens[4].matches("\\d*\\.?\\d+")) continue;

                    if(tokens[3].charAt(0) == ch) { //Process the drug name start with each character.
                        String fullname = tokens[1] + "_" + tokens[2];


                        //Add a new entry in the map if drug name not in the map yet, otherwise update the set of prescriber names
                        if(!map_dp.containsKey(tokens[3])) {
                            map_dp.put(tokens[3], new HashSet<String>());
                            map_dp.get(tokens[3]).add(fullname);
                        }
                        else {
                            map_dp.get(tokens[3]).add(fullname);
                        }

                        //Add a new entry if drug name not already in the map, otherwise update the total drug cost.
                        if(!map_dc.containsKey(tokens[3])) map_dc.put(tokens[3], Float.parseFloat(tokens[4]));
                        else {
                            map_dc.replace(tokens[3], map_dc.get(tokens[3]) + Float.parseFloat(tokens[4]));
                        }
                    }
                 }

                 // note that Scanner suppresses exceptions
                 if (sc.ioException() != null) {
                     sc.ioException().printStackTrace();
                 }
             }
             catch (FileNotFoundException e) {
                 e.printStackTrace();
             }

             //Construct the output lines with drug name, number of prescribers, and total cost
             //put the <totalCost, List<lines>> into the treeMap
             for(String drug: map_dp.keySet()) {
                 int totalCost = Math.round(map_dc.get(drug));
                 String record = drug + ","+ map_dp.get(drug).size() +"," + totalCost; //Construct the output lines
                 if(!map.containsKey(totalCost)) {
                     map.put(totalCost, new ArrayList<String>());
                     map.get(totalCost).add(record);
                 }
                 else map.get(totalCost).add(record);

             }
             map_dp.clear();
             map_dc.clear();
        }

        try (Scanner sc = new Scanner(new File(args[0]), "UTF-8")) {
            //long freeMemoryBefore = Runtime.getRuntime().freeMemory();

            sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                String[] tokens = line.split(",");
                if(tokens.length != 5) continue;

                if(tokens[3].charAt(0) < 'A' || tokens[3].charAt(0) > 'Z') {

                    String fullname = tokens[1] + "_" + tokens[2];

                     if(!map_dc.containsKey(tokens[3])) map_dc.put(tokens[3], Float.parseFloat(tokens[4]));
                     else {
                         map_dc.replace(tokens[3], map_dc.get(tokens[3]) + Float.parseFloat(tokens[4]));
                     }

                     if(!map_dp.containsKey(tokens[3])) {
                         map_dp.put(tokens[3], new HashSet<String>());
                         map_dp.get(tokens[3]).add(fullname);
                     }
                     else {
                         map_dp.get(tokens[3]).add(fullname);
                     }
                 }
             }
             if (sc.ioException() != null) {
                 sc.ioException().printStackTrace();
             }
         }
         catch (FileNotFoundException e) {
             e.printStackTrace();
         }

         for(String drug: map_dp.keySet()) {

             int totalCost = Math.round(map_dc.get(drug));
             String record = drug + ","+ map_dp.get(drug).size() +"," + totalCost;
             if(!map.containsKey(totalCost)) {
                  map.put(totalCost, new ArrayList<String>());
                  map.get(totalCost).add(record);
             }
             else map.get(totalCost).add(record);

         }
         map_dp.clear();
         map_dc.clear();

         if(map.size() == 0) {
            System.out.println("Input file is empty, or All input records are illegally formatted!");
            return;
         }

         String outputFileName;
         if(args.length == 2) {
             File tempFile = new File(args[1]);
             if(tempFile.exists() && !tempFile.isDirectory()) {
                 tempFile.delete();
             }
             //outputFile = new File(args[1]);
             outputFileName = args[1];
         }
         else {
             File tempFile = new File("./output/output.txt");
             if(tempFile.exists() && !tempFile.isDirectory()) {
                 tempFile.delete();
             }
             //outputFile = new File("./output/output.txt");
             outputFileName = "./output/output.txt";
         }

         try {
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
             writer.write("drug_name,num_prescriber,total_cost\n");
             for(List<String> val : map.values()){
                 if(val.size() > 1) {
                     Collections.sort(val);
                     Collections.reverse(val);
                     for(String record: val) {
                         writer.write(record);
                         writer.newLine();
                     }
                 }
                 else {
                     writer.write(val.get(0));
                     writer.newLine();
                 }

             }
             writer.close();
         }
         catch (IOException e1) {
             e1.printStackTrace();
         }

         long endTime = System.nanoTime();
         long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
         System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms"  );
    }
}
