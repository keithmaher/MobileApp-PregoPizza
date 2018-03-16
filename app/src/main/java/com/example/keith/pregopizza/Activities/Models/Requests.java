package com.example.keith.pregopizza.Activities.Models;

import java.util.List;

/**
 * Created by Keith on 16/03/2018.
 */

public class Requests {

    private String phone;
    private String name;
    private String address;
//    private List<Order> foods;

    public Requests() {
    }

    public Requests(String phone, String name, String address) {
        this.phone = phone;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
