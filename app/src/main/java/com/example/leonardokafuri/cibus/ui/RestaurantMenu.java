package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.AccountInfo;
import com.example.leonardokafuri.cibus.History;
import com.example.leonardokafuri.cibus.Login;
import com.example.leonardokafuri.cibus.OrderConfirmation;
import com.example.leonardokafuri.cibus.Payment;
import com.example.leonardokafuri.cibus.Promotion;
import com.example.leonardokafuri.cibus.datamodel.Menu;
import com.example.leonardokafuri.cibus.datamodel.Restaurant;
import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.connection.RestaurantsMenuAsyncTask;
import com.example.leonardokafuri.cibus.utils.TestData;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenu extends AppCompatActivity {

    private String restaurantName;

    private ImageView logo;

    private Button orderButton;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private List<Menu> menuList;

    private RestaurantMenu_Adapter rmAdapter;


    private int startingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        initializeScreen();

        RestaurantsMenuAsyncTask loadMenus = new RestaurantsMenuAsyncTask(menuList, rmAdapter,startingIndex);
        loadMenus.execute();

    }

    // 3 dot menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.home:
                startActivity(new Intent(RestaurantMenu.this, AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(RestaurantMenu.this, History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(RestaurantMenu.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(RestaurantMenu.this, Login.class));
                break;
        }
        return true;
    }

    private void initializeScreen(){


        logo = findViewById(R.id.restaurantmenu_logo);
        Intent getFromParent = getIntent();

        if(getFromParent.hasExtra("name") && getFromParent.hasExtra("index")) {

            restaurantName = getFromParent.getStringExtra("name");
            startingIndex = getFromParent.getIntExtra("index", -1);

            ArrayList<Restaurant> restaurants = TestData.getListOfRestaurants();

            loadLogo(restaurants);

            orderButton = findViewById(R.id.restaurantmenu_order);

            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent l = new Intent(RestaurantMenu.this, Payment.class);
                    double price = rmAdapter.getTotalPrice();
                    l.putExtra("str", price);
                    //todo, start to work from the totalprice here
                    Toast.makeText(RestaurantMenu.this, "Total price is " + rmAdapter.getTotalPrice(), Toast.LENGTH_LONG).show();

                    int[] orderList = rmAdapter.getOrderQuantity();

                    int counterForOrderList = 0;
                    Intent openConfirmation = new Intent(RestaurantMenu.this, OrderConfirmation.class);

                    //Han: this is used for the next activity to work out the menus names
                    openConfirmation.putExtra("startIndexForMenu",startingIndex);

                    openConfirmation.putExtra("lengthOfOrderList", orderList.length);
                    //Han: store each order in sequence and pass to order conformation screen
                    for (int i = 0; i < orderList.length; i++) {
                       openConfirmation.putExtra(String.valueOf(i), orderList[i]);
                    }

                    startActivity(openConfirmation);



                }
            });

            recyclerView = findViewById(R.id.restaurantmenu_recyclerview);

            linearLayoutManager = new LinearLayoutManager(RestaurantMenu.this);

            recyclerView.setLayoutManager(linearLayoutManager);

            menuList = new ArrayList<>();

            rmAdapter = new RestaurantMenu_Adapter(menuList);


            recyclerView.setAdapter(rmAdapter);
        }


    }

    private void loadLogo(ArrayList<Restaurant> restaurants){
        //Han: a workaround.
        //normally an Android app would use the url from parent intent to get JSON
        //and get Logo link and other info from JSON, then retrieve logo image based on its link
        for (Restaurant r : restaurants) {
            if (restaurantName.equals(r.getName())) {
                logo.setImageResource(r.getLogo());
                break;
            }
        }
    }
}
