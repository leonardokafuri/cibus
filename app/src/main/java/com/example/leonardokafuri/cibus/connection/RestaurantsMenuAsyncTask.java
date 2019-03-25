package com.example.leonardokafuri.cibus.connection;

import android.os.AsyncTask;

import com.example.leonardokafuri.cibus.datamodel.Menu;
import com.example.leonardokafuri.cibus.ui.RestaurantMenu_Adapter;
import com.example.leonardokafuri.cibus.utils.TestData;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsMenuAsyncTask extends AsyncTask<Void,Void, ArrayList<Menu>> {

    private List<Menu> menuList;
    private RestaurantMenu_Adapter rmAdapter;
    private int startingIndex;

    public RestaurantsMenuAsyncTask(List<Menu> menuList,
                                    RestaurantMenu_Adapter rmAdapter,
                                    int startingIndex) {
        this.menuList = menuList;
        this.rmAdapter = rmAdapter;
        this.startingIndex = startingIndex;
    }

    @Override
    protected ArrayList<Menu> doInBackground(Void... voids) {

        return TestData.getListOfMenus(startingIndex);
    }

    @Override
    protected void onPostExecute(ArrayList<Menu> menus) {
        super.onPostExecute(menus);

        menuList.clear();
        menuList.addAll(menus);

        rmAdapter.updateSelectionList();

        rmAdapter.notifyDataSetChanged();

    }
}
