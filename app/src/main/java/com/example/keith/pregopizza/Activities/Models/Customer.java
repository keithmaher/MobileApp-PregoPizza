package com.example.keith.pregopizza.Activities.Models;

/**
 * Created by Keith on 14/03/2018.
 */

public class Customer {

    private String name;
    private String password;
    private String phone;

    public Customer() {
    }

    public Customer(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
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
