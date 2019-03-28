package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class History extends AppCompatActivity {

    private DatabaseHelper dbh;

    private ListView listView;

    private Button back;

    private SharedPreferences sharedPreferences;

    private String[] orderIDList,  timeList, amountList, restaurantList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbh = new DatabaseHelper(this);

        back = findViewById(R.id.backhome);

        listView = findViewById(R.id.order_history_listview);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        //retriving which user is using the app by checking his id
        int id = sharedPreferences.getInt("userId",0);


        Cursor c = dbh.viewHistory(id);


        if (c.getCount() > 0) {

            orderIDList = new String[c.getCount()];
            timeList  = new String[c.getCount()];
            amountList = new String[c.getCount()];
            restaurantList = new String[c.getCount()];


            int index = 0;

            while (c.moveToNext()){

                    orderIDList[index] = "Order ID: "+c.getString(0);
                    restaurantList[index] = "Restaurant: "+c.getString(2);
                    timeList[index] = "Time: "+c.getString(3);
                    amountList[index]= "Amount: "+c.getString(4);

                index++;
            }

            List<HashMap<String,String>> aList = new ArrayList<>();



            //Han: put everything into four list
            for (int i = 0; i < orderIDList.length; i++) {

                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("orderID",orderIDList[i]);
                hm.put("restaurant", restaurantList[i]);
                hm.put("time", timeList[i] );
                hm.put("amount", amountList[i]);

                aList.add(hm);
            }


            String[] from= {"orderID","restaurant","time", "amount"};
            int[] to = {
                    R.id.order_history_orderid,
                    R.id.order_history_restaurant,
                    R.id.order_history_order_time,
                    R.id.order_history_order_amount
            };

            SimpleAdapter adapter = new SimpleAdapter(
                    History.this,
                    aList,
                    R.layout.order_history_listview_item,
                    from,
                    to
            );

            listView.setAdapter(adapter);


        } else {
            //display a toast message
            Toast.makeText(History.this, "No orders found", Toast.LENGTH_SHORT).show();
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(History.this, Restaurants.class));
            }
        });
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
                startActivity(new Intent(History.this, AccountInfo.class));
                break;
            case R.id.History:
                Toast.makeText(this,"You already are on the Order History Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.Promotion:
                startActivity(new Intent(History.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(History.this, Login.class));
                break;
        }
        return true;
    }
}
