package ucf.assignments;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import ucf.assignments.FileHandler;
import ucf.assignments.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;
import ucf.assignments.InventoryApp;
import ucf.assignments.InventoryMainController;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 andres rosales
 */
public class TestInventoryMainController {
    InventoryMainController App = new InventoryMainController();

    @Test//adds 100 items then saves to html, open in app to see it can display all 100 items correctly.
    public void add100Items(){
        LinkedList<Item> inventoryList = new LinkedList<Item>();
        Item testItem = new Item("$45.67","Chrysler SUV", "AsdfdsfASa334");

        for(int i=0; i<100; i++){
            inventoryList.add(testItem);
        }
        assert(inventoryList.size() == 100);
        //outputs 100 item list to check how it looks when opened in app
        File file = new File("./src/test/resources/add100Items.html");
        FileHandler.saveHTML(inventoryList,file);
    }


}
