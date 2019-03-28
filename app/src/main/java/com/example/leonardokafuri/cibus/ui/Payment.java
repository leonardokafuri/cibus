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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;


import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class Payment extends AppCompatActivity {
    DatabaseHelper dbh;
    RestaurantMenu rm;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dbh = new DatabaseHelper(this);
        final EditText number = findViewById(R.id.CardNum);
        final EditText name = findViewById(R.id.cardName);
        final EditText date = findViewById(R.id.Expire);
        final EditText type = findViewById(R.id.CardType);


        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final TextView result = findViewById(R.id.total2);
        Button submit = findViewById(R.id.submitOrd);
        final CheckBox saveCC = findViewById(R.id.saveCC);
        final Date currentTime = Calendar.getInstance().getTime();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final int id = sharedPreferences.getInt("userId",0);

        float f = sharedPreferences.getFloat("totalprice",0);

        final double totalPrice = ((double) f);

        final DecimalFormat priceFormat = new DecimalFormat("00.00");

        final String RestName = sharedPreferences.getString("name","");

        Cursor c =dbh.getSavedCC(id);
        try{
            if (c.getCount()>0)
            {
                while (c.moveToNext())
                {
                    number.setText(c.getInt(0));
                    name.setText(c.getString(1));
                    date.setText(c.getInt(2));
                    type.setText(c.getString(3));
                }
            }else
            {
                Toast.makeText(Payment.this,"No card saved",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveCC.isChecked())
               {
                    try {
                        boolean saved = dbh.saveCC(number.getText().toString(), name.getText().toString(), date.getText().toString(), type.getText().toString(),id);
                        dbh.saveOrder(id,RestName,currentTime.toString(),totalPrice);
                        if(!saved)
                            Toast.makeText(Payment.this, "Something went wrong, please check your info and try again!", Toast.LENGTH_LONG).show();
                        else
                        {
                            Toast.makeText(Payment.this, "Credit Card successfully saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Payment.this, History.class));

                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(Payment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    startActivity(new Intent(Payment.this, History.class));
                    dbh.saveOrder(id, RestName, currentTime.toString(), totalPrice);
                }


            }
        });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            Cursor promo = dbh.PromotionCode(id);
            try {
                if(promo.getCount()>0)
                {
                    while (promo.moveToNext())
                    {
                        int currentpromoNum = promo.getInt(0);//get the user current promo code value
                        if (checkedId == R.id.pickup) {
                            if (currentpromoNum == 1) {// if user promo code is equals to 1 apply promo
                                result.setText("Your total is " + (priceFormat.format(totalPrice * 0.1)) + "\r\n" + "Promotion code is applied!");
                                promo = dbh.DeletePromoCode(id);
                            } else if (currentpromoNum == 0) {// if user promo code is equals to 0 no promo
                                result.setText("Your total is " + priceFormat.format(totalPrice));
                            }
                        }
                        if (checkedId == R.id.delivery) {
                            if (currentpromoNum == 1) {
                                result.setText("Your total is " + priceFormat.format(((totalPrice * 0.1) + 3.99 + totalPrice)) + " ($3.99 delivery fee)" + "\r\n" + "Promotion code is applied!");
                                promo = dbh.DeletePromoCode(id);
                            } else if (currentpromoNum == 0) {
                                result.setText("Your total is " + priceFormat.format((totalPrice + 3.99)) + " ($3.99 delivery fee)");
                            }
                        }
                    }

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

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
                startActivity(new Intent(Payment.this, AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Payment.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Payment.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Payment.this, Login.class));
                break;
        }
        return true;
    }
}
