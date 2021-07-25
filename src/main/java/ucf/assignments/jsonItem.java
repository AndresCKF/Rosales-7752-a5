package ucf.assignments;

public class jsonItem {
    public String price;
    public  String productName;
    public String serialNumber;

    jsonItem(){
        new jsonItem("", "", "");
    }
    jsonItem(String newPrice, String newProductName, String newSerialNumber){
        this.price = newPrice;
        this.productName = newProductName;
        this.serialNumber = newSerialNumber;
    }
}
