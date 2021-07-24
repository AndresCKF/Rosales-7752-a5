package ucf.assignments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {
    public final static String header = "<!DOCTYPE html>\n<html>\n<body>\n<table>\n";
    public final static String footer = "</table>\n</body>\n</html>\n";

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

    public static void loadTSV(ObservableList<Item> items, File file) throws IOException {
        LinkedList<String> data = readLines(file);
        String[] paramsFromLine;
        for(int i=0; i<data.size();i++){
            //turn each line into tab separated values
            paramsFromLine = data.get(i).split("\t");
            Item loadedItem = new Item( paramsFromLine[0], paramsFromLine[1], paramsFromLine[2]);
            items.add(loadedItem);
        }
    }

    public static void loadHTML(ObservableList<Item> items, File file) {
        LinkedList<String> data = readLines(file);
            try {
                for (int i = 5; i < data.size() - 4; i+=5) {
                    String price = StringUtils.substringBetween(data.get(i), "<th>", "</th>");
                    String productname = StringUtils.substringBetween(data.get(i + 1), "<th>", "</th>");
                    String serialnumber = StringUtils.substringBetween(data.get(i + 2), "<th>", "</th>");
                    items.add(new Item(price, productname, serialnumber));
                }
            }catch (Exception ex) {
                ex.printStackTrace();
        }
    }
    public static List<Item> loadJSON(String file) {
        List<Item> loadItems = new ArrayList<>();
        try {
            Type listOfItemObject = new TypeToken<ArrayList<Item>>() {}.getType();
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(file));
            // convert JSON array to list of users
            loadItems = new Gson().fromJson(reader, listOfItemObject);
            // close reader
            reader.close();
            return loadItems;
        } catch (Exception ex) {
            ex.printStackTrace();
            return loadItems;
        }

    }
    public static LinkedList<String> readLines(File file){
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
