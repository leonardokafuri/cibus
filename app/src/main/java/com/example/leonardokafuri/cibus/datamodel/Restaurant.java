package com.example.leonardokafuri.cibus.datamodel;


//this is a Restaurant data model
public class Restaurant {
    private String name;
    private int logo;

    public Restaurant(String name, int logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }
}
