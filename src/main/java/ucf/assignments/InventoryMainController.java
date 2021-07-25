/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 andres rosales
 */
package ucf.assignments;

import com.sun.javafx.scene.control.DoubleField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

public class InventoryMainController implements Initializable {
    @FXML
    public TableView<Item> tableView;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn serialColumn;
    @FXML
    private TableColumn productColumn;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField serialTextField;
    @FXML
    private TextField productTextField;
    @FXML
    private TextField searchField;
    public final ObservableList<Item> itemList = FXCollections.observableArrayList();
    private final LinkedList<String> serialList = new LinkedList<>();
    @FXML
    public void addItemButton(javafx.event.ActionEvent actionEvent) {
        //cut description field to 256 characters
        String newName;
        if (productTextField.getText().length() > 256) {
            newName = productTextField.getText().substring(0, 256);
        } else {
            newName = productTextField.getText();
        }
        //check serialnumber against list
        String newSerial = checkSerialDuplicate(serialTextField.getText());
        //initialize new object Item
        addItem(new Item(priceTextField.getText(), newName, newSerial));
    }
    public void addItem(Item newitem){
        itemList.add(newitem);
        tableView.setItems(itemList);

    }

    //checks input string against serialList
    public String checkSerialDuplicate(String text) {
        //if list empty add new serial number
        if(serialList.isEmpty()){
            serialList.add(text);
            return text;
        }
        //if serialist contains text set serial to Duplicate
        else if(serialList.contains(text)) {
            return "Duplicate";
        }else{
            //if  ListSerialNumbers doesnt contain the new serial number, return serial and add it to List
            serialList.add(text);
            return text;
        }
    }

    @FXML
    public void deleteClicked(ActionEvent event) {
        deleteListItem(tableView.getSelectionModel().getSelectedItem());
    }

    public void deleteListItem(Item selectedItem) {
        itemList.remove(selectedItem);
        tableView.setItems(itemList);
    }

    @FXML
    public void loadInventoryListClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        //sets title, file extension filter, initial directory
        configureFileChooser(fileChooser);
        //display file chooser
        Stage thisStage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(thisStage);
        //after retrieving file from user,  do the loading
        loadInventoryList(file);
    }

    private void loadInventoryList(File file) {
        if (file != null) {
            //get file path and extension
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
            String filePath = file.getAbsolutePath();
            //use file extension to call correct FileHandler method
            if (fileExtension.toLowerCase(Locale.ROOT).equals("tsv")) {
                LinkedList<Item> loadedList = FileHandler.loadTSV(file);
                //clear old list
                itemList.clear();
                //load new list
                itemList.addAll(loadedList);
                tableView.setItems(itemList);
            } else if (fileExtension.toLowerCase(Locale.ROOT).equals("html")) {
                LinkedList<Item> loadedList = FileHandler.loadHTML(file);
                //clear old list
                itemList.clear();
                //load new list
                itemList.addAll(loadedList);
                tableView.setItems(itemList);

            } else if (fileExtension.toLowerCase(Locale.ROOT).equals("json")) {
                LinkedList<Item> loadedList = FileHandler.loadJSON(filePath);
                //clear old list
                itemList.clear();
                //load new list
                itemList.addAll(loadedList);
                tableView.setItems(itemList);

            }
        }
    }

    @FXML
    public void saveInventoryListClick(ActionEvent actionEvent) {
        //open file chooser, configure it, get file from user.
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        Stage thisStage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showSaveDialog(thisStage);
        //pass user selected file to save function
        saveInventoryList(file);
    }

    public void saveInventoryList(File file) {
        //get file name
        //get file extension
        //pass file and working list to correct FileHandler function.
        if (file != null) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
            if (fileExtension.toLowerCase(Locale.ROOT).equals("tsv")) {
                FileHandler.saveTSV(tableView.getItems(), file);
            } else if (fileExtension.toLowerCase(Locale.ROOT).equals("html")) {
                FileHandler.saveHTML(tableView.getItems(), file);
            } else if (fileExtension.toLowerCase(Locale.ROOT).equals("json")) {
                FileHandler.saveJSON(tableView.getItems(), file);
            }

        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose List File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        //set filechooser to use just 3 extensions specified
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML", "*.html"),
                new FileChooser.ExtensionFilter("TSV", "*.tsv"),
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //make price column editable
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> event) {
                        ((Item) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setPrice(event.getNewValue());
                        tableView.refresh();
                    }
                }
        );
        //make serial number column editable
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> event) {
                        ((Item) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setSerialNumber(checkSerialDuplicate(event.getNewValue()));
                        tableView.refresh();
                    }
                }
        );
        //make product name column editable
        productColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Item, String> event) {
                        ((Item) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setProductName(event.getNewValue());
                    }
                }
        );


        // Create Search Listener
        //first wrap itemList in filtered List
        FilteredList<Item> filteredData = new FilteredList<>(itemList, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            // Wrap the FilteredList in a SortedList.
            SortedList<Item> sortedList = makeFilteredList(filteredData, newValue);
            // Bind the SortedList comparator to the TableView comparator.
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
            // Add sorted (and filtered) data to the table.
            tableView.setItems(sortedList);
            System.out.println(newValue);
        });
        //End Search Listener
    }
    //returns a sortedList with only items that contain a hit for searchField: newValue
    public static SortedList<Item> makeFilteredList(FilteredList<Item> filteredData, String newValue) {
        // Set the filter whenever the filter changes.
        filteredData.setPredicate(product -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare price, productname, serialnumber, with filter text.
            String lowerCaseFilter = newValue.toLowerCase();

            if (product.priceProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Search matches price.
            } else if (product.productNameProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Search matches product name.
            } else if (product.serialNumberProperty().get().contains(lowerCaseFilter)) {
                return true; // Search matches serial number
            } else {
                return false; // Does not match.
            }
        });
        return new SortedList<>(filteredData);
    }
}
