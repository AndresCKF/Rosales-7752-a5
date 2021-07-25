package ucf.assignments;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    @Test//2 tests in one, tests save and load
    public void testLoadAndSaveTSV(){
        //unchanged list to be tested against.
        LinkedList<Item> fixed = new LinkedList<>();
        //initialize file to save to then read from.
        File file = new File("./src/test/resources/testLoadAndSaveTSV.tsv");

        fixed.add(new Item("$25.50", "XBOX controller","axzA23"));
        fixed.add(new Item("$325.50", "XBOX 1","fgfdg5454"));
        fixed.add(new Item("$2550", "Gaming Laptop","dfsdfsd3434543fdgfdg"));

        FileHandler.saveTSV(fixed,file);
        LinkedList<Item> loadedList = FileHandler.loadTSV(file);
        assertIterableEquals(fixed, loadedList);
    }
    @Test //2 tests in one, tests save and load
    public void testLoadAndSaveHTML(){
        //unchanged list to be tested against.
        LinkedList<Item> fixed = new LinkedList<>();
        //initialize file to save to then read from.
        File file = new File("./src/test/resources/testLoadAndSaveHTML.html");

        fixed.add(new Item("$25.50", "XBOX controller","axzA23"));
        fixed.add(new Item("$325.50", "XBOX 1","fgfdg5454"));
        fixed.add(new Item("$2550", "Gaming Laptop","dfsdfsd3434543fdgfdg"));

        FileHandler.saveHTML(fixed,file);
        LinkedList<Item> loadedList = FileHandler.loadHTML(file);
        assertIterableEquals(fixed, loadedList);
    }

    @Test //2 tests in one, tests save and load
    public void testLoadAndSaveJSON(){
        //unchanged list to be tested against.
        LinkedList<Item> fixed = new LinkedList<>();
        //initialize file to save to then read from.
        File file = new File("./src/test/resources/testLoadAndSaveJSON.json");

        fixed.add(new Item("$255.50", "XBOX controller","vffgfgfg34"));
        fixed.add(new Item("$32435.50", "XBOX 1","dddddddddddddddd"));
        fixed.add(new Item("$253350", "Gaming Laptop","dfsdfsd3434543fdgfdg"));

        FileHandler.saveJSON(fixed,file);
        LinkedList<Item> loadedList = FileHandler.loadJSON(file.getAbsolutePath());
        assertIterableEquals(fixed, loadedList);
    }

}