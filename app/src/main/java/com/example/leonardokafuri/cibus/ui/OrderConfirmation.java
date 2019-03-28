package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.TestData;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderConfirmation extends AppCompatActivity {

    //Han: store order number that is passed in from parent activity
    //Note that the position of values are correponding to each other between 3 lists
    //eg. menuList[0] is the first item with price in pricelist[0] that represent orderlist[0]
    private int[] orderList;

    private String[] menuList;

    private double[] priceList;

    private  SharedPreferences sharedPreferences;

    private double totalPrice;

    private DecimalFormat priceFormat;

    private StringBuilder stringBuilder;

    private Button proceed;

    private EditText customerComment;

    private ListView listView;

    private TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        proceed = findViewById(R.id.order_confirmation_btn_confirm);

        customerComment = findViewById(R.id.order_confirmation_edittext_comments);

        listView = findViewById(R.id.order_confirmation_listview);

        total = findViewById(R.id.order_confirmation_total);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        totalPrice = (double) sharedPreferences.getFloat("totalprice",0);

        priceFormat = new DecimalFormat("00.00");

        total.setText("Total amount: $"+ priceFormat.format(totalPrice));

        if(sharedPreferences!=null)
        {

            //Han: check this out @Bogdan
            int startingIndex = sharedPreferences.getInt("startIndexForMenu", -1);

            int lengthOfOrderList = sharedPreferences.getInt("lengthOfOrderList",0);

            orderList = new int[lengthOfOrderList];

            menuList = new String[lengthOfOrderList];

            priceList = new double[lengthOfOrderList];

            ArrayList<com.example.leonardokafuri.cibus.datamodel.Menu> tempList
                    = TestData.getListOfMenus(startingIndex);


            //Han: load order quantity to local int[]
            for(int i = 0 ; i< lengthOfOrderList; i++){
                orderList[i] = sharedPreferences.getInt(String.valueOf(i), 0);
            }

            //Han: load menu names to local Sting[]
            for (int i = 0; i < tempList.size(); i++) {
                menuList[i] = tempList.get(i).getFoodName();


            }

            //Han: load prices to loca double[]
            for (int i = 0; i < priceList.length; i++) {
                priceList[i] = (double)sharedPreferences.getFloat("m"+String.valueOf(i),0);
            }



        }

        List<HashMap<String,String>> aList = new ArrayList<>();

        for (int i = 0; i < menuList.length; i++) {

            if(i == 0){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name","Food");
                hm.put("unitprice", "Unit Price");
                hm.put("unit", "Qty" );
                aList.add(hm);
            }

            //Han: only display ordered items
            if(orderList[i]> 0){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name",menuList[i]);
                hm.put("unitprice", priceFormat.format(priceList[i]));
                hm.put("unit", Integer.toString(orderList[i]) );
                aList.add(hm);
            }

        }


        String[] from= {"name","unitprice","unit"};
        int[] to = {
                R.id.order_confirmation_listview_menu,
                R.id.order_confirmation_listview_unitPrice,
                R.id.order_confirmation_listview_quantity
        };

        SimpleAdapter adapter = new SimpleAdapter(
                OrderConfirmation.this,
                        aList,
                        R.layout.order_confirmation_listview_item,
                        from,
                        to
        );

        listView.setAdapter(adapter);
        //result.setText(stringBuilder);



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
