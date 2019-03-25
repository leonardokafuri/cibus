package com.example.leonardokafuri.cibus.utils;

import android.util.Log;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.datamodel.Menu;
import com.example.leonardokafuri.cibus.datamodel.Restaurant;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.round;

//this class is to create test data by outputing Objects
public class TestData {

    private static final String LOG_TAG = TestData.class.getSimpleName();

    private static String [] restaurants = {

            "Mcdonalds",
            "Subway",
            "Pizza Hut",
            "KFC",
            "Dominos",
            "Boston Pizza",
            "White Spot"};
    private static int [] images = new int[]{
            R.drawable.mcdonalds,
            R.drawable.subway,
            R.drawable.pizzahut,
            R.drawable.kfc,
            R.drawable.dominos,
            R.drawable.boston,
            R.drawable.whitespot
    };

    private static String[] menuForAll = new String[]{
            "Big Mac Meal",
            "Mighty Angus Original Meal",
            "Quarter Pounder with Cheese Meal",
            "McChicken Meal",
            "Chicken McNuggets Meal",
            "Egg McMuffin Meal",
            "Baked Apple Pie",//0-6, macdonald

            "Rotisserie Chicken - 6",
            "Carved Turkey - 6",
            "Roast Beef - 6",
            "Subway Club - 6",
            "Oven Roasted Chicken - 6",
            "Turkey Breast - 6",
            "Black Forest Ham - 6",//7-13, subway

            "Pepperoni Lover's",
            "Meat Lover's",
            "Ultimate Cheese Lover’s",
            "Veggie Lover’s",
            "Supreme Pizza",
            "BBQ Lover’s",
            "Chicken Supreme",//14-20, pizza hut

            "2 Pc. Chicken",
            "Doublicious Sandwich",
            "Popcorn Nuggets",
            "2 Chicken Littles",
            "Chicken Tenders",
            "Tailgate Meal",
            "Mashed Potatoes",//21-27, KFC

            "America’s Favorite Feast",
            "Bacon Cheeseburger Feast",
            "Deluxe Feast",
            "ExtravaganZZa Feast ",
            "MeatZZa Feast",
            "Ultimate Pepperoni Feast",
            "Wings",//28-34, Dominos

            "Pork Back Ribs",
            "Chicken Parmesan",
            "Ribber-Winger Combo",
            "Chicken Wrap",
            "Boston Brute",
            "The Dig Dipper",
            "NY Steak Sandwich",//35-41, boston

            "The WS Club Combo",
            "Shrimp Sandwich Combo",
            "Nat's Beef Dip Combo",
            "Dippin with Caesar",
            "Tuscan Chicken Pasta",
            "Legendary Burger",
            "Surprising Dish"//42-48, whitespot


    };




    public static ArrayList<Restaurant> getListOfRestaurants(){

        ArrayList<Restaurant> output = new ArrayList<>();

        try{

            for (int i = 0; i < restaurants.length; i++) {
                Restaurant temp = new Restaurant(restaurants[i],images[i]);
                output.add(temp);
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "error converting raw data into objects");

        }


        return output;
    }


    public static ArrayList<Menu> getListOfMenus(int startingRange){
        ArrayList<Menu> output = new ArrayList<>();

        int convertedIndex = -1;

        try{

            //Han: used to create random prices for the menu item that is selected
            Random rand = new Random();

            convertedIndex = startingRange*7;

            //Han: select the next 7 menu items for a test menu as each of the restaurant test menu has 7 dishes
            for (int i = 0; i < 7; i++) {

                //Han: random prices that is below $50 per unit
                double tempPrice =rand.nextDouble()*50;
                Menu temp = new Menu(menuForAll[convertedIndex],tempPrice);
                output.add(temp);
                convertedIndex++;
            }

        }catch(Exception e){
            Log.e(LOG_TAG, "error converting raw data into objects");

        }

        return output;

    }



}
