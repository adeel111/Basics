package com.example.muhammadashfaq.eatit.Model;

public class Order {
    private String Productid,ProductName,Quanitity,Price,Discount;

    public Order() {
    }

    public Order(String productid, String productName, String quanitity, String price, String discount) {
        Productid = productid;
        ProductName = productName;
        Quanitity = quanitity;
        Price = price;
        Discount = discount;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuanitity() {
        return Quanitity;
    }

    public void setQuanitity(String quanitity) {
        Quanitity = quanitity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
