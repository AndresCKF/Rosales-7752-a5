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
public class TestInventoryMainController implements Initializable {
    @FXML TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    searchField.setText("Andres");
    System.out.println(searchField.getText());
    }

    @Test//adds 100 items then saves to html, open in app to see it can display all 100 items correctly.
    public void add100Items(){
        LinkedList<Item> inventoryList = new LinkedList<Item>();
        Item testItem = new Item("$45.67","Chrysler SUV", "AsdfdsfASa334");

        for(int i=0; i<100; i++){
            inventoryList.add(new Item("$" + i,"Chrysler SUV", "AsdfdsfASa334" + i));
        }
        assert(inventoryList.size() == 100);
        //outputs 100 item list to check how it looks when opened in app
        File file = new File("./src/test/resources/add100Items.html");
        FileHandler.saveHTML(inventoryList,file);
    }

    @Test
    public void testMakeFilteredList(){
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
        assertEquals(sortedList.size(), 1);
    }
    @Test
    public void testFalseMakeFilteredList(){
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        itemList.add(new Item("$33.44","Andres","sdfsttr3443434"));
        itemList.add(new Item("$33.44","Bob","sdfsdfaw3434"));
        itemList.add(new Item("$33.44","Charlie","sdfsdfjjjj434"));
        itemList.add(new Item("$33.44","Dick","sdfggg43434"));

        FilteredList<Item> filteredData = new FilteredList<>(itemList, b -> true);
        SortedList<Item> sortedList = InventoryMainController.makeFilteredList(filteredData, "Andres");
        //new sorted list should not contain 2 items
        assertNotEquals(sortedList.size(), 2);
    }
}
