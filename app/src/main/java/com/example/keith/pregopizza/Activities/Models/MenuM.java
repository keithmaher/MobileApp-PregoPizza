package com.example.keith.pregopizza.Activities.Models;

/**
 * Created by Keith on 13/03/2018.
 */

public class MenuM {

    private String name, image, toppings, price, menuId;

    public MenuM() {
    }

    public MenuM(String name, String image, String toppings, String price, String menuId) {
        this.name = name;
        this.image = image;
        this.toppings = toppings;
        this.price = price;
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToppings() {
        return toppings;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
