package com.example.muhammadashfaq.eatit.Model;

public class UserModel {
    private String name;
    private String password;
    private String phone;
    private String isStaff;
    public UserModel(String name, String password,String phone) {
        this.name = name;
        this.password = password;
        this.phone=phone;
        isStaff="false";
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

