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
import java.util.*;

public class InventoryMainController implements Initializable {
    @FXML private TableView<Item> tableView;
    @FXML private AnchorPane anchorPane;
    @FXML private TableColumn priceColumn;
    @FXML private TableColumn serialColumn;
    @FXML private TableColumn productColumn;
    @FXML private TextField priceTextField;
    @FXML private TextField serialTextField;
    @FXML private TextField productTextField;
    @FXML private TextField searchField;
    private final ObservableList<Item> itemList = FXCollections.observableArrayList();
    @FXML
    public void addItemButton(javafx.event.ActionEvent actionEvent){
        //cut description field to 256 characters
        String newName;
        if(productTextField.getText().length() > 256) {
            newName = productTextField.getText().substring(0, 256);
        }else{
            newName = productTextField.getText();
        }
        //initialize new object Item
        tableView.getItems().add(new Item(priceTextField.getText(), newName, serialTextField.getText()));

    }

    @FXML
    public void deleteClicked(ActionEvent event){
        deleteListItem();
    }

    private void deleteListItem() {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
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
                //get file name and extension
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
                //use extension to call correct FileHandler method
                tableView.getItems().clear();
                if(fileExtension.toLowerCase(Locale.ROOT).equals("tsv")){
                    LinkedList<Item> loadedList = FileHandler.loadTSV(file);
                    //add loadedList to tableView one by one.
                    itemList.addAll(loadedList);
                }else if(fileExtension.toLowerCase(Locale.ROOT).equals("html")){
                    LinkedList<Item> loadedList = FileHandler.loadHTML(file);
                    itemList.addAll(loadedList);
                }else if(fileExtension.toLowerCase(Locale.ROOT).equals("json")){
                    ObservableList<Item> loadList = tableView.getItems();
                    loadList.clear();
                    //get List from loadJSON and add all to observable list
                    loadList.addAll(FileHandler.loadJSON(fileName));
                }
        }
    }

    @FXML
    public void saveInventoryListClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        Stage thisStage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showSaveDialog(thisStage);
        saveInventoryList(file);
    }
    public void saveInventoryList(File file){
        if (file != null) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
            if(fileExtension.toLowerCase(Locale.ROOT).equals("tsv")){
                FileHandler.saveTSV(tableView.getItems(), file);
            }else if(fileExtension.toLowerCase(Locale.ROOT).equals("html")){
                FileHandler.saveHTML(tableView.getItems(), file);
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
        public void initialize(URL location, ResourceBundle resources){

            //make each column editable
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
            serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            serialColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Item, String> event) {
                            ((Item) event.getTableView().getItems().get(
                                    event.getTablePosition().getRow())
                            ).setSerialNumber(event.getNewValue());
                            tableView.refresh();
                        }
                    }
            );
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

            // 2. Set the filter whenever the filter changes.
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(product -> {
                    // If filter text is empty, display all persons.

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare price, productname, serialnumber, with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (product.priceProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    } else if (product.productNameProperty().get().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches last name.
                    }
                    else if (product.serialNumberProperty().get().contains(lowerCaseFilter))
                        return true;
                    else
                        return false; // Does not match.
                });
            });

            // Wrap the FilteredList in a SortedList.
            SortedList<Item> sortedData = new SortedList<>(filteredData);

            // Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            tableView.setItems(sortedData);
            //End Search Listener
        }
}
