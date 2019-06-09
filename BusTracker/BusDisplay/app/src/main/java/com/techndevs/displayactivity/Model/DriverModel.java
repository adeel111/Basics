package com.techndevs.displayactivity.Model;

public class DriverModel {
    String name,email,phone,bus_no;


    public DriverModel(String name, String email, String phone, String bus_no) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bus_no = bus_no;
    }

    public DriverModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }
}
