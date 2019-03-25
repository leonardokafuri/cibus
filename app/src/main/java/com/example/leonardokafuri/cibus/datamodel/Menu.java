package com.example.leonardokafuri.cibus.datamodel;


//this is a data model class!
public class Menu {
    private String foodName;
    private double pricePerUnit;

    public Menu(String foodName, double pricePerUnit) {
        this.foodName = foodName;
        this.pricePerUnit = pricePerUnit;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}
