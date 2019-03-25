package com.example.leonardokafuri.cibus.connection;

import android.os.AsyncTask;

import com.example.leonardokafuri.cibus.datamodel.Restaurant;
import com.example.leonardokafuri.cibus.ui.Restaurants_Adapter;
import com.example.leonardokafuri.cibus.utils.TestData;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsAsyncTask extends AsyncTask<Void,Void, ArrayList<Restaurant>> {

    private List<Restaurant> restaurantList;
    private Restaurants_Adapter rAdapter;

    //Han: the AsyncTask meant to be used for retrieveing JSON data from cloud,
    //however, the process has been simplified because the data is locally stored

    public RestaurantsAsyncTask(
            List<Restaurant> restaurants,
            Restaurants_Adapter rAdapter) {
        this.restaurantList = restaurants;
        this.rAdapter = rAdapter;
    }


    //Han: this is the method that suppose to be used to trigger internet connection
    //however, we use our locally generated data for testing
    @Override
    protected ArrayList<Restaurant> doInBackground(Void... voids)
    {
        return TestData.getListOfRestaurants();
    }

    @Override
    protected void onPostExecute(ArrayList<Restaurant> restaurants) {
        super.onPostExecute(restaurants);

        restaurantList.clear();
        restaurantList.addAll(restaurants);
        rAdapter.notifyDataSetChanged();
    }
}
