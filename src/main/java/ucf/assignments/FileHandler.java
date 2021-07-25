/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 andres rosales
 */
package ucf.assignments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public final static String header = "<!DOCTYPE html>\n<html>\n<body>\n<table>\n";
    public final static String footer = "</table>\n</body>\n</html>\n";

    public static void saveJSON(ObservableList<Item> items, File file) {
        System.out.println(items.size());
        //convert observable list into a list of jsonItems that GSON can read
        LinkedList<jsonItem> saveList = convertObserv2JSON(items);
        System.out.println(saveList.size());
        Gson gson = new Gson();
        String jsonString = gson.toJson(saveList);
        System.out.println(jsonString);
        try (FileWriter jsonWriter = new FileWriter(file)){
            jsonWriter.write(jsonString);
        }catch (IOException ex){
            System.out.println("An error occurred writing to file.");
            ex.printStackTrace();
        }

    }

    private static LinkedList<jsonItem> convertObserv2JSON(ObservableList<Item> items) {
        LinkedList<jsonItem> returnList = new LinkedList<>();
        for(int i=0; i< items.size(); i++){
            //interim jsonItem holder
            jsonItem temp = new jsonItem(items.get(i).priceProperty().get(),
                    items.get(i).productNameProperty().get(),
                    items.get(i).serialNumberProperty().get());
            returnList.add(temp);
        }
        return returnList;
    }

    public static void saveTSV(List<Item> itemList, File file) {
        try(FileWriter tsvWriter = new FileWriter(file)){
            for(Item element : itemList){
                //write properties, tab after first 2 properties, new line after last.
                tsvWriter.write(element.priceProperty().get() + "\t");
                tsvWriter.write(element.productNameProperty().get() + "\t");
                tsvWriter.write(element.serialNumberProperty().get() + "\n");
            }
        }catch (IOException e) {
            System.out.println("An error occurred writing to file.");
            e.printStackTrace();
        }
    }
    public static void saveHTML(List<Item> itemList, File file) {
        try (FileWriter htmlWriter = new FileWriter(file)) {
            htmlWriter.write(header);
            for(Item element : itemList){
                //create new row
                htmlWriter.write("\t<tr>\n");
                //write the price html line
                htmlWriter.write("\t\t<th>");
                htmlWriter.write(element.priceProperty().get());
                htmlWriter.write("</th>\n");
                //write the item name html line
                htmlWriter.write("\t\t<th>");
                htmlWriter.write(element.productNameProperty().get());
                htmlWriter.write("</th>\n");
                //write the serial number html line
                htmlWriter.write("\t\t<th>");
                htmlWriter.write(element.serialNumberProperty().get());
                htmlWriter.write("</th>\n");

                //close the row
                htmlWriter.write("\t</tr>\n");
            }
            htmlWriter.write(footer);

        }catch (IOException e) {
            System.out.println("An error occurred writing to file.");
            e.printStackTrace();
        }
    }

    public static LinkedList<Item> loadTSV(File file) {
        //Reads in tsv file, every element of data is a whole line from file
        LinkedList<String> data = readLines(file);
        //String[] will hold 3 values, the price, product name, serial number
        String[] paramsFromLine;
        //items is list to be returned to main controller
        LinkedList<Item> items = new LinkedList<>();
        try {
            for (int i = 0; i < data.size(); i++) {
                //turn each line into tab separated values
                paramsFromLine = data.get(i).split("\t");
                //pass String[] paramsFromLine to items List
                Item loadedItem = new Item(paramsFromLine[0], paramsFromLine[1], paramsFromLine[2]);
                items.add(loadedItem);
            }
            return items;
        }catch (Exception ex) {
            ex.printStackTrace();
            return items;
        }
    }

    public static LinkedList<Item> loadHTML(File file) {
        //read every line from file into an element in data
        LinkedList<String> data = readLines(file);
        //items is list to be returned to main controller
        LinkedList<Item> items = new LinkedList<>();
            try {
                //initiate i at 5 to skip header
                //set i parameters to skip from product price to proceeding product price, hence i+=5
                for (int i = 5; i < data.size() - 4; i+=5) {
                    // data was stored in same order, price, productname, serial number.
                    String price = StringUtils.substringBetween(data.get(i), "<th>", "</th>");
                    String productname = StringUtils.substringBetween(data.get(i + 1), "<th>", "</th>");
                    String serialnumber = StringUtils.substringBetween(data.get(i + 2), "<th>", "</th>");
                    items.add(new Item(price, productname, serialnumber));
                }
                return items;
            }catch (Exception ex) {
                ex.printStackTrace();
                return items;
        }
    }
    public static LinkedList<Item> loadJSON(String filePath) {
        FileHandler handler = new FileHandler();
        LinkedList<Item> returnItems = new LinkedList<>();
        //retrieve list of json readable objects
        LinkedList<jsonItem> loadjsonItems = handler.readJSON(filePath);
        //convert into list<Item> to be read by main controller
        for(int i=0; i< loadjsonItems.size(); i++){
            Item newItem = new Item(loadjsonItems.get(i).price, loadjsonItems.get(i).productName, loadjsonItems.get(i).serialNumber);
            returnItems.add(newItem);
        }
        return returnItems;
    }



    public LinkedList<jsonItem> readJSON(String filePath){
        LinkedList<jsonItem> loadItems = new LinkedList<>();
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            Type listOfObj = new TypeToken<LinkedList<jsonItem>>() {}.getType();
            loadItems = new Gson().fromJson(reader, listOfObj);
            // close reader
            reader.close();

            return loadItems;
        } catch (Exception ex) {
            ex.printStackTrace();
            return loadItems;
        }
    }

    public static LinkedList<String> readLines(File file){
        //hold every line from file as an element in data
        LinkedList<String> data = new LinkedList<>();
        try {
            //initialize file object
            Scanner myReader = new Scanner(file);
            //read in lines, creating new link for each line.
            while (myReader.hasNextLine()) {
                data.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
}
