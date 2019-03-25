package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.leonardokafuri.cibus.AccountInfo;
import com.example.leonardokafuri.cibus.datamodel.Restaurant;
import com.example.leonardokafuri.cibus.History;
import com.example.leonardokafuri.cibus.Login;
import com.example.leonardokafuri.cibus.Promotion;
import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.connection.RestaurantsAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class Restaurants extends AppCompatActivity
        implements Restaurants_Adapter.CustomListItemClickListener {


    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    List<Restaurant> restaurantList;

    Restaurants_Adapter restaurantAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        initilizeScreen();

        RestaurantsAsyncTask loadRestaurants = new RestaurantsAsyncTask(restaurantList,restaurantAdapter);

        loadRestaurants.execute();



    }




    // 3 dot menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.home:
                startActivity(new Intent(Restaurants.this, AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Restaurants.this, History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Restaurants.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Restaurants.this, Login.class));
                break;
        }
        return true;
    }

    @Override
    public void customOnListItemClick(int clickedItemIndex) {

        Restaurant clickedRestaurant = restaurantList.get(clickedItemIndex);

        Intent openRestaurantMenu = new Intent(Restaurants.this, RestaurantMenu.class);

        //Han: in real case we can putExtra a link that is specifically point to the
        //menu of clicked restaurant,and will be used to retrieve data in the
        //subsequent screen

        openRestaurantMenu.putExtra("name", clickedRestaurant.getName());

        openRestaurantMenu.putExtra("index", clickedItemIndex);

        startActivity(openRestaurantMenu);

    }

    private void initilizeScreen(){

        recyclerView = findViewById(R.id.restaurant_recyclerView);

        linearLayoutManager = new LinearLayoutManager(Restaurants.this);

        recyclerView.setLayoutManager(linearLayoutManager);

        restaurantList= new ArrayList<>();

        restaurantAdapter = new Restaurants_Adapter(restaurantList,  this);

        recyclerView.setAdapter(restaurantAdapter);
    }
}
