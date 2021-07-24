/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 andres rosales
 */
package ucf.assignments;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Pattern;

public class Item {
    private final SimpleStringProperty price = new SimpleStringProperty("");
    private final SimpleStringProperty productName = new SimpleStringProperty("");
    private final SimpleStringProperty serialNumber = new SimpleStringProperty("");

    private static final LinkedList<String> ListSerialNumbers = new LinkedList<String>();
    public Item(){
        this("","","");
    }
    public Item(String newValue,  String name, String serial){
        setPrice(newValue);
        setSerialNumber(serial);
        setProductName(name);
    }

    public void setPrice(String newValue) {
        if(Pattern.matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$", newValue)) {
            price.set(newValue);
        }else {
            price.set("Invalid Format");
        }
    }

    public void setSerialNumber(String serial) {
        //make sure serial number is only letters and numbers
        if(Pattern.matches("^[a-zA-Z0-9]+$", serial)) {
            //make sure new serial number doesn't already exist
            if(ListSerialNumbers.isEmpty()){
                serialNumber.set(serial);
                ListSerialNumbers.add(serial);
            }
            else if(ListSerialNumbers.contains(serial)) {
                serialNumber.set("Duplicates not allowed");
            }else{
                //if  ListSerialNumbers doesnt contain the new serial number, set serial and add it to List
                serialNumber.set(serial);
                ListSerialNumbers.add(serial);
            }
        }else{
            serialNumber.set("Alpha-numeric Only");
        }
    }

    public void setProductName(String name) {
        productName.set(name);
    }
    public SimpleStringProperty priceProperty(){

        return price;
    }
    public SimpleStringProperty productNameProperty(){

        return productName;
    }
    public SimpleStringProperty serialNumberProperty(){

        return serialNumber;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Item)) return false;
        Item other = (Item)obj;
        if(this.price.get().equals(other.price.get()) && this.productName.get().equals(other.productName.get()) && this.serialNumber.get().equals(other.serialNumber.get())){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(price, productName, serialNumber);
    }
}