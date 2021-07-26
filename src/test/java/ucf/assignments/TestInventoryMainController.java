package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import ucf.assignments.FileHandler;
import ucf.assignments.Item;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import ucf.assignments.InventoryApp;
import ucf.assignments.InventoryMainController;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 andres rosales
 */
public class TestInventoryMainController {

    @Test//adds 100 items then saves to html, open in app to see it can display all 100 items correctly.
    public void add100Items(){
        Item testItem = new Item("$45.67","Chrysler SUV", "AsdfdsfASa334");
        InventoryMainController controller = new InventoryMainController();

        for(int i=0; i<100; i++){
            controller.addItem(new Item("$" + i,"Chrysler SUV", "AsdfdsfAS" + i));
        }
        assert(controller.itemList.size() == 100);
        //outputs 100 item list to check how it looks when opened in app
        File file = new File("./src/test/resources/add100Items.html");
        FileHandler.saveHTML(controller.itemList,file);
    }
    @Test
    public void testDeleteListItem(){
        InventoryMainController controller = new InventoryMainController();
        //add 2 items to list
        Item newItem = new Item("$44", "xbox controller", "abcd123");
        Item newItem2 = new Item("$55", "headset", "xyz123");
        controller.addItem(newItem);
        controller.addItem(newItem2);
        //take 1 out
        controller.deleteListItem(newItem);
        // new size of list should be 1
        assert(controller.itemList.size() == 1);
    }
    @Test
    public void testSearchByName(){
        //create new item list with 4 items
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        itemList.add(new Item("$33.44","Andres","sdfsttr3443434"));
        itemList.add(new Item("$33.44","Bob","sdfsdfaw3434"));
        itemList.add(new Item("$33.44","Charlie","sdfsdfjjjj434"));
        itemList.add(new Item("$33.44","Dick","sdfggg43434"));
        //make new filtered list with search term Andres
        FilteredList<Item> filteredData = new FilteredList<>(itemList, b -> true);
        SortedList<Item> sortedList = InventoryMainController.makeFilteredList(filteredData, "Andres");
        //should return sortedlist with single item, Andres
        //create singleton list with expected item
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(new Item("$33.44","Andres","sdfsttr3443434"));

        assertIterableEquals(sortedList, expected);
    }
    @Test
    public void testSearchBySerial(){
        //create new item list with 4 items
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        itemList.add(new Item("$33.44","Andres","sdfsttr3443"));
        itemList.add(new Item("$33.44","Bob","sdfsdfaw34"));
        itemList.add(new Item("$33.44","Charlie","sdferwjj434"));
        itemList.add(new Item("$33.44","Dick","sdfggg4334"));
        //make new filtered list with search term Andres
        FilteredList<Item> filteredData = new FilteredList<>(itemList, b -> true);
        SortedList<Item> sortedList = InventoryMainController.makeFilteredList(filteredData, "sdfggg4334");
        //should return sortedlist with single item
        //create singleton list with expected item
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(new Item("$33.44","Dick","sdfggg4334"));

        assertIterableEquals(sortedList, expected);
    }
    //tests that search works simultaneously on product name and serial numbers, and different letter cases
    @Test
    public void testFalseMakeFilteredList(){
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        itemList.add(new Item("$33.44","Andres","sdfsttr3443434"));
        itemList.add(new Item("$33.44","Bob","sfANDRES44"));
        itemList.add(new Item("$33.44","Charlie","sdfsdfjjjj434"));
        itemList.add(new Item("$33.44","Dick","sdfggg43434"));

        FilteredList<Item> filteredData = new FilteredList<>(itemList, b -> true);
        SortedList<Item> sortedList = InventoryMainController.makeFilteredList(filteredData, "Andres");
        //new sorted list should not contain 2 items
        assertEquals(sortedList.size(), 2);
    }
    @Test
    public void testDuplicateSerial(){
        InventoryMainController controller = new InventoryMainController();
        String expected = "Duplicate";
        String Serial1 = "XXX";
        String Serial2 = "XXX";
        String newSerial1 =  controller.checkSerialDuplicate(Serial1);
        String newSerial2 = controller.checkSerialDuplicate(Serial2);
        assertEquals(newSerial2, expected);
    }

    //The Sort product, sort serial numbers, sort price features are built into tableview
    //Ability to edit each cell is also built into user interaction with tableview.
}
