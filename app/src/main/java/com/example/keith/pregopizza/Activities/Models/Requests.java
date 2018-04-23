package com.example.keith.pregopizza.Activities.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keith on 16/03/2018.
 */

public class Requests {
    static int count = 1;
    static int id;
    private String phone;
    private String name;
    private String address;
    private String status;
    private List<Order> foods;

    public Requests() {
    }

    public Requests(String phone, String name, String address, List<Order> foods) {
        id = count ++;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = "Placed";
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getId() {
        return String.valueOf(id);
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
