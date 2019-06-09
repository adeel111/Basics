package com.example.muhammadashfaq.eatit.Model;

import com.example.muhammadashfaq.eatit.Order;

import java.util.List;

public class Request {

    private String phone;
    private String name;
    private String adress;
    private  String total;
    private List<com.example.muhammadashfaq.eatit.Model.Order> foods;
    private String status;


    public Request() {
    }

    public Request(String phone, String name, String adress, String total, List<com.example.muhammadashfaq.eatit.Model.Order> foods,String status)
    {
        this.phone=phone;
        this.name=name;
        this.adress=adress;
        this.total=total;
        this.foods=foods;
        this.status="0";
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<com.example.muhammadashfaq.eatit.Model.Order> getFoods() {
        return foods;
    }

    public void setFoods(List<com.example.muhammadashfaq.eatit.Model.Order> carts) {
        this.foods = carts;
    }
}
