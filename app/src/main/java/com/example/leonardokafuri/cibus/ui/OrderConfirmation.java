package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leonardokafuri.cibus.AccountInfo;
import com.example.leonardokafuri.cibus.History;
import com.example.leonardokafuri.cibus.Login;
import com.example.leonardokafuri.cibus.Payment;
import com.example.leonardokafuri.cibus.Promotion;
import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.TestData;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderConfirmation extends AppCompatActivity {

    //Han: store order number that is passed in from parent activity
    private int[] orderList;
    //Han: store menu names
    private String[] menuList;

    private  SharedPreferences sharedPreferences;

    private double totalPrice;

    private DecimalFormat priceFormat;

    private StringBuilder stringBuilder;

    private TextView result;

    private Button proceed;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        result = findViewById(R.id.result);

        proceed = findViewById(R.id.order_confirmation_btn_confirm);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        totalPrice = (double) sharedPreferences.getFloat("totalprice",0);

        priceFormat = new DecimalFormat("00.00");

        stringBuilder = new StringBuilder("Your order is : \r\n");




        if(sharedPreferences!=null)
        {

            //Han: check this out @Bogdan
            int startingIndex = sharedPreferences.getInt("startIndexForMenu", -1);

            int lengthOfOrderList = sharedPreferences.getInt("lengthOfOrderList",0);

            orderList = new int[lengthOfOrderList];

            menuList = new String[lengthOfOrderList];

            ArrayList<com.example.leonardokafuri.cibus.datamodel.Menu> tempList
                    = TestData.getListOfMenus(startingIndex);


            //Han: load order quantity to local int[]
            for(int i = 0 ; i< lengthOfOrderList; i++){
                orderList[i] = sharedPreferences.getInt(String.valueOf(i), 0);
            }

            //Han: load menu names to local Sting[]
            for (int i = 0; i < tempList.size(); i++) {
                menuList[i] = tempList.get(i).getFoodName();

                if(orderList[i] > 0)
                    stringBuilder.append(menuList[i] + " - " + orderList[i] + "\r\n");
            }

            stringBuilder.append("Your total is: " + priceFormat.format(totalPrice));






        }

        result.setText(stringBuilder);



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirmation.this, Payment.class));
            }
        });
    }

    //3 dot menu
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
                startActivity(new Intent(OrderConfirmation.this, AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(OrderConfirmation.this, History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(OrderConfirmation.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(OrderConfirmation.this, Login.class));
                break;
        }
        return true;
    }
}
