package ucf.assignments;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.regex.Pattern;

public class Item {
    private final SimpleStringProperty price = new SimpleStringProperty("");
    private final SimpleStringProperty serialNumber = new SimpleStringProperty("");
    private final SimpleStringProperty productName = new SimpleStringProperty("");
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
        if(Pattern.matches("^[a-zA-Z0-9]+$", serial)) {
            serialNumber.set(serial);
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
}
