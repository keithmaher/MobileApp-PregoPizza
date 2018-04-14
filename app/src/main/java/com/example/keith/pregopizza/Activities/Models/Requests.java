package com.example.keith.pregopizza.Activities.Models;

import java.util.ArrayList;

/**
 * Created by Keith on 16/03/2018.
 */

public class Requests {
    static int count = 1;
    static int id;
    private String phone;
    private String name;
    private String address;
    private ArrayList<ArrayList<Order>> foods;

    public Requests() {
    }

    public Requests(String phone, String name, String address, ArrayList<ArrayList<Order>> foods) {
        id = count ++;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.foods = foods;
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

    public ArrayList<ArrayList<Order>> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<ArrayList<Order>> foods) {
        this.foods = foods;
    }
}
