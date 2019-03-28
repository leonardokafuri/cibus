package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.utils.TestData;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderConfirmation extends AppCompatActivity {

    //Han: store order number that is passed in from parent activity
    private int[] orderList;
    //Han: store menu names
    private String[] menuList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        TextView result = findViewById(R.id.result);

        Intent intent = getIntent(); // get the intent that was passed to it
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        float f = sp.getFloat("totalprice",0);
        final double totalPrice = ((double) f);
        DecimalFormat precision = new DecimalFormat("00.00");


        if(intent!=null)
        {
            String qty = intent.getStringExtra("data"); // get the value that was passed
            result.setText(qty); // setting the textview with the value

            //Han: check this out @Bogdan
            int startingIndex = intent.getIntExtra("startIndexForMenu", -1);

            int lengthOfOrderList = intent.getIntExtra("lengthOfOrderList",-1);

            orderList = new int[lengthOfOrderList];

            menuList = new String[lengthOfOrderList];

            ArrayList<com.example.leonardokafuri.cibus.datamodel.Menu> tempList
                    = TestData.getListOfMenus(startingIndex);
            StringBuilder sb = new StringBuilder("Your order is : \r\n");
            //Han: load menu names to local Sting[]
            for (int i = 0; i < tempList.size(); i++) {
                menuList[i] = tempList.get(i).getFoodName();
                orderList[i] = intent.getIntExtra(String.valueOf(i), 0);
                if(orderList[i] == 1)
                    sb.append(menuList[i] + " - " + orderList[i] + "\r\n");
            }
            sb.append("Your total is: " + precision.format(totalPrice));
            result.setText(sb);

            //Han: load order quantity to local int[]
            for(int i = 0 ; i< lengthOfOrderList; i++){
                orderList[i] = intent.getIntExtra(String.valueOf(i), 0);
            }


        }


        Button proceed = findViewById(R.id.goPay);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirmation.this,Payment.class));
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
                startActivity(new Intent(OrderConfirmation.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(OrderConfirmation.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(OrderConfirmation.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(OrderConfirmation.this,Login.class));
                break;
        }
        return true;
    }
}
