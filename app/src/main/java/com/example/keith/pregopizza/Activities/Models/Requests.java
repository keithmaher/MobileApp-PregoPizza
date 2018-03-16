package com.example.keith.pregopizza.Activities.Models;

import java.util.List;

/**
 * Created by Keith on 16/03/2018.
 */

public class Requests {

    private String phone;
    private String name;
    private String address;
    private List<Order> foods;

    public Requests() {
    }

    public Requests(String phone, String name, String address, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.foods = foods;
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

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
